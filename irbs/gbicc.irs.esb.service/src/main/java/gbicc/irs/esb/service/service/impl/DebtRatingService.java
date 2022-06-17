/*package gbicc.irs.esb.service.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.batch.reader.po.CustomerInfo;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.ftp.client.FtpClientConfig;
import com.dcfs.esb.ftp.client.FtpGet;
import com.dcfs.esb.ftp.server.error.FtpException;

import gbicc.irs.customer.entity.CompanyCustomer;
import gbicc.irs.customer.service.CompanyCustomerService;
import gbicc.irs.customer.service.UserService;
import gbicc.irs.debtRating.debt.entity.AppDebtRating;
import gbicc.irs.debtRating.debt.entity.AppGuaranTee;
import gbicc.irs.debtRating.debt.entity.AppGuarantor;
import gbicc.irs.debtRating.debt.entity.AppProductDetail;
import gbicc.irs.debtRating.debt.service.AppDebtRatingService;
import gbicc.irs.debtRating.debt.service.AppGuaranTeeService;
import gbicc.irs.debtRating.debt.service.AppGuarantorService;
import gbicc.irs.debtRating.debt.service.AppProductDetailService;
import gbicc.irs.debtRating.util.FormulaCalculateEntity;
import gbicc.irs.debtRating.util.FormulaCalculateUtil;
import gbicc.irs.debtRating.util.FormulaCalculateUtilK;
import gbicc.irs.debtRating.util.FormulaCalculateUtilOther;
import gbicc.irs.debtRating.util.FormulaCalculateUtilR;
import gbicc.irs.debtRating.util.FormulaCalculateUtilRF1;
import gbicc.irs.debtRating.util.FormulaCalculateUtilRSME;
import gbicc.irs.esb.service.constant.EsbConstant;
import gbicc.irs.esb.service.response.Response;
import gbicc.irs.esb.service.support.FileUtils;
import gbicc.irs.esb.service.support.JsonUtil;

@Service("DebtRatingService")
public class DebtRatingService {
	private static Log log = LogFactory.getLog(DebtRatingService.class);
	@Autowired
	private AppDebtRatingService appDebtRatingService;
	@Autowired
	private SystemParameterServiceImpl systemParameterServiceImpl;
	@Autowired
	private AppGuaranTeeService appGuaranTeeService;
	@Autowired
	private AppGuarantorService appGuarantorService;
	@Autowired
	private AppProductDetailService appProductDetailService;
	@Autowired
	FormulaCalculateUtilOther utilOther;
	@Autowired
	FormulaCalculateUtilK formulaK;
	@Autowired
	FormulaCalculateUtilOther formulaOther;
	@Autowired
	private CompanyCustomerService companyCustomerser;
	
	
	@Autowired
	private UserService userSer;
	
	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	FormulaCalculateUtil formula;

	// 系统状态标识
	private String ret_status = EsbConstant.STATUS_SUCCESS;
	// 业务状态标识
	private String ret_code = EsbConstant.CODE_SUCCESS;
	// 调用结果描述
	private String ret_msg = EsbConstant.MSG_SUCCESS;

	@Transactional
	public CompositeData execute(CompositeData req) throws Exception {
		CompositeData body = new CompositeData();
		CompositeData file_head = req.getStruct("FILE_HEAD");
		String file_path = file_head.getField("FILE_PATH").strValue();
		if (StringUtils.isEmpty(file_path))
			throw new Exception("获取不到接口上传文件路径");
		// 文件下载
		String url = fileDownload(file_path);
		// 数据解析
		AppDebtRating appDebtRating = analysisJsonData(url);
		// 债项评级数据入库
		process(appDebtRating, body);

		return body;
	}

	@Transactional
	public Response test() {
		Response resp = new Response();
		AppDebtRating appDebtRating = null;
		try {
			// 数据解析
			appDebtRating = testAnalysisJsonData();
			formulaResult(appDebtRating);
			CompositeData body = new CompositeData();
			// 债项评级数据入库
			 process(appDebtRating,body);
			//删除重复数据
			jdbc.update("delete NS_APPLICATION_DEBT_RATING where FD_APPLICATION_NUMBER=?",appDebtRating.getApplicationNumber());
			jdbc.update("delete NS_APPLICATION_GUARANTEE where FD_DEBT_RATING_ID=?",appDebtRating.getApplicationNumber());
			jdbc.update("delete NS_APPLICATION_GUARANTOR where FD_DEBT_RATING_ID=?",appDebtRating.getApplicationNumber());
			jdbc.update("delete NS_APPLICATION_DEBT_RATING where FD_DEBT_RATING_ID=?",appDebtRating.getApplicationNumber());
			appDebtRatingService.add(appDebtRating);
		} catch (Exception e) {
			log.error(e);
			ret_status = EsbConstant.STATUS_ERROR;
			ret_code = EsbConstant.CODE_ERROR;
			ret_msg = StringUtils.isEmpty(e.getMessage()) ? "接口异常" : e.getMessage();
			throw new RuntimeException(StringUtils.isEmpty(e.getMessage()) ? "接口异常" : e.getMessage());
		} finally {
			resp.setRet_code(ret_code);
			resp.setRet_status(ret_status);
			resp.setRet_msg(ret_msg);
		}
		return resp;
	}

	public <T> T getData(String sql, Class<T> requiredType, Object... obj) {
		return (T) jdbc.queryForObject(sql, requiredType, obj);
	}

	*//**
	 * 债项评级数据入库
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@Transactional
	private void process(AppDebtRating appDebtRating, CompositeData body) throws Exception {
		if (null == appDebtRating) {
			log.warn("债项评级数据入库的数据为空：" + appDebtRating);
			return;
		}
		try {
			// 保存MIS接口传过来的数据
			// 以下是计算打分的业务逻辑
			Map<String, String> map = formulaResult(appDebtRating);
			// 组装返回报文
			// 申请编号
			FieldAttr applyNoAttr = new FieldAttr(FieldType.FIELD_STRING, 30, 0);
			Field applyNo = new Field(applyNoAttr);
			applyNo.setValue(map.get(EsbConstant.APPLY_NO));
			body.addField(EsbConstant.APPLY_NO, applyNo);
			// 违约损失率
			FieldAttr lgdRateAttr = new FieldAttr(FieldType.FIELD_STRING, 20, 0);
			Field lgdRate = new Field(lgdRateAttr);
			lgdRate.setValue(map.get(EsbConstant.LGD_RATE));
			body.addField(EsbConstant.LGD_RATE, lgdRate);
			// 违约等级
			FieldAttr lgdLevelAttr = new FieldAttr(FieldType.FIELD_STRING, 10, 0);
			Field lgdLevel = new Field(lgdLevelAttr);
			lgdLevel.setValue(map.get(EsbConstant.LGD_LEVEL));
			body.addField(EsbConstant.LGD_LEVEL, lgdLevel);
			// 计算日期
			FieldAttr computeDateAttr = new FieldAttr(FieldType.FIELD_STRING, 10, 0);
			Field computeDate = new Field(computeDateAttr);
			computeDate.setValue(map.get(EsbConstant.COMPUTE_DATE));
			body.addField(EsbConstant.COMPUTE_DATE, computeDate);
		} catch (Exception e) {
			throw e;
			
			 * e.printStackTrace();
			 * log.error("业务申请债项评级接口数据入库异常,请求参数："+appDebtRating.toString()+"异常信息："+e);
			 
		}
	}

	public Map<String, String> formulaResult(AppDebtRating appDebtRating) throws Exception {
		FormulaCalculateUtilOther.zzsl=Double.parseDouble(systemParameterServiceImpl.getParameter("rate_of_value-added_tax"));
		FormulaCalculateUtil.defaultLevel=systemParameterServiceImpl.getParameter("defaultLevel");
		FormulaCalculateUtilOther.targetRaroc=Double.parseDouble(systemParameterServiceImpl.getParameter("targetRaroc"));
		FormulaCalculateUtilOther.targetSyl = Double.parseDouble(systemParameterServiceImpl.getParameter("targetSyl"));
		FormulaCalculateUtil.appNumber=appDebtRating.getApplicationNumber();
		FormulaCalculateEntity entity = new FormulaCalculateEntity();
		
		Map<String, String> result = new HashMap<String, String>();
		String msg = "评级结果计算成功";
		boolean flag = true;
		result.put("flag", String.valueOf(flag));
		result.put("msg", msg);
		String custNo = appDebtRating.getCustomerNumber();
		String productCode = appDebtRating.getProductCode();

		// 押品信息
		List<AppProductDetail> productDetailList = appDebtRating.getAppProductDetailList();

		
		List<AppGuaranTee> appGuaranTee = appDebtRating.getAppGuaranTeeList();
		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓LGD↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		List<Double> ZYWHSBL = new ArrayList<Double>();// 抵质押物1回收比例

		Double amt = Double.parseDouble(appDebtRating.getContractAmount());
		Double ead = utilOther.getEad(appDebtRating.getBusinessTypes(), productCode,
				Integer.parseInt(appDebtRating.getTypeCredit()), Double.parseDouble(appDebtRating.getContractAmount()));
		List<Double> ZYWTZFGL = new ArrayList<Double>();// 调整覆盖率
		Double zywhsbl = 0.0;

		for (AppProductDetail product : productDetailList) {
			entity.setCODE(product.getProductNumber());// 产品编号

			
			
			Double businessPledge = Double.parseDouble(product.getBusinessPledge());// 权利价值
			
			//产品认定权利价值
			formula.insertProcessValue(product.getProductNumber(), "权利价值", businessPledge);
			//amtYp
			String contract = product.getContractNumber();//existingContractBalance
			Double amtYp=null; 
			Double amtBl = 1.0;
			String amtSq = appDebtRating.getContractAmount();//existingContractBalance
			for (int i = 0; i < appGuaranTee.size(); i++) {
				if(appGuaranTee.get(i).getContractNumber().equals(contract)) {
					if(appGuaranTee.get(i).getGuaranteeSubject().equals("保证金")) {
						amtSq=appGuaranTee.get(i).getGuaranteeAmount();
						if(StringUtils.isNotBlank(appGuaranTee.get(i).getExistingContractBalance())) {
							amtYp=Double.parseDouble(appGuaranTee.get(i).getExistingContractBalance());
							amtBl =  Double.parseDouble(amtSq)/(amtYp+Double.parseDouble(amtSq));
						}else {
							amtYp=0.0;
							amtBl=1.0;
						}
						
					}else {
						if(StringUtils.isNotBlank(appGuaranTee.get(i).getExistingContractBalance())) {
							amtYp=Double.parseDouble(appGuaranTee.get(i).getExistingContractBalance());
							amtBl =  Double.parseDouble(amtSq)/(amtYp+Double.parseDouble(amtSq));
						}else {
							amtYp=0.0;
							amtBl=1.0;
						}
						
					}
				}
			}
			
			
			
			//担保比例
			formula.insertProcessValue(product.getProductNumber(), "担保品所占比例", amtBl);
			
			businessPledge = amtBl * businessPledge;

			entity.setMainT(product.getGuarTypelCode());
			// FormulaCalculateUtil formula = new FormulaCalculateUtil();
			entity.setPGJG(businessPledge);// 评估价格==>不再取评估价值取权利价值
			//产品担保的合同价值
			formula.insertProcessValue(product.getProductNumber(), "担保合同金额", amtYp);
			//产品拆分后比例价值
			formula.insertProcessValue(product.getProductNumber(), "评估价值", businessPledge);
			Double zkl = null;
			Integer count = null;
			//如果中类不存在则返回接口信息
			try {
				count = getData("select count(*) from PT_PLEDGE_THURDLE_RATE where FD_COLLATERAL_CODE=?",
						Integer.class, product.getGuarTypelCode());
				if(count==0) {
					count=2/count;
				}
			} catch (Exception e) {
				throw new Exception(product.getGuarTypelCode()+"产品中类不存在，请核实数据！");
			}
			
			try {
				count = getData("select count(FD_DISCOUNT_INITIAL) from PT_PLEDGE_THURDLE_RATE where FD_COLLATERAL_CODE=? and FD_COLLATERAL_MAIN_CODE = ?",
						Integer.class, product.getGuarTypelCode(),appDebtRating.getPrincipalModeGuarantee());
			if(count>0) {
				zkl = getData("select FD_DISCOUNT_INITIAL from PT_PLEDGE_THURDLE_RATE where FD_COLLATERAL_CODE=? and FD_COLLATERAL_MAIN_CODE = ?",
						double.class, product.getGuarTypelCode(),appDebtRating.getPrincipalModeGuarantee());
			}else {
				zkl = getData("select FD_DISCOUNT_INITIAL from PT_PLEDGE_THURDLE_RATE where FD_COLLATERAL_CODE=? and FD_COLLATERAL_MAIN_CODE = '空'",
						double.class, product.getGuarTypelCode());
			}
			} catch (Exception e) {
				throw new Exception("折扣率取值异常！");
			}
				
			formula.insertProcessValue(product.getProductNumber(), "折扣率", zkl);
			
			
			entity.setZKL(zkl);
			entity.setFXBL(ead);
			Double fgl = formula.getFGL(product.getBusinessPledge(),zkl,ead);// 获取到覆盖率
			//保存覆盖率
			formula.insertProcessValue(product.getProductNumber(), "覆盖率", fgl);
			
			product.setBusinessCoverage(String.valueOf(fgl));
			
			Double tzfgl=formula.getTZFGL(fgl);
			
			ZYWTZFGL.add(tzfgl);// 调整覆盖率

			//保存调整覆盖率
			formula.insertProcessValue(product.getProductNumber(), "调整覆盖率",tzfgl);
			
			product.setAdjustedCoverage(String.valueOf(tzfgl));
			
			Double tzxszj = formula.getDefaultCoefficient();// 调整系数之积

			//保存调整覆盖率
			formula.insertProcessValue(product.getProductNumber(), "调整系数之积", tzxszj);
			
			entity.setZYWTZFGL(tzfgl);
			
			entity.setZYWTZXSZJ(tzxszj);
			
			zywhsbl = formula.getZYWUHSBL(entity,count,appDebtRating.getPrincipalModeGuarantee());// 抵质押物1,2,3回收比例 ==>lgd
			ZYWHSBL.add(zywhsbl);

			//保存调整覆盖率
			formula.insertProcessValue(product.getProductNumber(), "质押物回收比例", zywhsbl);
			
			
			entity.setZKL(zkl);// 折扣率


		}
		// 质押物变现回收率
		double ZYWUHSBL1=formula.getZYWUHSBL1(entity,ZYWHSBL);
		
		//保存调整覆盖率
		formula.insertProcessValue("质押物变现回收率", "质押物变现回收率", ZYWUHSBL1);

		// 担保人信息AppGuarantor
		List<AppGuarantor> guarantorList = appDebtRating.getAppGuarantorList();

		List<String> BZRCODE = new ArrayList<String>();// 保证人回收编码

		List<Double> BZRJZHSL = new ArrayList<Double>();// 保证人基准回收率

		List<Double> BZRHSGXL = new ArrayList<Double>();// 保证人回收贡献率

		List<Double> BZRHSGX = new ArrayList<Double>();// 保证人回收贡献

		Double JKRBZRHSBL = null;


		entity.setJKRCODE(appDebtRating.getCustomerNumber());
		if(guarantorList.size()==0) {
			String ratingJZJZVal=jdbc.queryForObject("select FD_NAKED from PT_BORROWER_GUARANTOR WHERE fd_code=? and fd_type='1'", String.class,formula.defaultLevel);
			entity.setJKRJZHSL(Double.parseDouble(ratingJZJZVal));
			formula.insertProcessValue(appDebtRating.getCustomerNumber(), "质押物变现回收率", ratingJZJZVal);
		}
		for (int i = 0; i < guarantorList.size(); i++) {
			AppGuarantor appGua = guarantorList.get(i);
			BZRCODE.add(appGua.getGuarCustId());
			
			String sqlValue="";
			try {
				sqlValue=jdbc.queryForObject("select FD_FIN_LEVEL from NS_COMPANY_RATING where FD_CUSTNO=?  and FD_RATE_STATUS='010'",String.class,appGua.getGuarCustId());
			if(StringUtils.isNotBlank(sqlValue)) {
				appGua.setGuarCustPd(sqlValue);
			}else {
				appGua.setGuarCustPd("NOPD");
			}
			}catch(Exception e) {
				appGua.setGuarCustPd("NOPD");
			} 
			List<Double> listResult = new ArrayList<Double>();
			try {
				listResult = formula.getBZRHSGX(entity,appGua.getOwnership(), appGua.getValidityLaw(), "与借款人无关联关系（无互保）",
						appGua.getGuarCustId(),appGua.getGuarAreaName(),appGua.getGuarGbIndustry());
				BZRJZHSL.add(listResult.get(0));// 保证人基准回收率
				
				if(BZRHSGXL.size()==0) {
					BZRHSGXL.add(listResult.get(1));// 保证人回收贡献率
				}else {
					BZRHSGXL.add(listResult.get(1)*0);// 保证人回收贡献率
				}
				
				if(BZRHSGX.size()==0) {
					BZRHSGX.add(listResult.get(2));// 保证人回收贡献
				}else {
					BZRHSGX.add(listResult.get(2)*0);// 保证人回收贡献
				}
				
			} catch (EmptyResultDataAccessException e) {
				String ratingJZHSVal="";
				try {
					String level=jdbc.queryForObject("select FD_FIN_LEVEL from NS_COMPANY_RATING where FD_CUSTNO=?  and FD_RATE_STATUS='010'",String.class, appDebtRating.getCustomerNumber());
					ratingJZHSVal=jdbc.queryForObject("select FD_NAKED from PT_BORROWER_GUARANTOR WHERE fd_code=? and fd_type='1'", String.class,level);

				}catch (Exception e1) {
					ratingJZHSVal=jdbc.queryForObject("select FD_NAKED from PT_BORROWER_GUARANTOR WHERE fd_code=? and fd_type='1'", String.class,FormulaCalculateUtil.defaultLevel);
				}
				
				//保证人基准回收率
				
				BZRJZHSL.add(Double.parseDouble(ratingJZHSVal));// 保证人基准回收率
				BZRHSGXL.add(0.0);// 保证人回收贡献率
				BZRHSGX.add(0.0);// 保证人回收贡献
			} catch (Exception e) {
				String ratingJZHSVal="";
				try {
					String level=jdbc.queryForObject("select FD_FIN_LEVEL from NS_COMPANY_RATING where FD_CUSTNO=?  and FD_RATE_STATUS='010'",String.class, appDebtRating.getCustomerNumber());
					ratingJZHSVal=jdbc.queryForObject("select FD_NAKED from PT_BORROWER_GUARANTOR WHERE fd_code=? and fd_type='1'", String.class,level);

				}catch (Exception e1) {
					ratingJZHSVal=jdbc.queryForObject("select FD_NAKED from PT_BORROWER_GUARANTOR WHERE fd_code=? and fd_type='1'", String.class,FormulaCalculateUtil.defaultLevel);
				}
				BZRJZHSL.add(Double.parseDouble(ratingJZHSVal));// 保证人基准回收率
				BZRHSGXL.add(0.0);// 保证人回收贡献率
				BZRHSGX.add(0.0);// 保证人回收贡献
			}
			formula.insertProcessValue(appGua.getGuarCustId(), "保证人基准回收率",BZRJZHSL.get(i));
			formula.insertProcessValue(appGua.getGuarCustId(), "保证人回收贡献率",BZRHSGXL.get(i));
			formula.insertProcessValue(appGua.getGuarCustId(), "保证人回收贡献",BZRHSGX.get(i));

		}
		Double fzl = null;
		String isGroup="";
		try {
			isGroup = getData("select distinct fd_is_group_parent_company from ns_customer where FD_CUSTNO =?", String.class, custNo);
			if(isGroup.equals("true")){
				fzl = getData("select fd_index_value from (\r\n" + 
						"					 select t.*,rownum rn from(\r\n" + 
						"           select fd_index_value from NS_FINANCIAL_REPORT_INDEX where fd_index_code = 'LV001' and fd_customer_no=?\r\n" + 
						"order by fd_financial_report_date desc "+
						"decode(fd_specifications, '01', 1, '03', 2, , '02',3)asc  " + 
						"           )t)where rn=1 ", Double.class, custNo);
				//fd_specifications正序查
				
			}else {
				fzl = getData("select fd_index_value from (\r\n" + 
						"					 select t.*,rownum rn from(\r\n" + 
						"           select fd_index_value from NS_FINANCIAL_REPORT_INDEX where fd_index_code = 'LV001' and fd_customer_no=?\r\n" + 
						"order by fd_financial_report_date desc "+
						"decode(fd_specifications, '02', 1, '03', 2, , '01',3) asc  " + 
						"           )t)where rn=1 ", Double.class, custNo);
				//fd_specifications倒叙查
			}
		} catch (Exception e) {
			fzl=1.0;
		}

		// cityBank
		Double JKRHSGX = null;
		try {
			JKRHSGX = formula.getJKRHSGX(entity,appDebtRating.getOwnership(), fzl, appDebtRating.getCityBank(), BZRHSGXL,appDebtRating.getGuarAreaName(),appDebtRating.getGbindustry());
		} catch (Exception e) {
			throw new Exception("借款人回收贡献计算异常！");
		}

		Double JKRBZRJQPJJZHSL = null;
		try {
			JKRBZRJQPJJZHSL = formula.getJKRBZRJQPJJZHSL(entity,BZRJZHSL, BZRCODE);// 借款人&保证人加权平均基准回收率
		} catch (Exception e) {
			throw new Exception("借款人&保证人加权平均基准回收率计算异常！");
		}

		try {
			JKRBZRHSBL = formula.getJKRBZRHSBL(entity,BZRHSGX);// ==>lgd
			formula.insertProcessValue("JKR_BZR_HSBL", "借款人&保证人回收贡献率",JKRBZRHSBL);
		} catch (Exception e) {
			throw new Exception("借款人&保证人回收贡献率计算异常！");
		}

		//FormulaCalculateUtil lgd = new FormulaCalculateUtil();
		entity.setProductNo(productCode);
		entity.setSYQX(Integer.parseInt(appDebtRating.getBusinessDeadline()));
		Double lgdValue = null;
		formula.getCPHSBLVoid(entity);
		formula.getZXTZXSZJ(entity);
		try {
			lgdValue = formula.LGD(entity,ZYWUHSBL1, JKRBZRHSBL,appDebtRating.getLoanOrigin());
		} catch (Exception e) {
			throw new Exception("LGD分数计算异常！");
		}

		
		formula.insertProcessValue("LGD", "","LGD =  100%*(1 - 产品回收比例) × (1 - 抵质押物回收比例) × (1 - 借款人&保证人回收比例) / 债项调整系数");
		formula.insertProcessValue("LGD", "原始LGD值",lgdValue);
		if (lgdValue < 0.004) {
			lgdValue = 0.004;
		}
		
		if (lgdValue >=1) {
			lgdValue = 1.0;
		}

		*//**
		 * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑LGD↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		 *//*

		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓K↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		Double pd = 0.0;
		try {
			
			
			pd = getData("select fd_pd from ns_cfg_main_scale where FD_SCALE_LEVEL = (select FD_FIN_LEVEL from NS_COMPANY_RATING where FD_CUSTNO=? and FD_RATE_STATUS = '010') and fd_type='010'", Double.class, custNo);
		} catch (Exception e) {
			pd = getData("select fd_pd from ns_cfg_main_scale where FD_SCALE_LEVEL = ? and fd_type='010'", Double.class, FormulaCalculateUtil.defaultLevel);
		}
		Double k = null;
		String businessTypes = "";
		Integer businessDeadline = null;
		try {
			double r =0.0;
			String type="";
			FormulaCalculateUtilR formular = new FormulaCalculateUtilR();

			FormulaCalculateUtilRSME formulaRsem = new FormulaCalculateUtilRSME();
			try{
				type = getData("select fd_modelcode from ns_company_rating where fd_custno=?", String.class,custNo);
				//风险暴露如果为小微型则取中小企业风险暴露
				if(type.equals("C8")||type.equals("C9")||type.equals("C10")||type.equals("W1")) {
					
					Double s=30000000.00;
					try {
						 s=jdbc.queryForObject("select FD_ANNUAL_INCOME from NS_CUSTOMER  where FD_UNCUSTNO=?",Double.class,custNo);
					} catch (Exception e) {
						s=30000000.00;
					}
					r = formulaRsem.getRsme(pd,s);
				}else {//其他区一般企业
					r = formular.getR(pd);
				}
			}catch (Exception e) {
				r = formular.getR(pd);
			}
			
		
			if(StringUtils.isNotBlank(type)) {
				if(type.indexOf("FIRE")>-1) {
					formulaRf1.setCustCode(custNo);
					r = formulaRf1.getR();
				}else if(type.indexOf("CRE005")>-1) {
					formulaRsem.setCustCode(custNo);
					r = formulaRsem.getrsme();
				}else if(type.indexOf("SRE")>-1) {
					formular.setPd(pd);
					r = formular.getR();
				}else {
					formular.setPd(pd);
					r = formular.getR();
				}
			}

			
			businessDeadline = Integer.parseInt(appDebtRating.getBusinessDeadline());
			businessTypes = appDebtRating.getApplicationType();
			k = formulaK.getK(lgdValue, pd, r, custNo, businessDeadline);
		} catch (Exception e) {
			throw new Exception("K值计算异常！");
		}
		Double applicationRate = Double.parseDouble(appDebtRating.getApplicationRate());

		*//**
		 * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑K↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		 *//*

		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓目标利率↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		Double mbll = null;
		try {
			mbll = formulaOther.targetRate(productCode, ead, k, lgdValue, pd, businessDeadline, businessTypes, amt);
		} catch (Exception e) {
			throw new Exception("目标利率计算异常！");
			
			 * flag = false; msg = "目标利率计算异常！"; result.put("flag", String.valueOf(flag));
			 * result.put("msg", msg); return result;
			 
		}
		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓经济资本（内评法）↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		Double jjzb = null;
		try {
			jjzb = formulaOther.economicCapital(ead, k);
		} catch (Exception e) {
			throw new Exception("经济资本（内评法）计算异常！");
			
			 * flag = false; msg = "经济资本（内评法）计算异常！"; result.put("flag",
			 * String.valueOf(flag)); result.put("msg", msg); return result;
			 
		}

		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓实际利率↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		Double sjlv = null;
		try {
			sjlv = formulaOther.realInterestRate(productCode, ead, k, lgdValue, pd, applicationRate, businessDeadline,
					businessTypes, amt);

		} catch (Exception e) {
			throw new Exception("实际利率计算异常！");
			
			 * flag = false; msg = "实际利率计算异常！"; result.put("flag", String.valueOf(flag));
			 * result.put("msg", msg); return result;
			 
		}
		*//**
		 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓EVA（内评法）↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		 *//*
		String lgdLevel = "";
		try {
			lgdLevel = getData("select fd_level from ("
					+ "  select t.*,rownum rn from(select e.* from PT_LGD_LEVEL e  where ?>fd_interval_lower and ?<= fd_interval_upper)t)where rn=1 ",
					String.class, lgdValue,lgdValue);
		} catch (Exception e) {
			
			throw new Exception("LGD等级匹配失败！");
			
			 * flag = false; msg = "LGD等级匹配失败！"; result.put("flag", String.valueOf(flag));
			 * result.put("msg", msg); return result;
			 
		}

		Double EVA = formulaOther.EVA(sjlv, mbll, jjzb);
		result.put(EsbConstant.APPLY_NO, appDebtRating.getApplicationNumber());
		result.put(EsbConstant.LGD_RATE, String.valueOf(lgdValue));
		result.put(EsbConstant.LGD_LEVEL, lgdLevel);
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result.put(EsbConstant.COMPUTE_DATE, df.format(day));
		Double appRate = 0.0;
		if(StringUtils.isNotBlank(appDebtRating.getApplicationRate())) {
			appRate = Double.parseDouble(appDebtRating.getApplicationRate())*100;
			BigDecimal bg = new BigDecimal(appRate).setScale(2,RoundingMode.UP);
			appRate=bg.doubleValue();
		}
		Double lvSj=0.0;
		if(sjlv!=null) {
			lvSj=sjlv*100;
			BigDecimal bg = new BigDecimal(lvSj).setScale(2,RoundingMode.UP);
			lvSj=bg.doubleValue();
		}
		appDebtRating.setApplicationRate(appRate+"%/"+lvSj+"%");
		appDebtRating.setEad(ead.toString());
		appDebtRating.setEva(EVA.toString());
		appDebtRating.setLgd(lgdValue.toString());
		appDebtRating.setRaroc(mbll.toString());
		
		BigDecimal bigPd = new BigDecimal(pd.toString());
		String pdStr=bigPd.multiply(new BigDecimal("100")).toString();
		
		appDebtRating.setPdProbability(pdStr);
		appDebtRating.setEconomiccapital(sjlv.toString());
		appDebtRating.setCoefficientK(k.toString());
		try {
			CompanyCustomer cust= companyCustomerser.findById(appDebtRating.getCustomerNumber());
			appDebtRating.setCreator(cust.getManagerName());
			appDebtRating.setLastModifier(cust.getManagerName());
		} catch (Exception e) {
			throw new Exception("该客户在内评系统中不存在！");
		}
		
		appDebtRating.setCreateDate(new Date());
		appDebtRating.setLastModifyDate(new Date());
		//判断是否信用证类型
		Integer productCredit= jdbc.queryForObject("select count(*) from pt_ccf_parameter where fd_is_credit_card = 'true' and "
				+ " fd_product_no = ?", Integer.class,appDebtRating.getProductCode());
		//大于0为信用证类型
		if(productCredit==0) {
			appDebtRating.setTypeCredit("");
		}
		//删除旧数据
		jdbc.update("delete NS_APPLICATION_DEBT_RATING where  FD_APPLICATION_NUMBER =?",appDebtRating.getApplicationNumber());
		List<AppProductDetail> appGuar = appDebtRating.getAppProductDetailList();
		for (AppProductDetail appProductDetail : appGuar) {
			User user= userSer.findById(appProductDetail.getRegistrar());
			appProductDetail.setRegistrar(user.getUserName());
		}
		appDebtRatingService.add(appDebtRating);
		//appDebtRatingService.update(appDebtRating.getId(), appDebtRating);
		return result;
	}    

	*//**
	 * @ 解析JSON
	 * 
	 * @param fileUrl
	 * @return
	 * @throws Exception
	 *//*
	private AppDebtRating analysisJsonData(String fileUrl) throws Exception {
		String jsonStr = FileUtils.readToString(fileUrl);
		if (StringUtils.isNotEmpty(jsonStr)) {
			AppDebtRating ratingCustomer = JsonUtil.parseBean(jsonStr, AppDebtRating.class);
			return ratingCustomer;
		}
		return null;
	}


	*//**
	 * 开始解析JSON
	 *//*
	private AppDebtRating testAnalysisJsonData() throws Exception {

		String strJson = "{\r\n" + 
				"  \"applicationType\": \"2\",\r\n" + 
				"  \"applicationNumber\": \"2027093000000001\",\r\n" + 
				"  \"customerNumber\": \"20071027002899\",\r\n" + 
				"  \"customerName\": \"杭州新安安防系甸分公司\",\r\n" + 
				"  \"contractAmount\": \"500000\",\r\n" + 
				"  \"existingContractBalance\": \"\",\r\n" + 
				"  \"productName\": \"银行承兑汇票\",\r\n" + 
				"  \"productCode\": \"2010\",\r\n" + 
				"  \"businessTypes\": \"表外\",\r\n" + 
				"  \"applicationRate\": \"0.001\",\r\n" + 
				"  \"currency\": \"01\",\r\n" + 
				"  \"businessDeadline\": \"6\",\r\n" + 
				"  \"principalModeGuarantee\": \"0405010\",\r\n" + 
				"  \"industryTo\": \"I6311\",\r\n" + 
				"  \"byTime\": \"2027/09/30 03:26:51\",\r\n" + 
				"  \"typeCredit\": \"0\",\r\n" + 
				"  \"ownership\": \"BORROWER_OWNERSHIP_COLLECTIVE\",\r\n" + 
				"  \"cityBank\": \"2\",\r\n" + 
				"  \"guarAreaName\": \"\",\r\n" + 
				"  \"guarAreaCode\": \"140726\",\r\n" + 
				"  \"gbIndustry\": \"C3831\",\r\n" + 
				"  \"appProductDetailList\": [\r\n" + 
				"    {\r\n" + 
				"      \"applicationNumber\": \"2027093000000001\",\r\n" + 
				"      \"contractNumber\": \"2027093000000002\",\r\n" + 
				"      \"productNumber\": \"2027093000000001\",\r\n" + 
				"      \"rightNumber\": \"4612646464\",\r\n" + 
				"      \"cardType\": \"\",\r\n" + 
				"      \"collateralType\": \"060\",\r\n" + 
				"      \"guarName\": \"质押\",\r\n" + 
				"      \"guarTypelCode\": \"020220\",\r\n" + 
				"      \"guarTypel\": \"质押-银行汇票、本票、支票\",\r\n" + 
				"      \"assessmentTime\": \"\",\r\n" + 
				"      \"assessedPledge\": \"\",\r\n" + 
				"      \"determinedPledge\": \"500000\",\r\n" + 
				"      \"businessPledge\": \"500000\",\r\n" + 
				"      \"currency\": \"01\",\r\n" + 
				"      \"validityLaw\": \"\",\r\n" + 
				"      \"valueVolatility\": \"\",\r\n" + 
				"      \"closureConvenience\": \"\",\r\n" + 
				"      \"borrowerCorrelation\": \"\",\r\n" + 
				"      \"cashability\": \"\",\r\n" + 
				"      \"Insurance\": \"\",\r\n" + 
				"      \"registrar\": \"唐俊\",\r\n" + 
				"      \"registration\": \"光谷分行\",\r\n" + 
				"      \"registrationdate\": \"2027-09-30\",\r\n" + 
				"      \"updateTime\": \"2027-09-30\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"appGuarantorList\": [\r\n" + 
				"    {\r\n" + 
				"      \"applicationNumber\": \"2027093000000001\",\r\n" + 
				"      \"contractNumber\": \"2027093000000002\",\r\n" + 
				"      \"guarCustId\": \"20071027002899\",\r\n" + 
				"      \"guarCustName\": \"杭州新安安防系甸分公司\",\r\n" + 
				"      \"guarCustPd\": \"A-\",\r\n" + 
				"      \"guarGuarantee\": \"500000\",\r\n" + 
				"      \"validityLaw\": \"\",\r\n" + 
				"      \"ownership\": \"GUARANTOR_OWNERSHIP_FOREIGN_ENTERPRISE\",\r\n" + 
				"      \"guarGbIndustry\": \"C3831\",\r\n" + 
				"      \"guarAreaName\": \"\",\r\n" + 
				"      \"guarAreaCode\": \"140726\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"appGuaranTeeList\": [\r\n" + 
				"    {\r\n" + 
				"      \"applicationNumber\": \"2027093000000001\",\r\n" + 
				"      \"contractNumber\": \"2027093000000002\",\r\n" + 
				"      \"currency\": \"01\",\r\n" + 
				"      \"guaranteeAmount\": \"500000\",\r\n" + 
				"      \"guaranteeType\": \"010\",\r\n" + 
				"      \"guaranteeName\": \"一般担保\",\r\n" + 
				"      \"guaranteeSubject\": \"质押\",\r\n" + 
				"      \"guaranteeCode\": \"060\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		if (StringUtils.isNotEmpty(strJson)) {
			AppDebtRating appDebtRating = JsonUtil.parseBean(strJson, AppDebtRating.class);
			return appDebtRating;
		}
		return null;
	}

	*//**
	 * 文件下载
	 *//*
	private String fileDownload(String file_path) throws Exception {
		String fileName = file_path.substring(file_path.indexOf("BusinessEvaluateApplyData"));
		// 系统配置文件下载路径
		String url = DirectoryManager.getDirectoryByName("dir.work.web.upload");
		url = url + fileName;
		// 文件下载
		String config = DirectoryManager.getDirectoryByName("dir.config") + File.separator
				+ "FtpClientConfig.properties";
		FtpClientConfig.loadConf(config);
		log.info("文件路径:" + file_path);
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
	public static void main(String[] args) {
	}
}
*/