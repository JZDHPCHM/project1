package gbicc.irs.main.rating.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.engine.model.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;

import gbicc.irs.customer.entity.FinAccountData;
import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.customer.repository.NsBpMasterRepository;
import gbicc.irs.customer.service.FinAccountDataService;
import gbicc.irs.customer.service.FinancialStatementsService;
import gbicc.irs.customer.service.NsBpMasterService;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.support.CommonUtils;
/**
 * 主体评级及相关码值翻译类
 * @author wsh
 *
 */
@Service("clockDictionary")
public class BpMasterServiceImpl extends DaoServiceImpl<NsBpMaster, String, NsBpMasterRepository>
		implements NsBpMasterService {
	private static final Logger log =LoggerFactory.getLogger(BpMasterServiceImpl.class);
	
	@Autowired
	private JdbcTemplate template;
	@Autowired
	MainRatingServiceImpl mainRating;

	@Autowired
	FinancialStatementsService finaStatements;
	@Autowired
	FinAccountDataService finAccountData;
	
	


	public static List<String> roleCode(){
		List<String> list =CommonUtils.getAllRoleIds();
		return list;
	}
	
	
	
	
	/**
	 * 模糊搜索客户信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> fuzzySearch(String col) {
		String sql = "select " + col + " from t_aft_atten_customer group by " + col;
		List<Map<String, Object>> map = template.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				if (map2.get(key) != null) {
					res.put("name", map2.get(key).toString());
					result.add(res);
				}
			}
		}
		return result;
	}

	/**
	 * 模糊用户信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> userSearch() {
		String sql = "select FD_USERNAME from fr_aa_user";
		List<Map<String, Object>> map = template.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				if (map2.get(key) != null) {
					res.put("name", map2.get(key).toString());
					result.add(res);
				}
			}
		}
		return result;
	}

	/**
	 * 模糊项目信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> projectSearch() {
		String sql = "select PROJECT_NAME from ns_prj_project";
		List<Map<String, Object>> map = template.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				if (map2.get(key) != null) {
					res.put("name", map2.get(key).toString());
					result.add(res);
				}
			}
		}
		return result;
	}

	//

	
	/**
	 * 模糊搜索客户信息
	 * 
	 * @return
	 */
	public String fuzzySearchUser(String col) {
		String sql = "select fd_id,fd_username from fr_aa_user  ";
		List<Map<String, Object>> list = template.queryForList(sql);
		Map<String,Object> map =new HashMap<String,Object>();
		for (Map<String, Object> map2 : list) {
			map.put(map2.get("FD_ID").toString(), map2.get("FD_USERNAME"));
		}
		
		String json = "";
			try {
				json = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		
		return json;
	}
	
	/**
	 * 模糊搜索客户信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> fuzzySearchBpMaster(String col) {
		String sql = "select " + col + " from ns_bp_master where FD_bp_type <>'GUTA_NP' ";
		List<Map<String, Object>> map = template.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				if (map2.get(key) != null) {
					res.put("name", map2.get(key).toString());
					result.add(res);
				}
			}
		}
		return result;
	}

	/**
	 * warnrule
	 * 
	 * @param col
	 * 
	 * @return
	 */
	public List<Map<String, String>> fuzzySearchRule(String col) {
		String sql = "select " + col + " from t_aft_warn_rule ";
		List<Map<String, Object>> map = template.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				res.put("name", map2.get(key).toString());
				result.add(res);
			}
		}
		return result;
	}

	/**
	 * @国标、高精尖、所在园区字典表
	 */
	public Map<String, String> dicIndustry(String flag) {
		String sql = "select VALUE_CODE as KEY,VALUE_NAME as VALUE from NS_ZGC_FATHER_LEVEL ";
		List<Map<String, Object>> map = template.queryForList(sql);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map2 : map) {
			result.put(map2.get("KEY").toString(), map2.get("VALUE").toString());
		}
		return result;
	}

	/**
	 * @国标、高精尖、所在园区字典表
	 */
	public String dicIndustryToString(String flag) {
		String json = "";
		try {
			json = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(dicIndustry(flag));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * @机构
	 */
	public Map<String, String> dicOrg() {
		String sql = "select FD_CODE as KEY,FD_NAME as VALUE from Fr_Aa_Org WHERE FD_DESCRIPTION='SALES' OR FD_DESCRIPTION='ASSET'";
		List<Map<String, Object>> map = template.queryForList(sql);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map2 : map) {
			result.put(map2.get("KEY").toString(), map2.get("VALUE").toString());
		}
		return result;
	}

	/**
	 * @经济类型
	 */
	public Map<String, String> dicEconomic() {
		String sql = "select FD_CODE_VALUE as KEY,FD_CODE_VALUE_NAME as VALUE from NS_ECONOMIC_TYPE";
		List<Map<String, Object>> map = template.queryForList(sql);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map2 : map) {
			result.put(map2.get("KEY").toString(), map2.get("VALUE").toString());
		}
		return result;
	}

	/**
	 * @经济类型
	 */
	public String dicEconomicToString() {
		String json = "";
		try {
			json = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(dicEconomic());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * @企业规模/是否实际承租人/业务经理所在部门/企业荣誉
	 */
	public Map<String, String> esAlStType(String type) {
		String sql = "select CODE as KEY,CODE_NAME as VALUE from FD_ES_AL_ST_TYPE where TYPE= ?";
		List<Map<String, Object>> map = template.queryForList(sql, type);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map2 : map) {
			result.put(map2.get("KEY").toString(), map2.get("VALUE").toString());
		}
		return result;
	}

	/**
	 * @企业规模/是否实际承租人/业务经理所在部门/企业荣誉
	 */
	public String esAlStTypeToString(String type) {
		String json = "";
		try {
			json = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(esAlStType(type));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * @查询财报科目
	 */
	@Override
	public boolean subjects(Map<String, Object> mapP, NsBpMaster master) {
		List<Map<String, Object>> list = template.queryForList(
				"select fd_cust_code,FD_FIR_REP,FD_SEC_REP from ns_main_rating where fd_cust_code = ? and fd_rating_vaild='1' and fd_final_level is not null",
				master.getBpCode());
		if (!(list.size() > 0)) {
			return false;
		}
		String cust = (String) list.get(0).get("FD_CUST_CODE");
		String fir = (String) list.get(0).get("FD_FIR_REP");
		String sec = (String) list.get(0).get("FD_SEC_REP");
		String ratingId = (String) list.get(0).get("FD_ID");
		List<Map<String, Object>> listFir = template.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, fir);
		List<Map<String, Object>> listSec = template.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, sec);
		Map<String, String> reportKm = new HashMap<String, String>();
		for (Map<String, Object> map : listFir) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString(), map.get("FD_END_VALUE").toString());
		}

		for (Map<String, Object> map : listSec) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString() + "_S", map.get("FD_END_VALUE").toString());
		}
		List<Map<String, Object>> listDx = template
				.queryForList("select * from ns_non_fin where FD_MAIN_ID=? and fd_vaild='1'", ratingId);

		for (Map<String, Object> map : listDx) {
			reportKm.put(map.get("FD_CODE").toString(), map.get("FD_VALUE").toString());
		}
		Map<String, String> mapReport = new HashMap<String, String>();
		List<Map<String, Object>> lsitReport = (List<Map<String, Object>>) mapP.get("financialReportDatas");
		for (int i = 0; i < lsitReport.size(); i++) {
			List<Map<String, String>> listData = (List<Map<String, String>>) lsitReport.get(i)
					.get("financialSubjectDatas");
			for (int j = 0; j < listData.size(); j++) {
				mapReport.put(listData.get(j).get("finStatementItem").toString(),
						listData.get(j).get("amount") == null ? "" : listData.get(j).get("amount").toString());
			}
		}
		return compareMap2(reportKm,mapReport);
	}
	
	
	/**
	 * 3.0
	 * 校验财报指标数据是否有变化		输出结果目前有问题
	 */
	/**@Override
	public boolean subjects(Map<String, Object> mapP, NsBpMaster master) {
		//查询是否有最终评级结果
		List<Map<String, Object>> list = template.queryForList(
				"select fd_cust_code,FD_FIR_REP,FD_SEC_REP,FD_THI_REP from ns_main_rating where fd_cust_code = ? and fd_rating_vaild='1' and fd_final_level is not null",
				master.getBpCode());
		if (list.size()!=1) {
			return false;//无复审最终评级等级信息
		}
		String cust = (String) list.get(0).get("FD_CUST_CODE");//客户编号
		String fir = (String) list.get(0).get("FD_FIR_REP");//第一年财报编号
		String sec = (String) list.get(0).get("FD_SEC_REP");//第二年财报编号
		String thi = (String) list.get(0).get("FD_THI_REP");//第三年财报编号
		String ratingId = (String) list.get(0).get("FD_ID");//客户ID
		//ns_fin_account_data:客户财报科目数据 
		//第一年财报		数据项代码+期末值(指标编号+指标值)
		List<Map<String, Object>> listFir = template.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, fir);
		//第二年财报		数据项代码+期末值
		List<Map<String, Object>> listSec = template.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, sec);
		//第三年财报		数据项代码+期末值
		List<Map<String, Object>> listThi = template.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, thi);
		
		
		
		Map<String, String> reportKm = new HashMap<String, String>();
		for (Map<String, Object> map : listFir) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString(), map.get("FD_END_VALUE").toString());
		}

		for (Map<String, Object> map : listSec) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString() + "_S", map.get("FD_END_VALUE").toString());
		}
		
		for (Map<String, Object> map : listThi) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString() + "_T", map.get("FD_END_VALUE").toString());
		}
		
		//ns_non_fin:非财务信息	FD_ID:评级ID FD_MAIN_ID:承租人编号	承租人名称 评级类型
		List<Map<String, Object>> listDx = template.queryForList("select * from ns_non_fin where FD_MAIN_ID=? and fd_vaild='1'", ratingId);

		for (Map<String, Object> map : listDx) {
			reportKm.put(map.get("FD_CODE").toString(), map.get("FD_VALUE").toString());
		}
		Map<String, String> mapReport = new HashMap<String, String>();
		List<Map<String, Object>> lsitReport = (List<Map<String, Object>>) mapP.get("financialReportDatas");
		for (int i = 0; i < lsitReport.size(); i++) {
			List<Map<String, String>> listData = (List<Map<String, String>>) lsitReport.get(i).get("financialSubjectDatas");
			for (int j = 0; j < listData.size(); j++) {
				mapReport.put(listData.get(j).get("FINSTATEMENTITEM").toString(),
						listData.get(j).get("AMOUNT") == null ? "" : listData.get(j).get("AMOUNT").toString());
			}
		}
		return compareMap2(reportKm, mapReport);
	}
	*/

	public boolean compareMap2(Map<String, String> m1, Map<String, String> m2) {
		Iterator<Entry<String, String>> iter1 = m1.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry<String, String> entry1 = (Entry<String, String>) iter1.next();
			String m1value = entry1.getValue() == null ? "" : entry1.getValue();
			String m2value = m2.get(entry1.getKey()) == null ? "" : m2.get(entry1.getKey());

			if (!m1value.equals(m2value)) {// 若两个map中相同key对应的value不相等
				return false;
			}
		}
		return true;
	}
	public static <T> T parseBean(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * @主体评级数据解析入库 2.0
	 */
	@Override
	@Transactional
	public Map<String, String> parsingData(Map<String, Object> map) {
		String json = JSONObject.toJSON(map).toString();

		// map = (Map)JSONObject.parseObject(json.toString());
		MainRating mainEntity = new MainRating();
		mainEntity.setRatingVaild("1");
		NsBpMaster master = parseBean(json, NsBpMaster.class);
		if(map.get("bpId")==null) {
			Map<String, String> mapResult = new HashMap<String, String>();
			mapResult.put("flag", "020");
			mapResult.put("msg", "客户id为空，评级失败!");
			return mapResult;
		}
		master.setFdid(map.get("bpId").toString());
		if(subjects(map, master)) {
			Map<String, String> mapResult = new HashMap<String, String>();
			mapResult.put("flag", "020");
			mapResult.put("msg", "重复评级，请修改数据再次发起评级!");
			return mapResult;
		}
		template.update("delete ns_bp_master where fd_bp_code =?", master.getBpCode());
		repository.save(master);
		if (map.get("ratingType").toString().equals("OVER_STAGE")) {
			String sqlCount = "select count(*) from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='010' AND fd_rating_vaild='1' and fd_cust_code=? ";
			Integer count = template.queryForObject(sqlCount, Integer.class, master.getBpCode());
			if (count > 0) {
				String sql = "select FD_ID from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='010' and fd_rating_vaild='1' and  fd_cust_code=? ";
				String id = template.queryForObject(sql, String.class, master.getBpCode());
				template.update("update ns_rating_step set fd_vaild='2' where FD_rateid=? and fd_vaild='1'", id);
				mainEntity = mainRating.getRepository().findOne(id);
			} else {
				sqlCount = "select count(*) from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='020'  and fd_rating_vaild='1' and  fd_cust_code=?  ";
				count = template.queryForObject(sqlCount, Integer.class, master.getBpCode());
				if (count > 0) {
					String sql = "select FD_ID from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='020' and fd_rating_vaild='1' and  fd_cust_code=? ";
					String id = template.queryForObject(sql, String.class, master.getBpCode());
					template.update("update ns_rating_step set fd_vaild='0' where FD_rateid=? and fd_vaild='1'", id);
					mainEntity = mainRating.getRepository().findOne(id);
				} else {
					Map<String, String> mapResult = new HashMap<String, String>();
					mapResult.put("flag", "020");
					mapResult.put("msg", "无初评记录，无法复评!");
					return mapResult;
				}
				/*
				 * Map<String,String> mapResult = new HashMap<String,String>();
				 * mapResult.put("flag","020"); mapResult.put("msg", "无初评记录，无法复评!"); return
				 * mapResult;
				 */
			}
		}
		// 一个客户fd_rating_vaild的状态只会有一条
		if (map.get("ratingType").toString().equals("START_STAGE")) {
			String ratingEffective = "select count(fd_rating_vaild) from  ns_main_rating where  FD_VERSION='2.0' and fd_rating_status='010' and fd_rating_vaild='1' and fd_cust_code=? ";
			Integer ratingEffectiveCount = template.queryForObject(ratingEffective, Integer.class, master.getBpCode());
			if (ratingEffectiveCount == 1) {
				String sql = "select FD_ID from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='010' and fd_rating_vaild='1' and fd_cust_code=? ";
				String id = template.queryForObject(sql, String.class, master.getBpCode());
				template.update("update ns_rating_step set fd_vaild='0' where FD_rateid=?", id);
				mainEntity = mainRating.getRepository().findOne(id);
			}
			ratingEffective = "select count(fd_rating_vaild) from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='020' and fd_rating_vaild='1'  and fd_cust_code=? ";
			ratingEffectiveCount = template.queryForObject(ratingEffective, Integer.class, master.getBpCode());
			if (ratingEffectiveCount == 1) {
				String sql = "select FD_ID from  ns_main_rating where FD_VERSION='2.0' and fd_rating_status='020' and fd_rating_vaild='1' and fd_cust_code=?";
				String id = template.queryForObject(sql, String.class, master.getBpCode());
				//template.update("update ns_rating_step set fd_vaild='0' where FD_rateid=?", id);
				template.update("update ns_main_rating set fd_vaild='0',fd_rating_vaild='0' where FD_ID=?", id);
			}
		}

		mainEntity.setCustCode(master.getBpCode());
		mainEntity.setCustName(master.getBpName());
		// 区分版本号
		mainEntity.setFdVersion(map.get("version").toString());
		mainEntity.setFinalName(map.get("reviewer").toString());
		mainEntity.setType(master.getScoreTemplateId());
		mainEntity.setInternName(map.get("judge").toString());

		List<Map<String, Object>> lsitReport = (List<Map<String, Object>>) map.get("financialReportDatas");
		for (int i = 0; i < lsitReport.size(); i++) {
			String reportDate = lsitReport.get(i).get("fiscalTimes").toString();
			template.update("update ns_fin_stat set fd_vaild='0' where fd_cust_no=? and fd_report_buss_date=?",
					master.getBpCode(), reportDate);
			if (i == 0) {
				mainEntity.setFirRep(reportDate);
			} else {
				mainEntity.setSecRep(reportDate);
			}
			FinancialStatements financialStatements = new FinancialStatements();
			financialStatements.setFiscalTimes(lsitReport.get(i).get("fiscalTimes").toString());
			financialStatements.setFinStatementId(lsitReport.get(i).get("finStatementId").toString());
			financialStatements.setBpCode(lsitReport.get(i).get("bpCode").toString());
			financialStatements.setCurrencyCodeDesc(lsitReport.get(i).get("currencyCodeDesc").toString());
			financialStatements.setReportDetailType(lsitReport.get(i).get("reportDetailType").toString());
			financialStatements.setVaild("1");
			finaStatements.getRepository().save(financialStatements);

			List<Map<String, String>> listData = (List<Map<String, String>>) lsitReport.get(i)
					.get("financialSubjectDatas");
			for (int j = 0; j < listData.size(); j++) {
				FinAccountData accData = new FinAccountData();
				accData.setFinStatementId(listData.get(j).get("finStatementId"));
				accData.setAmount(Double.parseDouble(listData.get(j).get("amount")));
				accData.setFinStatementItem(listData.get(j).get("finStatementItem"));
				accData.setFinStatementType(listData.get(j).get("finStatementType"));
				accData.setItemDesc(listData.get(j).get("itemDesc"));
				finAccountData.getRepository().save(accData);
			}
		}
		List<Map<String, Object>> lsitNonFinancial = (List<Map<String, Object>>) map.get("scoreTargetDatas");

		String sql = " select * from  ns_main_rating where FD_VERSION='2.0' and fd_rating_vaild='1' and fd_cust_code=? ";
		List<Map<String, Object>> lsit = template.queryForList(sql, master.getBpCode());

		String status = "";
		if (map.get("ratingType").toString().equals("START_STAGE")) {
			mainEntity.setInternDate(new Date());
			status = "010";
			mainEntity.setInternCode(map.get("judge_id").toString());
			mainEntity.setRatingStatus(status);
		} else if (map.get("ratingType").toString().equals("OVER_STAGE")) {
			mainEntity.setFinalDate(new Date());
			mainEntity.setfDate(map.get("f_date").toString());
			mainEntity.setTreatN(map.get("treat_n").toString());
			mainEntity.setInternCode(map.get("judge_id").toString());
			mainEntity.setFinalCode(map.get("reviewer_id").toString());
			mainEntity.setFinalAdvice(map.get("reviewerOpinion").toString());
			status = "020";
			mainEntity.setRatingStatus(status);
		}

		String ratingId = "";
		try {
			if (lsit.size() > 0) {
				mainEntity.setId(lsit.get(0).get("FD_ID").toString());
				ratingId = mainRating.getRepository().save(mainEntity).getId();
			} else {
				template.update("update ns_main_rating set fd_vaild='0' where FD_VERSION='2.0' and fd_cust_code=? ", master.getBpCode());
				mainEntity.setVaild(true);
				ratingId = mainRating.getRepository().save(mainEntity).getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		template.update("update ns_non_fin set fd_vaild='0' where  FD_MAIN_ID=? ", ratingId);

		for (int i = 0; i < lsitNonFinancial.size(); i++) {
			String insert = "insert into ns_non_fin values(?,?,?,?,'1')";
			String uuid = UUID.randomUUID().toString();
			String codeDx = lsitNonFinancial.get(i).get("scoreTargetCode").toString().replace(" ", "");
			String valueDx = lsitNonFinancial.get(i).get("value").toString().replace(" ", "");
			template.update(insert, uuid, ratingId, codeDx, valueDx);
		}
		mainRating.getRepository().flush();
		Map<String, String> res = mainRating.subjects(mainEntity, status);
		return res;
	}
}
