package gbicc.irs.reportTemplate.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.exception.CreateObjectAlreadyExistsException;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.DaoExceptionProvider;
import org.wsp.framework.jpa.service.support.DaoExceptionProviderAdaptor;

import com.googlecode.aviator.AviatorEvaluator;

import gbicc.irs.reportTemplate.jpa.entity.FinancialBasicsLib;
import gbicc.irs.reportTemplate.jpa.repository.FinancialBasicsLibRepository;
import gbicc.irs.reportTemplate.jpa.support.FinancialStatementsWrap;
import gbicc.irs.reportTemplate.service.FinancialBasicsLibService;

@Service
public class FinancialBasicsLibServiceImpl extends DaoServiceImpl<FinancialBasicsLib, String, FinancialBasicsLibRepository>
		implements FinancialBasicsLibService {

	private static Log log = LogFactory.getLog(FinancialBasicsLibServiceImpl.class);
	
	@Override
	public DaoExceptionProvider<FinancialBasicsLib, String> getExceptionProvider() {
		return new DaoExceptionProviderAdaptor<FinancialBasicsLib, String>(){
			@Override
			public Exception getCreateObjectAlreadyExistsException(FinancialBasicsLib entity) {
				return new RuntimeException("基础指标编号或名称已存在！");
			}
		};
	}

	@Override
	protected FinancialBasicsLib findByUniqueKey(FinancialBasicsLib entity) throws Exception {
		List<FinancialBasicsLib> resuts = repository.findByBasicsCodeOrBasicsName(entity.getBasicsCode(), entity.getBasicsName());
		if(resuts != null && resuts.size() > 0) {
			return resuts.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> calculateBaseLib(List<FinancialStatementsWrap> finStats) throws Exception {
		//待计算指标
		List<FinancialBasicsLib> financialBasicsLibs = repository.findAll();
		if(finStats == null || finStats.size()==0) {
			Map<String,Object> resultMap = new HashMap<String, Object>();
			//补齐三期空基础数据项
			this.defaultThirdYearBasics(financialBasicsLibs, resultMap,3);
			return resultMap;
		}
		return this.calculating(finStats, null, financialBasicsLibs);
	}
	
	/**
	 * 设置第三年基础指标为0
	 * @param financialBasicsLibs
	 * @param basicsMap
	 * @return
	 */
	private Map<String,Object> defaultThirdYearBasics(List<FinancialBasicsLib> financialBasicsLibs,Map<String,Object> basicsMap,int countNum){
		for(int i=0;i<countNum;i++) {
			for(FinancialBasicsLib bl : financialBasicsLibs) {
				basicsMap.put(bl.getBasicsName()+"_L"+i+1, null);
			}
		}
		return basicsMap;
	}
	
	/**
	 * 获取基础指标后缀
	 * @param num
	 * @return
	 */
	private String getBasicsSuffix(int num) {
		if(num==0) {
			return "N";
		}else if(num==1){
			return "L";
		}else {
			return "L"+(num-1);
		}
	}
	
	/**
	 * 根据财报类型获取计算公式
	 * @param reportType
	 * @param basicsLib
	 * @return
	 */
	private String getBasicsLibFormula(String reportType,FinancialBasicsLib basicsLib) {
		if(reportType.equals("GENERALENTERPRISE")) { //一般企业新准则
			return basicsLib.getGeneralenterprise();
		}else if(reportType.equals("GENERALENTERPRISEOLD")) {//一般企业旧准则
			return basicsLib.getGeneralenterpriseold();
		}else if(reportType.equals("SMALLENTERPRISE")) {//小企业一般准则
			return basicsLib.getSmallenterprise();
		}else if(reportType.equals("SMALLENTERPRISEOLD")) {//小企业简易准则
			return basicsLib.getSmallenterpriseold();
		}else if(reportType.equals("INSTITUTION")) {//事业单位新准则
			return basicsLib.getInstitution();
		}else if(reportType.equals("INSTITUTIONOLD")) {//事业单位旧准则
			return basicsLib.getInstitutionold();
		}
		return null;
	}

	@Override
	public boolean checkBasicsLibExist(String basicsCode, String basicsName) throws Exception {
		List<FinancialBasicsLib> basicsLibs = repository.findByBasicsCodeOrBasicsName(basicsCode, basicsName);
		if(basicsLibs != null && basicsLibs.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> calculateBaseLib(List<FinancialStatementsWrap> finStats, Map<String, Object> parameters)
			throws Exception {
		//待计算指标
		List<FinancialBasicsLib> financialBasicsLibs = repository.findAll();
		if(finStats == null || finStats.size()==0) {
			Map<String,Object> resultMap = new HashMap<String, Object>();
			//补齐三期空基础数据项
			this.defaultThirdYearBasics(financialBasicsLibs, resultMap,3);
			return resultMap;
		}
		return this.calculating(finStats, parameters, financialBasicsLibs);
	}

	private Map<String,Object> calculating(List<FinancialStatementsWrap> finStats, Map<String, Object> parameters,
			List<FinancialBasicsLib> financialBasicsLibs) {
		
		if(parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		
		//计算结果
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//根据年份排序倒序
		Collections.sort(finStats, new Comparator<FinancialStatementsWrap>() {
			public int compare(FinancialStatementsWrap arg0, FinancialStatementsWrap arg1) {
				Integer year0 = Integer.valueOf(arg0.getReportBussDate());
				Integer year1 = Integer.valueOf(arg1.getReportBussDate());
                return year1.compareTo(year0);
            }
		});
		
		String basicsSuffix = null;
		//计算每期财报基础指标，并标记期次
		for(int i =0;i<finStats.size();i++) {
			//获取基础指标结果后缀(N:本期 L:上期  Ln:上n期)
			basicsSuffix = this.getBasicsSuffix(i);
			FinancialStatementsWrap statement = finStats.get(i);
			String reportType = statement.getReportType();
			//指标参数
			parameters.putAll(statement.getAccountData());
			
			//循环计算基础指标
			for(FinancialBasicsLib basicsLib : financialBasicsLibs) {
				String formula = this.getBasicsLibFormula(reportType, basicsLib);
				log.info("计算基础指标："+basicsLib.getBasicsName()+"="+formula);
				Object basicsResult = null;
				if(StringUtils.hasText(formula)) {
					try {
						basicsResult = AviatorEvaluator.execute(formula,parameters);
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e);
					}
					 log.info("基础指标："+basicsLib+"的计算结果为:"+basicsResult);
					 //put结果值
					 resultMap.put(basicsLib.getBasicsName()+"_"+basicsSuffix, basicsResult);
				}else {
					log.error("基础指标:"+basicsLib.getBasicsName() +"公式为空！");
					//put结果值
					resultMap.put(basicsLib.getBasicsName()+"_"+basicsSuffix, basicsResult);
				}
			}
		}
		//根据财报期次 补全缺失年份指标为 null
		if(finStats.size() == 2) {
			this.defaultThirdYearBasics(financialBasicsLibs, resultMap,1);
		}else if(finStats.size() == 1) {
			this.defaultThirdYearBasics(financialBasicsLibs, resultMap,2);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> calculateAssignBaseLib(String[] baseListCode, List<FinancialStatementsWrap> finStats,
			Map<String, Object> parameters) throws Exception {
		List<String> baseListCodes = Arrays.asList(baseListCode);
		//待计算指标
		List<FinancialBasicsLib> basicsLibs = repository.findByBasicsCodeIn(baseListCodes);
		List<FinancialBasicsLib> financialBasicsLibs = repository.findAll();
		if(finStats == null || finStats.size()==0) {
			Map<String,Object> resultMap = new HashMap<String, Object>();
			//补齐三期空基础数据项
			this.defaultThirdYearBasics(financialBasicsLibs, resultMap,3);
			return resultMap;
		}
		return this.calculating(finStats, parameters, basicsLibs);
	}
	
}
