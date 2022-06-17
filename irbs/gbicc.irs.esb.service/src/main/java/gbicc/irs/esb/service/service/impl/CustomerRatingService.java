/*package gbicc.irs.esb.service.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.flowable.engine.runtime.ProcessInstance;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.engine.model.core.po.Model;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.flowable.service.ProcessEntityService;
import org.wsp.framework.flowable.service.impl.AbstractFlowableDaoServiceImpl;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.ftp.client.FtpClientConfig;
import com.dcfs.esb.ftp.client.FtpGet;
import com.dcfs.esb.ftp.server.error.FtpException;

import gbicc.irs.customer.entity.CompanyCustomer;
import gbicc.irs.customer.service.CompanyCustomerService;
import gbicc.irs.ec.rating.entity.CompanyRating;
import gbicc.irs.ec.rating.entity.RatingCompanyCustomer;
import gbicc.irs.ec.rating.entity.RatingFinAccountData;
import gbicc.irs.ec.rating.entity.RatingFinancialStatements;
import gbicc.irs.ec.rating.repository.CompanyRatingRepository;
import gbicc.irs.ec.rating.service.CompanyRatingService;
import gbicc.irs.ec.rating.service.impl.CompanyRatingServiceImpl;
import gbicc.irs.ec.rating.support.DateUtil;
import gbicc.irs.ec.rating.support.DictionaryConstant;
import gbicc.irs.ec.rating.support.ModelConstant;
import gbicc.irs.ec.rating.support.RatingConstants;
import gbicc.irs.ec.rating.support.RoleConstant;
import gbicc.irs.ec.ratingconfig.entity.RatingConfig;
import gbicc.irs.ec.ratingconfig.service.ModelSelectorConfigService;
import gbicc.irs.ec.ratingconfig.service.RatingCompanyCustomerService;
import gbicc.irs.ec.ratingconfig.service.RatingConfigService;
import gbicc.irs.ec.ratingconfig.service.RatingFinAccountDataService;
import gbicc.irs.ec.ratingconfig.service.RatingFinancialStatementsService;
import gbicc.irs.esb.service.constant.EsbConstant;
import gbicc.irs.esb.service.constant.FinancialReportConstant;
import gbicc.irs.esb.service.request.RatingCustomerRequest;
import gbicc.irs.esb.service.request.RatingFinancialReportRequest;
import gbicc.irs.esb.service.request.RatingFinancialSubjectRequest;
import gbicc.irs.esb.service.response.Response;
import gbicc.irs.esb.service.support.FileUtils;
import gbicc.irs.esb.service.support.JsonUtil;
import gbicc.irs.reportTemplate.service.CheckedRelationshipService;

@Service("CustomerRatingService")
public class CustomerRatingService extends
AbstractFlowableDaoServiceImpl<CompanyRating, String, CompanyRatingRepository>{
	private static Log log = LogFactory.getLog(CustomerRatingService.class);
	@Autowired
	private ModelSelectorConfigService modelSelectorConfigService;
	@Autowired
	private RatingCompanyCustomerService customerService;
	@Autowired
	private RatingFinancialStatementsService financialReportService;
	@Autowired
	private RatingFinAccountDataService financialSubjectService;
	@Autowired
	private CheckedRelationshipService checkedRelationshipService;
	@Autowired
	private RatingConfigService ratingConfigService;
	@Autowired
	private CompanyRatingService companyRatingService;
	@Autowired
	private CompanyRatingServiceImpl companyRatingServiceImpl;
	@Autowired
	private ProcessEntityService processEntityService;
	@Autowired
	private OrgService orgService;
	@Autowired 
	private CompanyCustomerService companyCustomerService;
	@Autowired 
	private SystemDictionaryService systemDictionaryService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	// 系统状态标识
	private String ret_status = EsbConstant.STATUS_SUCCESS;
	// 业务状态标识
	private String ret_code = EsbConstant.CODE_SUCCESS;
	// 调用结果描述
	private String ret_msg = EsbConstant.MSG_SUCCESS;
	
	@Transactional
	public void execute(CompositeData req) throws Exception{
		CompositeData file_head = req.getStruct("FILE_HEAD");
		String file_path = file_head.getField("FILE_PATH").strValue();
		if(StringUtils.isEmpty(file_path)) throw new Exception("获取不到接口上传文件路径"); 
		// 文件下载
		String url = this.fileDownload(file_path);
		// 数据解析
		RatingCustomerRequest ratingCustomer = this.analysisJsonData(url);
		// 开始评级申请
		this.startRating(ratingCustomer);
	}
	
	@Transactional
	public Response test() {
		Response resp = new Response();
		RatingCustomerRequest ratingCustomer = null;
		try {
			// 数据解析
			ratingCustomer = analysisJsonData("C:\\HkbCustomerRating/uploadCustomerEvaluateApplyData_20190418183232.txt");
			// 开始评级申请
			startRating(ratingCustomer);
		} catch (Exception e) {
			log.error(e);
			ret_status = EsbConstant.STATUS_ERROR;
			ret_code = EsbConstant.CODE_ERROR;
			ret_msg = StringUtils.isEmpty(e.getMessage()) ? "接口异常" : e.getMessage();
			throw new RuntimeException(StringUtils.isEmpty(e.getMessage()) ? "接口异常" : e.getMessage());
		}finally {
			resp.setRet_code(ret_code);
			resp.setRet_status(ret_status);
			resp.setRet_msg(ret_msg);
		}
		return resp;
	}

	*//**
	 * 开始评级申请
	 * @return
	 *//*
	@Transactional
	private void startRating(RatingCustomerRequest ratingCustomer) throws Exception {
		// 客户数据必输项非空判断
		this.checkCustomer(ratingCustomer);
		// 模型选择路径参数
		String modelCode = this.getModelCode(ratingCustomer);
		if (StringUtils.isEmpty(modelCode)) throw new Exception("未匹配到适用模型，请确认客户信息后发起评级申请");
		// 获取筛选后的财报
		List<RatingFinancialReportRequest> financialReports = this.getFinanReports(ratingCustomer, modelCode);
		// 获取模型评级配置
		RatingConfig ratingConfig = ratingConfigService.findByModelCode(modelCode);
		if(ratingConfig == null) throw new Exception("无法获取模型编号为:"+modelCode+"的有效评级配置信息");
		if(CollectionUtils.isEmpty(ratingConfig.getSteps())) throw new Exception("无法获取模型步骤配置信息");
		// 获取模型
		Model model = companyRatingServiceImpl.getModel(modelCode);
		if(model == null) throw new Exception("无法获取模型");
		// 保存客户信息
		RatingCompanyCustomer cust = this.saveCustomer(ratingCustomer,model.getId());
		// 保存财报、科目信息
		this.saveRatingFinancialReport(financialReports,cust);
		// 降序
		if(!CollectionUtils.isEmpty(financialReports)) {
			financialReports.sort((RatingFinancialReportRequest r1, RatingFinancialReportRequest r2) -> r2.getYear()
					.compareTo(r1.getYear()));
		}
		// 获取最大财报时间
		String currentMaxDate = "";
		if(!CollectionUtils.isEmpty(financialReports)) {
			currentMaxDate =  financialReports.get(0).getYear();
		}
		// 评级客户信息
		CompanyRating companyRating = this.saveCustRating(cust, modelCode,currentMaxDate, ratingConfig,model);
		// 开启审批流信贷模型评级流程
		String deployedKey = processEntityService.getEngineDeployedKey("loan_model_rating");
		Map<String,Object> variables = new HashMap<String, Object>();//持久化变量
		Map<String,Object> transientVariables = new HashMap<String, Object>();//临时变量
		variables.put("assignee", cust.getManagerCode());
		ProcessInstance instance= super.startProcessByKey(deployedKey, companyRating.getId(), variables, transientVariables, false);
		if(StringUtils.isNotEmpty(instance.getId())){
			companyRating.setTaskId(companyRatingServiceImpl.getTaskId(companyRating.getId()));
			companyRatingService.getRepository().save(companyRating);
		}
	}
	
	
	
	*//**
	 * 财报处理
	 * @param ratingCustomer
	 * @param modelCode
	 * @return
	 * @throws Exception
	 *//*
	private List<RatingFinancialReportRequest> getFinanReports(RatingCustomerRequest ratingCustomer, String modelCode)
			throws Exception{
		List<RatingFinancialReportRequest> finlReports = ratingCustomer.getFinancialReportDatas();
		if(!CollectionUtils.isEmpty(finlReports)) {
			int year = DateUtil.currentYear(); 
			// 去年/前年/大前年/大大前年
			int y1 = year -1,y2 = year -2,y3 = year -3,y4 = year -4;
			List<RatingFinancialReportRequest> y1List = finlReports.stream()
					.filter(fr -> String.valueOf(y1).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			List<RatingFinancialReportRequest> y2List = finlReports.stream()
					.filter(fr -> String.valueOf(y2).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			List<RatingFinancialReportRequest> y3List = finlReports.stream()
					.filter(fr -> String.valueOf(y3).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			List<RatingFinancialReportRequest> y4List = finlReports.stream()
					.filter(fr -> String.valueOf(y4).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			
			// 降序
			finlReports.sort((RatingFinancialReportRequest r1, RatingFinancialReportRequest r2) -> r2.getYear()
					.compareTo(r1.getYear()));
			// 财报科目必输项非空判断
			this.checkFinancialReport(ratingCustomer);
			List<RatingFinancialReportRequest> financialReports = null;
			// 微型企业模型
			if(ModelConstant.MODEL_WXQY.equals(modelCode)) {
				financialReports = this.getWXQYModelFinReport(y1List,y2List,y3List,y4List);
			// 新建企业模型
			}else if(ModelConstant.MODEL_XJQY.equals(modelCode)) {
				financialReports = this.getXJYQFinReport(y1List,y2List,y3List,y4List,finlReports); 
			// 其他模型
			}else {
				financialReports = this.getOtherModelFinReport(y1List,y2List,finlReports);
				if(CollectionUtils.isEmpty(financialReports) || financialReports.size() < 2) {
//					throw new Exception("缺失最新相同口径2年财报，请确认客户财报信息后发起评级申请");
					return null;
				}
			}
			// 转换科目编号
			this.conversionSubjectCode(financialReports);
			// 校验财报科目、勾稽关系校验
			String subjectInfo = this.checkSubject(financialReports);
			if(StringUtils.isNotEmpty(subjectInfo)) throw new Exception(subjectInfo);
			return financialReports;
		// 没有财报的场景
		}else {
			if(ModelConstant.MODEL_WXQY.equals(modelCode) || ModelConstant.MODEL_XJQY.equals(modelCode)) {
				return null;
			}else {
				throw new Exception("缺失最新相同口径2年财报，请确认客户财报信息后发起评级申请");
			}
		}
	}
	
	
	*//**
	 * 新建企业财报处理
	 * @param financialReportList
	 * @return
	 *//*
	private List<RatingFinancialReportRequest> getXJYQFinReport(
			List<RatingFinancialReportRequest> y1,
			List<RatingFinancialReportRequest> y2,
			List<RatingFinancialReportRequest> y3,
			List<RatingFinancialReportRequest> y4,
			List<RatingFinancialReportRequest> financialReportList) throws Exception{
		// 筛选有几期财报
		List<RatingFinancialReportRequest> finReportDates = financialReportList.stream()
				.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>
				(Comparator.comparing(fin -> fin.getYear()))), ArrayList::new));
		List<RatingFinancialReportRequest> reportRequests= null;
		// 走原逻辑按2期的场景
		if(finReportDates.size() > 1) {
			reportRequests = this.getOtherModelFinReport(y1,y2,financialReportList);
			if(CollectionUtils.isEmpty(reportRequests)) {
				throw new Exception("当前模型为新建企业，多期财报，2期财报不连续，请确认客户财报信息后发起评级申请");
			}
		}else {
			// 确保唯一的一期的财报是近2年的
			int year = DateUtil.currentYear(),year1 = year - 1,year2 =year - 2;
			finReportDates = finReportDates.stream().filter(fin -> 
				String.valueOf(year1).equals(fin.getYear().substring(0,4))
				|| String.valueOf(year2).equals(fin.getYear().substring(0,4)))
					.collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(finReportDates)) {
				// 这里认为只有一期的场景来做
				reportRequests = this.getXJQYOneYearFinReport(finReportDates);
			}
		}
		return reportRequests;
	}
	
	
	*//**
	 * 新建企业只有一期财报的处理
	 * @param financialReportList
	 * @return
	 * @throws Exception
	 *//*
	private List<RatingFinancialReportRequest> getXJQYOneYearFinReport(
			List<RatingFinancialReportRequest> financialReportList) throws Exception{
		List<RatingFinancialReportRequest> financialReports = null;
		CompanyCustomer companyCustomer = companyCustomerService.findById(financialReportList.get(0).getCompanyCustomer());
		// 如果内评系统无客户信息，或者客户信息集团母公司不为1，则为非集团客户
		if (companyCustomer == null || !"1".equals(companyCustomer.getIsGroupParentCompany())) {
			// 本部
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_BB);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
			// 汇总
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_HZ);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
			// 合并
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_HB);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
		} else {
			// 集团客户
			// 合并
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_HB);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
			// 汇总
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_HZ);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
			// 本部
			financialReports = this.getOneYearFinReport(financialReportList,
					FinancialReportConstant.REPORT_SCOPE_BB);
			if (!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
		}
		return null;
	}
	
	
	private List<RatingFinancialReportRequest> getOneYearFinReport(
			List<RatingFinancialReportRequest> financialReportList, String ReportSpecification) throws Exception {
		// 相同口径
		List<RatingFinancialReportRequest> financialReports = financialReportList.stream()
				.filter(financialReport -> ReportSpecification.equals(financialReport.getReportSpecifications()))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(financialReports)) {
			return null;
		}
		List<RatingFinancialReportRequest> list = new ArrayList<RatingFinancialReportRequest>();
		list.add(financialReports.get(0));
		return list;
	}
	
	
	
	*//**
	 * 获取模型CODE
	 * @param ratingCustomer
	 * @return
	 * @throws Exception
	 *//*
	private String getModelCode(RatingCustomerRequest ratingCustomer) throws Exception {
		// 企业成立时间 小于2年都认为是新建企业
		String establishmentTime = ratingCustomer.getEstablishmentTime();
//		log.info("企业成立时间："+establishmentTime);
		String currentDate = DateUtil.currentDate();
//		log.info("当前时间："+currentDate);
		int days = DateUtil.compareDaysApart(currentDate, establishmentTime); 
		ratingCustomer.setNewEnterpriseMark("1");
		// 大于则表示不是新建企业
		if(days > 730) {
			ratingCustomer.setNewEnterpriseMark("0");
		}
		// 满足则认为是政府融资平台
		ratingCustomer.setGovPlatform("0");
		if("011001".equals(ratingCustomer.getIntraIndustry()) 
				 || "011002".equals(ratingCustomer.getIntraIndustry())) {
			ratingCustomer.setGovPlatform("1");
		}
		
		Map<String, Object> modelParam = new HashMap<String, Object>();
		modelParam.put("ctmType", ratingCustomer.getCtmType());
		modelParam.put("gbIndustry", ratingCustomer.getGbIndustry());
		modelParam.put("enterpriseScale", StringUtils.isEmpty(ratingCustomer.getEnterpriseScale()) ? 0 :ratingCustomer.getEnterpriseScale());
		modelParam.put("totalAssets", !StringUtils.isEmpty(ratingCustomer.getTotalAssets()) ? new BigDecimal(ratingCustomer.getTotalAssets()) : BigDecimal.ZERO );
		modelParam.put("annualIncome", !StringUtils.isEmpty(ratingCustomer.getAnnualIncome()) ? new BigDecimal(ratingCustomer.getAnnualIncome()) : BigDecimal.ZERO );
		modelParam.put("govPlatform", ratingCustomer.getGovPlatform());
		modelParam.put("newEnterpriseMark", ratingCustomer.getNewEnterpriseMark());
		modelParam.put("isGuaranteeCorporation", ratingCustomer.getIsGuaranteeCorporation());
		String modelCode = modelSelectorConfigService.getModelCode(modelParam);
		log.info("Rating-modelCode:"+modelCode);
		return modelCode;
	}
	

	*//**
	 * 转换科目编号
	 * @param financialReports
	 *//*
	private void conversionSubjectCode(List<RatingFinancialReportRequest> financialReports) throws Exception{
		if(CollectionUtils.isEmpty(financialReports)) return;
		// 科目
		List<RatingFinancialSubjectRequest> subjects = null;
		RatingFinancialSubjectRequest financialSubjectRequest = null;
		// 需要转换的code
		List<String> codes = new ArrayList<String>();
		String sql = "select fd_fa_code\r\n" + 
				"   from NS_RT_FINAN_ACCOUNT_MAPP a\r\n" + 
				"   where a.fd_stat_template_fd_id =\r\n" + 
				"       (select fd_id\r\n" + 
				"          from NS_RT_FINAN_STAT_TEMPLATE n\r\n" + 
				"         where fd_fs_type =?)\r\n" + 
				"   and fd_fa_formercode = ?\r\n" + 
				"   and fd_fa_category = ?";
		for (RatingFinancialReportRequest rfr : financialReports) {
			subjects = new ArrayList<RatingFinancialSubjectRequest>();
			for (RatingFinancialSubjectRequest rfs : rfr.getfinancialSubjectDatas()) {
				financialSubjectRequest = new RatingFinancialSubjectRequest();
				financialSubjectRequest.setAccountCategory(rfs.getAccountCategory());
				financialSubjectRequest.setBeginValue(rfs.getBeginValue());
				financialSubjectRequest.setLineCode(rfs.getLineCode());
				financialSubjectRequest.setEndValue(rfs.getEndValue());
				financialSubjectRequest.setDataitemCode(rfs.getDataitemCode());
				financialSubjectRequest.setDataitemName(rfs.getDataitemName());
				codes = jdbcTemplate.queryForList(sql,String.class,rfr.getReportType(),rfs.getLineCode(),rfs.getAccountCategory());
				if(!CollectionUtils.isEmpty(codes)) {
					financialSubjectRequest.setDataitemCode(codes.get(0));
					subjects.add(financialSubjectRequest);
				}
			}
			rfr.setfinancialSubjectDatas(subjects);
		}
		
		
		// 补充缺失财报科目
	
		sql = "select FD_ID,FD_FA_CATEGORY,FD_FA_CODE,FD_FA_NAME\r\n" + 
				"   from NS_RT_FINAN_ACCOUNT_MAPP a\r\n" + 
				"   where a.fd_stat_template_fd_id =\r\n" + 
				"       (select fd_id\r\n" + 
				"          from NS_RT_FINAN_STAT_TEMPLATE n\r\n" + 
				"         where fd_fs_type =?) ";
		
		for (RatingFinancialReportRequest report : financialReports) {
			List<Map<String, Object>> mapp =jdbcTemplate.queryForList(sql,report.getReportType());
			List<RatingFinancialSubjectRequest> rsMy = new ArrayList<RatingFinancialSubjectRequest>();			
			List<RatingFinancialSubjectRequest> rsXd = new ArrayList<RatingFinancialSubjectRequest>(report.getfinancialSubjectDatas());
			for (Map<String, Object> m : mapp) {
				RatingFinancialSubjectRequest sub=new RatingFinancialSubjectRequest();
				String myCode=(String)m.get("FD_FA_CODE");
				sub.setAccountCategory((String)m.get("FD_FA_CATEGORY"));
				sub.setDataitemCode(myCode);
				sub.setDataitemName((String)m.get("FD_FA_NAME"));
				sub.setEndValue("0");
				sub.setBeginValue("0");
				rsMy.add(sub);				
				
				for(RatingFinancialSubjectRequest xdSu: rsXd ) {
					if(myCode.equals(xdSu.getDataitemCode())) {
						sub.setEndValue(xdSu.getEndValue());
						sub.setBeginValue(xdSu.getBeginValue());
						break;
					}
				}
			}
			report.setfinancialSubjectDatas(rsMy);
		}
	}
	
	*//**
	 * 客户数据必输项非空判断
	 * @param ratingCustomer
	 * @throws Exception
	 *//*
	private void checkCustomer(RatingCustomerRequest ratingCustomer)  throws Exception {
		String info = "";
		if(ratingCustomer == null) throw new Exception("当前客户信息缺失，请确认客户信息后发起评级申请"); 
		if(StringUtils.isEmpty(ratingCustomer.getCustNo()))					info += "“信贷客户号”、";
		if(StringUtils.isEmpty(ratingCustomer.getCtmType())) 				info += "“客户类型”、";
		if(StringUtils.isEmpty(ratingCustomer.getGbIndustry()))				info += "“国标行业”、";
//		if(StringUtils.isEmpty(ratingCustomer.getEnterpriseScale()))		info += "“企业规模”、";
//		if(StringUtils.isEmpty(ratingCustomer.getTotalAssets()))			info += "“资产总额”、";
//		if(StringUtils.isEmpty(ratingCustomer.getAnnualIncome()))			info += "“营业收入”、";
//		if(StringUtils.isEmpty(ratingCustomer.getGovPlatform()))			info += "“是否政府性融资性平台”、";
		if(StringUtils.isEmpty(ratingCustomer.getIntraIndustry()))			info += "“行内行业分类”、";
//		if(StringUtils.isEmpty(ratingCustomer.getNewEnterpriseMark()))		info += "“新建企业标识”、";
		if(StringUtils.isEmpty(ratingCustomer.getIsGuaranteeCorporation()))	info += "“是否担保公司”、";
		if(StringUtils.isEmpty(ratingCustomer.getManagerCode()))			info += "“客户经理编号”、";
		if(StringUtils.isEmpty(ratingCustomer.getEstablishmentTime()))		info += "“成立时间”、";
		if(!StringUtils.isEmpty(info)) 
			throw new Exception("当前客户信息（"+info.substring(0,info.length()-1)+"）缺失，请确认客户信息后发起评级申请");
		
		
		// 是否存在在途评级流程
		List<CompanyRating> ratings = companyRatingService.findByCustNo(ratingCustomer.getCustNo());
		if(!CollectionUtils.isEmpty(ratings)) {
			long count = ratings.stream().filter(rat -> !(
					DictionaryConstant.RATING_PROCESS_STATUS_YWC_TY.equals(rat.getProcessStatus()) ||
					DictionaryConstant.RATING_PROCESS_STATUS_YWC_FJ.equals(rat.getProcessStatus()) ||
					DictionaryConstant.RATING_PROCESS_STATUS_YWC_ZZ.equals(rat.getProcessStatus()) ||
					DictionaryConstant.RATING_PROCESS_STATUS_YWC_CX.equals(rat.getProcessStatus())) 
					).count();
			if(count > 0) throw new Exception("当前客户"+ratingCustomer.getCustNo()+"已经存在在途评级流程，无法发起评级申请。");
		}
		
		// 是否存在违约认定流程
		String cognizanceSql = "select count(1) from ns_dc_customer where fd_cust_no =? and FD_VAILD ='1'";
		List<Integer> cognizances = jdbcTemplate.queryForList(cognizanceSql,Integer.class,ratingCustomer.getCustNo());
		if(!CollectionUtils.isEmpty(cognizances)) {
			if(cognizances.get(0) > 0) {
				throw new Exception("当前客户"+ratingCustomer.getCustNo()+",存在违约认定，无法发起评级申请");
			}
		}
	}
	
	*//**
	 * 财报科目必输项非空判断
	 * @param ratingCustomer
	 * @throws Exception
	 *//*
	private void checkFinancialReport(RatingCustomerRequest ratingCustomer)  throws Exception {
		if(!CollectionUtils.isEmpty(ratingCustomer.getFinancialReportDatas())) {
			String info = "";
			for (RatingFinancialReportRequest fr : ratingCustomer.getFinancialReportDatas()) {
				if(StringUtils.isEmpty(fr.getCompanyCustomer()))			info += "“信贷客户号”、";
				if(StringUtils.isEmpty(fr.getYear()))						info += "“财报时间”、";
				if(StringUtils.isEmpty(fr.getReportBussDate()))				info += "“财报期数”、";
				if(StringUtils.isEmpty(fr.getReportSpecifications()))		info += "“财报口径”、";
				if(StringUtils.isEmpty(fr.getReportCurrency()))				info += "“财报币种”、";
				if(StringUtils.isEmpty(fr.getVaild()))						info += "“是否有效”、";
				if(StringUtils.isEmpty(fr.getIsAudit()))					info += "“是否审计”、";
				if(StringUtils.isEmpty(fr.getReportCycle()))				info += "“财报周期”、";
				if(StringUtils.isEmpty(fr.getReportType()))					info += "“财报类型”、";
				if(StringUtils.isEmpty(fr.getIsNullTable()))				info += "“是否空表”、";
				if(StringUtils.isEmpty(fr.getIsNullBalance()))				info += "“是否平衡”、";
				if(StringUtils.isEmpty(fr.getCheckPassRate()))				info += "“勾稽校验通过率”、";
				if(StringUtils.isEmpty(fr.getReportState()))				info += "“报表状态”、";
				//if(CollectionUtils.isEmpty(fr.getfinancialSubjectDatas()))	info += "“财报下的客户科目信息”、";
				if(!StringUtils.isEmpty(info)) 
					throw new Exception("当前客户财报信息（财报时间:"+ Optional.ofNullable(fr.getYear()).orElse("") 
											+ "--"+ info.substring(0,info.length()-1)+"）缺失，请确认客户财报信息后发起评级申请");
				
				info = "";
				for (RatingFinancialSubjectRequest fad : fr.getfinancialSubjectDatas()) {
					if(StringUtils.isEmpty(fad.getAccountCategory()))		info += "“科目分类”、";
					if(StringUtils.isEmpty(fad.getDataitemCode()))			info += "“数据项代码”、";
					if(StringUtils.isEmpty(fad.getDataitemName()))			info += "“数据项名称”、";
//					if(StringUtils.isEmpty(fad.getBeginValue()))			info += "“期初值”、";
					if(StringUtils.isEmpty(fad.getEndValue()))				info += "“期末值”、";
					if(!StringUtils.isEmpty(info)) 
						throw new Exception("财报时间:"+ Optional.ofNullable(fr.getYear()).orElse("")+"的科目信息" +info.substring(0,info.length()-1) + "缺失，请确认科目信息后发起评级申请  ");
				}
			}
		}
	}
	
	// 保存评级信息
	public CompanyRating saveCustRating(RatingCompanyCustomer cust,String modelCode,String currentMaxDate,RatingConfig ratingConfig,Model model) throws Exception{
		String modelName = "";
		Map<String,String> modelMap = systemDictionaryService.getDictionaryMap("MODEL_TYPE",new Locale("zh", "CN"));
		if(modelMap != null ) {
			modelName = modelMap.get(modelCode);
		}
		CompanyRating companyRating = new CompanyRating();
		companyRating.setRatingCustomer(cust);
		companyRating.setRatingType(RatingConstants.RATING_TYPE_YEAR);
		companyRating.setCustNo(cust.getCustNo());
		companyRating.setCustName(cust.getCustName());
		companyRating.setModelId(cust.getModelId());
		companyRating.setModelCode(modelCode);
		companyRating.setRatingConfig(ratingConfig);
		companyRating.setAccountManager(cust.getManagerName());
		companyRating.setAccountManagerCode(cust.getManagerCode());
		companyRating.setLaunchUser(cust.getManagerName());
		companyRating.setProcessStatus(DictionaryConstant.RATING_PROCESS_STATUS_DTJ);
		companyRating.setRatingStatus(DictionaryConstant.RATING_STATUS_NOT_ACTIVE);
		companyRating.setCurrentAssignee(cust.getManagerCode());
		companyRating.setModelName(modelName);
		Calendar completDate = Calendar.getInstance();
		companyRating.setStartTime(completDate.getTime());
		companyRating.setVaild(true);
		companyRating.setSource("IRB");
		companyRating.setFinancialReportDate(currentMaxDate);
		companyRating = companyRatingService.add(companyRating);
		companyRating.setCurrentRole(RoleConstant.CUSTOMER_MANAGER);
		// 初始化评级步骤、补录项、调整项、指标
		companyRatingService.saveRatingStepAndIndex(companyRating,model);
		return companyRating;
	}
	
	*//**
	 * 微型企业模型财报处理
	 * @param financialReportList
	 * @return
	 *//*
	private List<RatingFinancialReportRequest> getWXQYModelFinReport(
			List<RatingFinancialReportRequest> y1,
			List<RatingFinancialReportRequest> y2,
			List<RatingFinancialReportRequest> y3,
			List<RatingFinancialReportRequest> y4) throws Exception{
		List<RatingFinancialReportRequest> reports = new ArrayList<RatingFinancialReportRequest>();
		if(CollectionUtils.isEmpty(y1)) {
			if(CollectionUtils.isEmpty(y2)) {
				return null;
			// 前年不为空的场景
			}else {
				final String reportSpecifications = y2.get(0).getReportSpecifications();
				// 前年财报有数据
				// 第一年财报
				if(!CollectionUtils.isEmpty(y2)) {
					reports.add(y2.get(0));
				}
				// 第二年如果有财报，则需要保持口径一致
				if(!CollectionUtils.isEmpty(y3)) {
					y3 = y3.stream().filter(fin -> reportSpecifications.equals(fin.getReportSpecifications()))
							.collect(Collectors.toList());
					if(CollectionUtils.isEmpty(y3)) {
						return null;
					}
					reports.add(y3.get(0));
				}
				// 第三年如果有财报
				if(!CollectionUtils.isEmpty(y4)) {
					y4 = y4.stream().filter(fin -> reportSpecifications.equals(fin.getReportSpecifications()))
							.collect(Collectors.toList());
					if(CollectionUtils.isEmpty(y4)) {
						return null;
					}
					reports.add(y4.get(0));
				}
				return reports;
			}
		// 去年财报不为空的场景
		}else {
			final String reportSpecifications = y1.get(0).getReportSpecifications();
			// 如果去年财报有数据
			// 第一年财报
			if(!CollectionUtils.isEmpty(y1)) {
				reports.add(y1.get(0));
			}
			// 第二年如果有财报，则需要保持口径一致
			if(!CollectionUtils.isEmpty(y2)) {
				y2 = y2.stream().filter(fin -> reportSpecifications.equals(fin.getReportSpecifications()))
						.collect(Collectors.toList());
				if(CollectionUtils.isEmpty(y2)) {
					return null;
				}
				reports.add(y2.get(0));
			}
			// 第三年如果有财报
			if(!CollectionUtils.isEmpty(y3)) {
				y3 = y3.stream().filter(fin -> reportSpecifications.equals(fin.getReportSpecifications()))
						.collect(Collectors.toList());
				if(CollectionUtils.isEmpty(y3)) {
					return null;
				}
				reports.add(y3.get(0));
			}
			return reports;
		}
	}
	
	*//**
	 * 其他模型财报处理
	 * @param y1
	 * @param y2
	 * @param financialReportList
	 * @return
	 * @throws Exception
	 *//*
	private List<RatingFinancialReportRequest> getOtherModelFinReport(
			List<RatingFinancialReportRequest> y1,
			List<RatingFinancialReportRequest> y2,
			List<RatingFinancialReportRequest> financialReportList) throws Exception{
		if(CollectionUtils.isEmpty(y1) && CollectionUtils.isEmpty(y2)) {
			log.info("接口校验信息：去年和前年均无财报！！！");
			throw new Exception("缺失最新相同口径2年财报，请确认客户财报信息后发起评级申请");
		}
		// 查询最新一期是否有多个口径,获取最大财报时间
		String currentMaxDate =  financialReportList.get(0).getYear();
		// 最新一期是否有多个财报且不同口径集合
		List<RatingFinancialReportRequest> maxDateFins = financialReportList.stream().filter(fin -> currentMaxDate.equals(fin.getYear()))
				.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(fin -> fin.getReportSpecifications()))), ArrayList::new));
		List<RatingFinancialReportRequest> financialReports = null;
		// 最新一期有多个则按照优先级获取财报
		if(maxDateFins.size() > 1) {
			CompanyCustomer companyCustomer = companyCustomerService.findById(maxDateFins.get(0).getCompanyCustomer());
			// 如果内评系统无客户信息，或者客户信息集团母公司不为1，则为非集团客户
			if(companyCustomer == null || !"1".equals(companyCustomer.getIsGroupParentCompany())) {
				// 本部
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_BB);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
				// 汇总
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_HZ);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
				// 合并
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_HB);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
			}else {
				// 集团客户
				// 合并
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_HB);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
				// 汇总
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_HZ);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
				// 本部
				financialReports = this.checkReportSpecification(financialReportList, FinancialReportConstant.REPORT_SCOPE_BB);
				if(!CollectionUtils.isEmpty(financialReports)) {
					return financialReports;
				}
			}
		}else {
			// 最新一期没有多条口径的场景
			financialReports = this.checkReportSpecification(financialReportList, financialReportList.get(0).getReportSpecifications());
			if(!CollectionUtils.isEmpty(financialReports)) {
				return financialReports;
			}
		}
		return null;
	}
	

	*//**
	 * 相同口径财报
	 * @param financialReportList
	 * @param ReportSpecification
	 * @return
	 * @throws Exception
	 *//*
	private List<RatingFinancialReportRequest> checkReportSpecification(
			List<RatingFinancialReportRequest> financialReportList,
			String ReportSpecification) throws Exception{
			// 相同口径
			List<RatingFinancialReportRequest> financialReports = financialReportList.stream()
					.filter(financialReport -> ReportSpecification
					.equals(financialReport.getReportSpecifications())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(financialReports)) {
				return null;
			}
			List<RatingFinancialReportRequest> list = null;
			int year = DateUtil.currentYear();
			int month = DateUtil.currentMonth(); 
			// 去年/前年/大前年/大大前年
			int y1 = year -1,y2 = year -2,y3 = year -3,y4 = year -4;
			// 开始判断财报口径年份
			List<RatingFinancialReportRequest> y1s = financialReports.stream()
					.filter(fr -> String.valueOf(y1).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			List<RatingFinancialReportRequest> y2s = financialReports.stream()
					.filter(fr -> String.valueOf(y2).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
			List<RatingFinancialReportRequest> y3s = financialReports.stream()
					.filter(fr -> String.valueOf(y3).equals(fr.getYear().substring(0,4)))
					.collect(Collectors.toList());
//			List<RatingFinancialReportRequest> y4s = financialReports.stream()
//					.filter(fr -> String.valueOf(y4).equals(fr.getYear().substring(0,4)))
//					.collect(Collectors.toList());
			// 当前时间如果处于上半年
			if(month <= 6) {
				// 判断去年和前年财报是否存在
				if(!CollectionUtils.isEmpty(y1s) && !CollectionUtils.isEmpty(y2s)) {
					list = this.assemblyList(y3,y1s, y2s,financialReportList);
					if(!CollectionUtils.isEmpty(list)) {
						return list;
					}
				}
				// 判断前年和大前年财报是否存在
				if(!CollectionUtils.isEmpty(y2s) && !CollectionUtils.isEmpty(y3s)) {
					list = this.assemblyList(y4,y2s, y3s,financialReportList);
					if(!CollectionUtils.isEmpty(list)) {
						return list;
					}
				}
			}else {
				// 当前时间为下半年，则必须拥有去年和前年的财务信息
				if(!CollectionUtils.isEmpty(y1s) && !CollectionUtils.isEmpty(y2s)) {
					list = this.assemblyList(y3,y1s, y2s,financialReportList);
					if(!CollectionUtils.isEmpty(list)) {
						return list;
					}
				}
			}
		return null;
	}

	
	*//**
	 * 
	 * @param threeYear 第三年
	 * @param r1 第一年财报
	 * @param r2 第二年财报
	 * @param reportRequests 所有财报
	 * @return
	 * @throws Exception
	 *//*
	private List<RatingFinancialReportRequest> assemblyList(
			int threeYear,
			List<RatingFinancialReportRequest> r1, 
			List<RatingFinancialReportRequest> r2,
			List<RatingFinancialReportRequest> reportRequests) throws Exception{
		List<RatingFinancialReportRequest> list = new ArrayList<RatingFinancialReportRequest>();
		list.add(r1.get(0));
		list.add(r2.get(0));
		// 先看第三年有没有财报
		reportRequests = reportRequests.stream().filter(fin -> 
			String.valueOf(threeYear).equals(fin.getYear().substring(0,4)))
			.collect(Collectors.toList());
		// 如果有第三年财报，则看有没有匹配相同口径的，有则返回，没有则表示不满足
		if(!CollectionUtils.isEmpty(reportRequests)) {
			reportRequests = reportRequests.stream().filter(fin -> 
				r1.get(0).getReportSpecifications().equals(fin.getReportSpecifications()))
			.collect(Collectors.toList());
			if(CollectionUtils.isEmpty(reportRequests)) {
				return null;
			}
			list.add(reportRequests.get(0));
		}
		return list;
	}
	
	
	*//**
	 * 校验财报科目
	 * @return
	 *//*
	private String checkSubject(List<RatingFinancialReportRequest> financialReports) throws Exception{
		if(CollectionUtils.isEmpty(financialReports)) return null;
		// 科目列表
		Map<String,Object> parame = new HashMap<String, Object>();
		// 科目校验结果
//		Map<String,Map<String,String>> subjectMap = new HashMap<String, Map<String,String>>();
		List<String> list = new ArrayList<String>();
		// 所有科目校验结果
		List<List<String>> reportLists = new ArrayList<List<String>>();
		for (RatingFinancialReportRequest fr : financialReports) {
			parame = new HashMap<String, Object>();
			for (RatingFinancialSubjectRequest fs : fr.getfinancialSubjectDatas()) {
				String key = fs.getDataitemCode();
				BigDecimal value = StringUtil.isEmpty(fs.getEndValue()) ? BigDecimal.ZERO : new BigDecimal(fs.getEndValue());
				parame.put(key, value);
			}
			log.info("勾稽关系参数");
			log.info("goujimap："+parame);
			list = checkedRelationshipService.CheckedRelationship(parame, fr.getReportType(),fr.getYear());
			if(!CollectionUtils.isEmpty(list)) {
				reportLists.add(list);
			}
		}
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(reportLists)) {
			for (List<String> ls : reportLists) {
				if(!CollectionUtils.isEmpty(ls)) {
					for (String l : ls) {
						sb.append(l);
						sb.append("\n");
					}
				}
			}
// 			for (Map<String, Map<String, String>> item : reportLists) {
// 				for (Map.Entry<String,Map<String,String>> m : item.entrySet()) {
//					if("false".equals(m.getValue().get("flag"))) {
//						sb.append(m.getValue().get("msg"));
//						sb.append("\n");
//					}
//				}
//			}
		}
		return sb.toString();
	}
	
	*//**
	 * 解析JSON
	 * @param fileUrl
	 * @return
	 * @throws Exception
	 *//*
	private RatingCustomerRequest analysisJsonData(String fileUrl) throws Exception {
		String jsonStr = FileUtils.readToString(fileUrl);
		if(StringUtils.isNotEmpty(jsonStr)) {
			RatingCustomerRequest ratingCustomer = JsonUtil.parseBean(jsonStr, RatingCustomerRequest.class);
			return ratingCustomer;
		}
		return null;
	}
	
	*//**
	 * 保存客户信息
	 *//*
	private RatingCompanyCustomer saveCustomer(RatingCustomerRequest ratingCustomer,String modelId) throws Exception {
		
		// 机构ID
		String ordId = "",updateOrgId= "";
//		String orgSql = " select org_no from m_t_pd_dim_bkorg_map where mis_bankid =? ";
//		List<String> orgIds = null;
//		if(!StringUtils.isEmpty(ratingCustomer.getOrgId())) {
//			orgIds = jdbcTemplate.queryForList(orgSql,String.class,ratingCustomer.getOrgId());
//			if(!CollectionUtils.isEmpty(orgIds)) {
//				ordId = orgIds.get(0);
//			}
//		}
//		
//		// 更新机构
//		if(!StringUtils.isEmpty(ratingCustomer.getUpdateOrgid())) {
//			orgIds = jdbcTemplate.queryForList(orgSql,String.class,ratingCustomer.getUpdateOrgid());
//			if(!CollectionUtils.isEmpty(orgIds)) {
//				updateOrgId = orgIds.get(0);
//			}
//		}
		
		RatingCompanyCustomer customer = new RatingCompanyCustomer();
		customer.setCustName(ratingCustomer.getCustName());
		customer.setCustNo(ratingCustomer.getCustNo());
		customer.setRegAmount(StringUtils.isEmpty(ratingCustomer.getRegAmount()) ? 0.0 : Double.parseDouble(ratingCustomer.getRegAmount()));
		customer.setRegAddress(ratingCustomer.getRegAddress());
		customer.setGbIndustry(ratingCustomer.getGbIndustry());
		customer.setIntraIndustry(ratingCustomer.getIntraIndustry());
		customer.setBusinessLicense(ratingCustomer.getBusinessLicense());
		customer.setEmpCount(StringUtils.isEmpty(ratingCustomer.getEmpCount()) ? 0 : Integer.parseInt(ratingCustomer.getEmpCount()));
		customer.setLegalRep(ratingCustomer.getLegalRep());
		customer.setManagerName(ratingCustomer.getManagerName());
		customer.setManagerCode(ratingCustomer.getManagerCode());
		customer.setOffcAddress(ratingCustomer.getOffcAddress());
		customer.setOffcAddrZipCode(ratingCustomer.getOffcAddrZipCode());
		customer.setCrdtAmt(StringUtils.isEmpty(ratingCustomer.getCrdtAmt()) ? 0 : Double.parseDouble(ratingCustomer.getCrdtAmt()));
		customer.setEnterpriseScale(ratingCustomer.getEnterpriseScale());
		customer.setEstablishmentTime(ratingCustomer.getEstablishmentTime());
		customer.setNewEnterpriseMark(Optional.ofNullable(ratingCustomer.getNewEnterpriseMark()).orElse("0").equals("1") ?true : false);
		customer.setSourceType("IRB");
		customer.setCtmType(ratingCustomer.getCtmType());
		customer.setModelId(modelId);
		customer.setOrg(orgService.getRepository().findOne(ordId));
		customer.setCtmCoreNo(ratingCustomer.getCtmCoreNo());
		customer.setCertType(ratingCustomer.getCertType());
		customer.setCertidNo(ratingCustomer.getCertidNo());
		customer.setIsFinancingGuarantee("1".equals(ratingCustomer.getIsFinancingGuarantee()) ? "1" : null);
		customer.setGovPlatform(ratingCustomer.getGovPlatform());
		customer.setNewTechnology("1".equals(ratingCustomer.getNewTechnology()) ? "1" : null);
		customer.setFinancialStatementsType(ratingCustomer.getFinancialStatementsType());
		customer.setBankShareholders("1".equals(ratingCustomer.getBankShareholders()) ? "1" : null);
		customer.setCreditDate(ratingCustomer.getCreditDate());
		customer.setIsElseWhere("1".equals(ratingCustomer.getIsElseWhere()) ? "1" : null);
		customer.setInputDt(ratingCustomer.getInputDt());
		customer.setUpdateOrg(orgService.getRepository().findOne(updateOrgId));
		customer.setUpdateDt(ratingCustomer.getUpdateDt());
		customer.setIsAbnormalOperation("1".equals(ratingCustomer.getIsAbnormalOperation()) ? "1" : null);
		customer.setIsBlankBlackList("1".equals(ratingCustomer.getIsBlankBlackList()) ? "1" : null);
		customer.setLitigation("1".equals(ratingCustomer.getLitigation()) ? "1" : null);
		customer.setTechnology("1".equals(ratingCustomer.getTechnology()) ? "1" : null);
		customer.setTotalAssets(ratingCustomer.getTotalAssets());
		customer.setAnnualIncome(ratingCustomer.getAnnualIncome());
		customer.setEnterpriseName(ratingCustomer.getEnterpriseName());
		customer.setIsGuaranteeCorporation(ratingCustomer.getIsGuaranteeCorporation());
		customer.setBusScope(ratingCustomer.getBusScope());
		RatingCompanyCustomer rc = customerService.add(customer);
		return rc;
	}
	
	*//**
	 * 保存财报信息
	 * @param financialReports
	 * @param cust
	 * @return
	 * @throws Exception
	 *//*
	private void saveRatingFinancialReport(List<RatingFinancialReportRequest> financialReports,
			RatingCompanyCustomer cust) throws Exception{
		RatingFinancialStatements financialReport = null;
		RatingFinAccountData  financialSubject = null;
		if(!CollectionUtils.isEmpty(financialReports)) {
			for (RatingFinancialReportRequest fr : financialReports) {
				financialReport = new RatingFinancialStatements();
				financialReport.setRatingCompanyCustomer(cust.getId());
				financialReport.setReportBussDate(fr.getReportBussDate());
				financialReport.setReportSpecifications(fr.getReportSpecifications());
				financialReport.setReportCurrency(fr.getReportCurrency());
				financialReport.setVaild(Optional.ofNullable(fr.getVaild()).orElse("0").equals("1") ? true : false );
				financialReport.setIsAudit(Optional.ofNullable(fr.getIsAudit()).orElse("0").equals("1") ? true : false );
				financialReport.setReportCycle(fr.getReportCycle());
				financialReport.setReportSource("IRB");
				financialReport.setReportType(fr.getReportType());
				financialReport.setIsNullTable(fr.getIsNullTable());
				financialReport.setIsNullBalance(fr.getIsNullBalance());
				financialReport.setCheckPassRate(fr.getCheckPassRate());
				financialReport.setReportState(fr.getReportState());
				financialReport.setYear(fr.getYear());
				RatingFinancialStatements finReport = financialReportService.add(financialReport);
				if(!CollectionUtils.isEmpty(fr.getfinancialSubjectDatas())) {
					for (RatingFinancialSubjectRequest fs : fr.getfinancialSubjectDatas()) {
						financialSubject = new RatingFinAccountData();
						financialSubject.setFinancia(finReport);
						financialSubject.setAccountCategory(fs.getAccountCategory());
						financialSubject.setLineCode(StringUtils.isEmpty(fs.getLineCode()) ? null : Integer.parseInt(fs.getLineCode()));
						financialSubject.setDataitemCode(fs.getDataitemCode());
						financialSubject.setDataitemName(fs.getDataitemName());
						financialSubject.setBeginValue(Double.parseDouble(Optional.ofNullable(fs.getBeginValue()).orElse("0")));
						financialSubject.setEndValue(Double.parseDouble(Optional.ofNullable(fs.getEndValue()).orElse("0")));
						financialSubjectService.add(financialSubject);
					}
				}
			}
		}
		
	}
	
	
	
	*//**
	 * 文件下载
	 *//*
	private String fileDownload(String file_path) throws Exception {
		String fileName = file_path.substring(file_path.indexOf("CustomerEvaluateApplyData"));
		// 系统配置文件下载路径
		String url = DirectoryManager.getDirectoryByName("dir.work.web.upload");
		url = url+fileName;
		// 文件下载
		String config = DirectoryManager.getDirectoryByName("dir.config")+File.separator+"FtpClientConfig.properties";
		FtpClientConfig.loadConf(config);
		log.info("文件路径:"+file_path);
		FtpGet ftp = null;
		try {
			ftp = new FtpGet(file_path, url, false);
			ftp.doGetFile();
		} catch (FtpException e) {
			if (log.isErrorEnabled()) {
				log.error("文件下载出错", e);
				throw new Exception("文件下载出错");
			}
		} finally {
			if (ftp != null)
				ftp.close();
		}
		return url;
	}

}
*/