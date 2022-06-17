package net.gbicc.app.xbrl.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.OrgService;

import net.gbicc.app.xbrl.enums.XBRLEnum;
import net.gbicc.app.xbrl.jpa.entity.XbrlRegulatorMappingEntity;
import net.gbicc.app.xbrl.jpa.exception.AppXbrlException;
import net.gbicc.app.xbrl.jpa.repository.XbrlRegulatorMappingRepository;
import net.gbicc.app.xbrl.properties.XBRLProperties;
import net.gbicc.app.xbrl.service.XbrlRegulatorMappingService;
import net.gbicc.app.xbrl.util.XbrlUtil;
import net.sf.json.JSONObject;

/**
* 监管机构映射serviceImpl
*/
@Service
public class XbrlRegulatorMappingServiceImpl extends DaoServiceImpl<XbrlRegulatorMappingEntity, String, XbrlRegulatorMappingRepository> 
	implements XbrlRegulatorMappingService {

	@Autowired	private XBRLProperties xbrlProperties;
	@Autowired	private OrgService orgService;
	
	@Override
	public Map<String, Object> xbrlRegister(String companyCode,Boolean isRead) throws Exception {
		if(StringUtils.isBlank(companyCode)) {
			throw new AppXbrlException("无法登录验证XBRL：机构编码为空！"); 
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String url = xbrlProperties.getServerPath() + xbrlProperties.getRegisterRequest() + "?" +
				 xbrlProperties.getCompanyParam() + "=" + companyCode;
		/*优化：XBRL初始化的时候直接在XBRL的cr_company表中映射好，不用此系统的映射表。
		QXbrlRegulatorMappingEntity qEntity = QXbrlRegulatorMappingEntity.xbrlRegulatorMappingEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qEntity.companyCode.eq(companyCode));
		Optional<XbrlRegulatorMappingEntity> xbrlMappOptional = getRepository().findOne(builder.getValue());
		if( ! xbrlMappOptional.isPresent()) {
			throw new AppXbrlException("无法登录验证XBRL：编码"+companyCode+"的机构无XBRL映射！"); 
		}
		XbrlRegulatorMappingEntity mapp = xbrlMappOptional.get();*/
		if(isRead != null) {
			if(isRead) {
				url += "&" + xbrlProperties.getGrantParam() + "=" + xbrlProperties.getQueryGrant();
			}
		}
		HttpGet request =null;
        try {
        	// 根据地址获取请求
             request = new HttpGet(url);//这里发送get请求
            // 获取当前客户端对象
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
            	throw new AppXbrlException("无法登录验证XBRL：请求出错！");
            }else{
                String responseBody = XbrlUtil.getStreamAsString(response.getEntity().getContent(), XBRLEnum.CHARSET.getValue());
                JSONObject json = JSONObject.fromObject(responseBody);
                if(json.has(XBRLEnum.XBRL_SERVER_REQUEST_VAR.getValue())) {//信息编码
                	String code = json.getString(XBRLEnum.XBRL_SERVER_REQUEST_VAR.getValue());
                	if(code.equals(String.valueOf(HttpStatus.SC_OK))) {//成功编码
                		if(json.has(xbrlProperties.getTokenParam())) {
                        	map.put(xbrlProperties.getTokenParam(), json.get(xbrlProperties.getTokenParam()));
                        	//不用此系统的映射表，在XBRL的cr_company表中直接映射好
                        	//map.put(xbrlProperties.getCompanyParam(), mapp.getRegulatorCode());
                        	map.put(xbrlProperties.getCompanyParam(), companyCode);
                        }
                	}else {//失败
                		String errorInfo = "错误码：" + code;
                		if(json.has(XBRLEnum.XBRL_SERVER_REQUEST_MESSAGE.getValue())) {
                			errorInfo += "，信息：" + json.getString(XBRLEnum.XBRL_SERVER_REQUEST_MESSAGE.getValue());
                		}
                		throw new AppXbrlException(errorInfo);
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppXbrlException("无法登录验证XBRL："+e.getMessage());
        }finally{
            if(request != null){
            	request.releaseConnection();
            }
        }
		return map;
	}

	@Override
	public Map<String, Object> xbrlReport(String companyCode, String reportEndDate,String reportType,Boolean isInit) throws Exception {
		if(StringUtils.isBlank(companyCode) || StringUtils.isBlank(reportEndDate) 
				|| StringUtils.isBlank(reportType) || isInit == null) {
			throw new AppXbrlException("无法初始化或查看报告:传入的参数存在空！");
		}
		//格式化期次日期
		reportEndDate = XbrlUtil.getXbrlReportEndDate(reportEndDate);
		//先默认不赋只读权限
        Map<String, Object> registerMap = xbrlRegister(companyCode, true);
		if(MapUtils.isEmpty(registerMap)) {
			throw new AppXbrlException("无法初始化或查看报告:登录不成功！");
		}
		String xbrlUrl = xbrlProperties.getServerPath();
		if(isInit) {
			xbrlUrl += xbrlProperties.getInitReportRequest();
		}else {
			xbrlUrl += xbrlProperties.getViewReportRequest();
		}
		xbrlUrl += "?" +
		xbrlProperties.getTokenParam() + "=" + registerMap.get(xbrlProperties.getTokenParam()) + "&" +
		xbrlProperties.getCompanyParam() + "=" + registerMap.get(xbrlProperties.getCompanyParam()) + "&" +
		xbrlProperties.getReportTypeParam() + "=" + reportType + "&" +
                xbrlProperties.getReportEndDateParam() + "=" + reportEndDate;
		Map<String, Object> map = XbrlUtil.getMap(true);
		map.put(XBRLEnum.XBRL_URL.getValue(), xbrlUrl);
		return map;
	}

	@Override
	public Map<String, Object> irrXbrlReport(String companyCode, String reportEndDate, Boolean isInit)
			throws Exception {
		if(StringUtils.isBlank(companyCode) || StringUtils.isBlank(reportEndDate) || isInit == null) {
			throw new AppXbrlException("无法初始化或查看报告:传入的参数存在空！");
		}
		//判断irr报告类型,恒安就一个总公司跟目录，可直接判断
		/*QOrg qOrg = QOrg.org;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qOrg.code.eq(companyCode));
		Optional<Org> orgOptional = orgService.getRepository().findOne(builder.getValue());
		if( ! orgOptional.isPresent()) {
			throw new AppXbrlException("无法初始化或查看报告:编码"+companyCode+"的机构不存在！");
		}
		Org org = orgOptional.get();*/
		String reportType = null;
		if(XBRLEnum.HA_STANDARD_LIFE_HEAD_ORG_CODE.getValue().equals(companyCode)) {
			reportType = xbrlProperties.getCompRiskRateReportCode();
		}else {
			reportType = xbrlProperties.getBranchRateReportCode();
		}
		return xbrlReport(companyCode, reportEndDate, reportType, isInit);
	}
}
