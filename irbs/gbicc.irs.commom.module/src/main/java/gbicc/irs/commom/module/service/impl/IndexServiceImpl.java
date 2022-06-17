/**  
 * Project Name:gbicc.irs.commom.module  
 * File Name:IndexServiceImpl.java  
 * Package Name:gbicc.irs.commom.module.service.impl  
 * Date:2019年6月19日上午9:25:45  
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package gbicc.irs.commom.module.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import gbicc.irs.commom.module.business.PJFBFactory;
import gbicc.irs.commom.module.business.PJFBQuery;
import gbicc.irs.commom.module.service.IndexService;

/**  
 * ClassName:IndexServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2019年6月19日 上午9:25:45 <br/>  
 * @author   xiaoxianlie
 * @version    
 * @since    JDK 1.8  
 * @see        
 */

@Service
public class IndexServiceImpl implements IndexService{
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemParameterServiceImpl systemParameterServiceImpl;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Map<String,Object> formatScore(Map<String,List<Map<String,Object>>> indexDataMap){
		Map<String,Object> resMap = new HashMap<String,Object>();
		//组装定量指标
		Map<String,Object> dlMap = new HashMap<String,Object>();
		LinkedList<Object> dlIndexs = getIndexFromLst(indexDataMap.get("CUSQUANTITATIVE"));
		dlMap.put("indexNames", dlIndexs);
		dlMap.put("custScore", getIndexValFromLst(indexDataMap.get("CUSQUANTITATIVE"),dlIndexs));
		dlMap.put("insScore", getIndexValFromLst(indexDataMap.get("INS_QUANTITATIVE"),dlIndexs));
		resMap.put("dl", dlMap);
		//组装定性指标
		Map<String,Object> dxMap = new HashMap<String,Object>();
		LinkedList<Object> dxIndexs = getIndexFromLst(indexDataMap.get("CUSQUALITATIVE_EDIT"));
		dxMap.put("indexNames", dxIndexs);
		dxMap.put("custScore", getIndexValFromLst(indexDataMap.get("CUSQUALITATIVE_EDIT"),dxIndexs));
		dxMap.put("insScore", getIndexValFromLst(indexDataMap.get("INS_QUALITATIVE_EDIT"),dxIndexs));
		resMap.put("dx", dxMap);
		return resMap;
	}
	
	public LinkedList<Object> getIndexValFromLst(List<Map<String,Object>> lst,LinkedList<Object> indexs){
		LinkedList<Object> res = new LinkedList<Object>();
		if(lst==null|| lst.size()==0){
			for(Object k:indexs){
				res.add(0);
			}
			return res;
		}
		for(Object k:indexs){
			for(Map<String,Object> m : lst){
				String indexName = ((Map<String,String>)k).get("name");
				if(m.containsKey(indexName)){
					res.add(m.get(indexName));
				}
			}
		}
		return res;
	}

	public LinkedList<Object> getIndexFromLst(List<Map<String,Object>> lst){
		LinkedList<Object> res = new LinkedList<Object>();
		if(lst==null)
			return res;
		for(Map<String,Object> m : lst){
			for(Map.Entry<String,Object> en : m.entrySet()){
				//取指标名
				Map<String,String> name = new HashMap<String,String>();
				name.put("name", en.getKey());
				res.add(name);
			}
		}
		return res;
	}
	
	public String getLastY(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String date = df.format(new Date());
		return Integer.parseInt(date) -1 +"";
	}

	public List<Object> getRiskFocus(String custNo){
		List<Object> res = new ArrayList<Object>();
		String lastY = getLastY();
		//定量计算
		String custThrSql=
				"select de.fd_indicators_type as name,res.indexRate as rate from ( \n"+
				" -- 计算出三级指标里面最小的值和对应的二级指标的名称 \n"+
				"select min(decode(t.MEDIANVAL,0,9999999999*t.fd_val, ((t.fd_score-t.MEDIANVAL)/t.MEDIANVAL)*t.fd_val)) as indexRate, t.Pa_name from \n"+
				"( select * from (  \n"+
				"  -- 定量3级指标临时表  \n"+
				"SELECT INX.FD_INDEXNAME,INX.FD_SCORE,inx.fd_indexcode as code,pa.fd_indexname as PA_NAME \n"
				+ " FROM NS_RATING_INDEXES INX \n"
				+ " left join NS_RATING_INDEXES pa \n"
				+ " on inx.fd_parent_id = pa.fd_id \n"
				+ " and pa.fd_level = '2' \n"
				+ " WHERE INX.FD_STEPID IN(\n" +
						"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUANTITATIVE')\n" + 
						"       AND STEP.FD_RATEID =(SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING WHERE RATING.FD_CUSTNO='"+custNo+"' AND RATING.FD_VAILD='1' AND RATING.FD_RATE_STATUS='010' and RATING.fd_modelcode !='S3')\n" +
						") AND INX.FD_LEVEL='3' \n"
						+ " order by pa.fd_indexcode ) t3 left join  \n"
				+" --- 定量中位数临时表  \n"
				+ " (SELECT INX.FD_INDEXNAME,median(inx.fd_score) MEDIANVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUANTITATIVE' \n" + 
				"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING \n"
				+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum =1 ) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
				") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME ) med \n"
				+ " on t3.pa_name = med.FD_INDEXNAME \n"
				+ " left join ns_deviate_index dev \n"
				+ " on t3.code = dev.fd_code ) t \n"
				+ " group by t.PA_NAME ) res \n"
				+ " left join ns_deviating_value de \n"
				+ " on res.pa_name = de.fd_result \n"
				+ " where res.indexRate between de.fd_lower and de.fd_upper \n"
				+ " and de.fd_indicators_type not like '暂无%' ";
		
		List<Map<String, Object>> ratingThrIndex = jdbc.queryForList(custThrSql);
		int i = 0;
		for(Map<String, Object> m : ratingThrIndex){
			res.add(replaceTar(++i+"、"+m.get("name"),m.get("rate").toString()));
		}
		
		//定性计算
		String dxSql = 
				"select de.fd_indicators_type as name,t.indexRate as rate from ( \n" +
				"-- 计算出结果 \n"+
				"select decode(av.AVGVAL,0,9999999999, (dx.fd_score-av.AVGVAL)/av.AVGVAL) as indexRate, \n"
				+ "av.FD_INDEXNAME from "+
				"-- 先拿到定性指标  \n"+
				"(SELECT INX.FD_INDEXTYPE,INX.FD_INDEXNAME,INX.FD_SCORE FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUALITATIVE_EDIT')\n" + 
				"       AND STEP.FD_RATEID = (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING WHERE RATING.FD_CUSTNO='"+custNo+"' AND RATING.FD_VAILD='1' AND RATING.FD_RATE_STATUS='010' AND rating.fd_modelcode!='S3' )\n" +
				") AND INX.FD_LEVEL='2') dx left join \n" +
				"(SELECT INX.FD_INDEXNAME,avg(inx.fd_score) AVGVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUALITATIVE_EDIT' \n" + 
				"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING \n"
				+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1 ) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
				") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME ) av \n"
				+ " on dx.FD_INDEXNAME = av.FD_INDEXNAME ) t " 
				+ " left join ns_deviating_value de \n"
				+ " on t.FD_INDEXNAME = de.fd_result \n"
				+ " where t.indexRate between de.fd_lower and de.fd_upper \n"
				+ " and de.fd_indicators_type not like '暂无%' ";
		List<Map<String, Object>> dxLst = jdbc.queryForList(dxSql);
		for(Map<String, Object> m : dxLst){
			res.add(replaceTar(++i+"、"+m.get("name"),m.get("rate").toString()));
		}
		return res;
	}
	
	public List<Object> getRiskFocus2(String ratingId,String custNo){
		List<Object> res = new ArrayList<Object>();
		String lastY = getLastY();
		//定量计算
		String custThrSql=
				"select de.fd_indicators_type as name,res.indexRate as rate from ( \n"+
				" -- 计算出三级指标里面最小的值和对应的二级指标的名称 \n"+
				"select min(decode(t.MEDIANVAL,0,9999999999*t.fd_val, ((t.fd_score-t.MEDIANVAL)/t.MEDIANVAL)*t.fd_val)) as indexRate, t.Pa_name from \n"+
				"( select * from (  \n"+
				"  -- 定量3级指标临时表  \n"+
				"SELECT INX.FD_INDEXNAME,INX.FD_SCORE,inx.fd_indexcode as code,pa.fd_indexname as PA_NAME \n"
				+ " FROM NS_RATING_INDEXES INX \n"
				+ " left join NS_RATING_INDEXES pa \n"
				+ " on inx.fd_parent_id = pa.fd_id \n"
				+ " and pa.fd_level = '2' \n"
				+ " WHERE INX.FD_STEPID IN(\n" +
						"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUANTITATIVE')\n" + 
						"       AND STEP.FD_RATEID='"+ratingId+"'\n" +
						") AND INX.FD_LEVEL='3' \n"
						+ " order by pa.fd_indexcode ) t3 left join  \n"
				+" --- 定量中位数临时表  \n"
				+ " (SELECT INX.FD_INDEXNAME,median(inx.fd_score) MEDIANVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUANTITATIVE' \n" + 
				"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING \n"
				+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum =1 ) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
				") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME ) med \n"
				+ " on t3.pa_name = med.FD_INDEXNAME \n"
				+ " left join ns_deviate_index dev \n"
				+ " on t3.code = dev.fd_code ) t \n"
				+ " group by t.PA_NAME ) res \n"
				+ " left join ns_deviating_value de \n"
				+ " on res.pa_name = de.fd_result \n"
				+ " where res.indexRate between de.fd_lower and de.fd_upper \n"
				+ " and de.fd_indicators_type not like '暂无%' ";
		
		List<Map<String, Object>> ratingThrIndex = jdbc.queryForList(custThrSql);
		int i = 0;
		for(Map<String, Object> m : ratingThrIndex){
			res.add(replaceTar(++i+"、"+m.get("name"),m.get("rate").toString()));
		}
		
		//定性计算
		String dxSql = 
				"select de.fd_indicators_type as name,t.indexRate as rate from ( \n" +
				"-- 计算出结果 \n"+
				"select decode(av.AVGVAL,0,9999999999, (dx.fd_score-av.AVGVAL)/av.AVGVAL) as indexRate, \n"
				+ "av.FD_INDEXNAME from "+
				"-- 先拿到定性指标  \n"+
				"(SELECT INX.FD_INDEXTYPE,INX.FD_INDEXNAME,INX.FD_SCORE FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUALITATIVE_EDIT')\n" + 
				" AND STEP.FD_RATEID='"+ratingId+"'\n" +
				") AND INX.FD_LEVEL='2') dx left join \n" +
				"(SELECT INX.FD_INDEXNAME,avg(inx.fd_score) AVGVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
				"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUALITATIVE_EDIT' \n" + 
				"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING \n"
				+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1 ) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
				") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME ) av \n"
				+ " on dx.FD_INDEXNAME = av.FD_INDEXNAME ) t " 
				+ " left join ns_deviating_value de \n"
				+ " on t.FD_INDEXNAME = de.fd_result \n"
				+ " where t.indexRate between de.fd_lower and de.fd_upper \n"
				+ " and de.fd_indicators_type not like '暂无%' ";
		List<Map<String, Object>> dxLst = jdbc.queryForList(dxSql);
		for(Map<String, Object> m : dxLst){
			res.add(replaceTar(++i+"、"+m.get("name"),m.get("rate").toString()));
		}
		return res;
	}


	public String replaceTar(String str, String rate){
		if(str.endsWith("**%")){
			Double d = Double.parseDouble(rate);
			d = new BigDecimal(d*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			str = str.replace("**%",d+"%");
		}
		return str;
	}

	public Map<String,List<Map<String,Object>>> queryModelIndexData(String custNo) {
		Map<String,List<Map<String,Object>>> resultData = new HashMap<String, List<Map<String,Object>>>();
		//String custDlKey = "CUS_QUANTITATIVE";  客户定量
		//String custDxKey = "CUS_QUALITATIVE_EDIT"; 客户定性
		//String insDlKey = "INS_QUANTITATIVE"; 行业定量
		//String insDxKey = "INS_QUALITATIVE_EDIT"; 行业定性
		
		//查询本客户定性 定量指标 查询实时有效的记录
		String custRatingSql=
				"SELECT INX.FD_INDEXTYPE,INX.FD_INDEXNAME,nvl(INX.FD_SCORE,0) as FD_SCORE FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
						"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUANTITATIVE','QUALITATIVE_EDIT')\n" + 
						"       AND STEP.FD_RATEID = (select * from (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING WHERE RATING.FD_CUSTNO='"+custNo+"' AND RATING.FD_VAILD='1'  "
								+ " and fd_source='IRB' and fd_modelcode !='S3' order by rating.fd_last_modifydate DESC ) where rownum=1 )\n" + 
						") AND INX.FD_LEVEL='2'";
		
		List<Map<String, Object>> ratingIndex = jdbc.queryForList(custRatingSql);
		for(Map<String, Object> mi : ratingIndex) {
			String indexType = mi.get("FD_INDEXTYPE").toString();
			String indexName = mi.get("FD_INDEXNAME").toString();
			String indexScore = mi.get("FD_SCORE").toString();
			
			if(resultData.containsKey("CUS"+indexType)) {
				Map<String,Object> inxData = new HashMap<String, Object>();
				inxData.put(indexName, indexScore);
				resultData.get("CUS"+indexType).add(inxData);
			}else {
				List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
				Map<String,Object> inxData = new HashMap<String, Object>();
				inxData.put(indexName, indexScore);
				tempList.add(inxData);
				resultData.put("CUS"+indexType, tempList);
			}
		}
		String lastY = getLastY();
		//行业定量值（中位数） 取去年的记录
		String insDlSql="SELECT INX.FD_INDEXNAME,median(inx.fd_score) MEDIANVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
								"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUANTITATIVE' \n" + 
								"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING "
								+ "WHERE RATING.fd_modelcode = (select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1 ) " +
				" AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
								") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME";

		List<Map<String, Object>> insDlIndex = jdbc.queryForList(insDlSql);
		List<Map<String,Object>> insDlTempList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> insDl : insDlIndex) {
			String indexName = insDl.get("FD_INDEXNAME").toString();
			String medianVal = insDl.get("MEDIANVAL").toString();

			Map<String,Object> inxData = new HashMap<String, Object>();
			inxData.put(indexName, medianVal);
			insDlTempList.add(inxData);
		}
		//保存行业定量指标
		resultData.put("INS_QUANTITATIVE", insDlTempList);
		
		//行业定性值（均值）
		String insDxSql="SELECT INX.FD_INDEXNAME,avg(inx.fd_score) AVGVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
								"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUALITATIVE_EDIT' \n" + 
								"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING "
								+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
								") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME";
		
		List<Map<String, Object>> insDxIndex = jdbc.queryForList(insDxSql);
		List<Map<String,Object>> insDxTempList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> insDx : insDxIndex) {
			String indexName = insDx.get("FD_INDEXNAME").toString();
			String avgVal = insDx.get("AVGVAL").toString();
			
			Map<String,Object> inxData = new HashMap<String, Object>();
			inxData.put(indexName, avgVal);
			insDxTempList.add(inxData);
		}
		//保存行业定性指标
		resultData.put("INS_QUALITATIVE_EDIT", insDxTempList);
				
		return resultData;
	}
	
	public Map<String,List<Map<String,Object>>> queryModelIndexData2(String ratingId,String custNo) {
		Map<String,List<Map<String,Object>>> resultData = new HashMap<String, List<Map<String,Object>>>();
		//String custDlKey = "CUS_QUANTITATIVE";  客户定量
		//String custDxKey = "CUS_QUALITATIVE_EDIT"; 客户定性
		//String insDlKey = "INS_QUANTITATIVE"; 行业定量
		//String insDxKey = "INS_QUALITATIVE_EDIT"; 行业定性
		
		//查询本客户定性 定量指标 查询实时有效的记录
		String custRatingSql=
				"SELECT INX.FD_INDEXTYPE,INX.FD_INDEXNAME,INX.FD_SCORE FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
						"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE IN ('QUANTITATIVE','QUALITATIVE_EDIT')\n" + 
						"       AND STEP.FD_RATEID = '"+ratingId+"' \n" + 
						") AND INX.FD_LEVEL='2'";
		
		List<Map<String, Object>> ratingIndex = jdbc.queryForList(custRatingSql);
		for(Map<String, Object> mi : ratingIndex) {
			String indexScore = "0";
			String indexName  = "0";
			String indexType  = "0";
			try {
				indexType = mi.get("FD_INDEXTYPE").toString();
			}catch(Exception e) {
				
			}
			try {
				indexName = mi.get("FD_INDEXNAME").toString();
			}catch(Exception e) {
				
			}
			try {
				indexScore= mi.get("FD_SCORE").toString();
			}catch(Exception e) {
				
			}
			
			if(resultData.containsKey("CUS"+indexType)) {
				Map<String,Object> inxData = new HashMap<String, Object>();
				inxData.put(indexName, indexScore);
				resultData.get("CUS"+indexType).add(inxData);
			}else {
				List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
				Map<String,Object> inxData = new HashMap<String, Object>();
				inxData.put(indexName, indexScore);
				tempList.add(inxData);
				resultData.put("CUS"+indexType, tempList);
			}
		}
		String lastY = getLastY();
		//行业定量值（中位数） 取去年的记录
		String insDlSql="SELECT INX.FD_INDEXNAME,median(inx.fd_score) MEDIANVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
								"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUANTITATIVE' \n" + 
								"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING "
								+ "WHERE RATING.fd_modelcode = (select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1 ) " +
				" AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
								") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME";

		List<Map<String, Object>> insDlIndex = jdbc.queryForList(insDlSql);
		List<Map<String,Object>> insDlTempList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> insDl : insDlIndex) {
			String indexName = insDl.get("FD_INDEXNAME").toString();
			String medianVal = insDl.get("MEDIANVAL").toString();

			Map<String,Object> inxData = new HashMap<String, Object>();
			inxData.put(indexName, medianVal);
			insDlTempList.add(inxData);
		}
		//保存行业定量指标
		resultData.put("INS_QUANTITATIVE", insDlTempList);
		
		//行业定性值（均值）
		String insDxSql="SELECT INX.FD_INDEXNAME,avg(inx.fd_score) AVGVAL FROM NS_RATING_INDEXES INX WHERE INX.FD_STEPID IN(\n" +
								"       SELECT STEP.FD_ID FROM NS_RATING_STEP STEP WHERE STEP.FD_STEP_TYPE ='QUALITATIVE_EDIT' \n" + 
								"       AND STEP.FD_RATEID in (SELECT RATING.FD_ID FROM NS_COMPANY_RATING RATING "
								+ "WHERE RATING.fd_modelcode=(select a.fd_modelcode from NS_COMPANY_RATING a where a.fd_custno='"+custNo+"' AND a.fd_process_status ='060' AND a.FD_RATE_STATUS='020' and to_char(a.fd_effective_time,'YYYY')="+lastY+" and rownum=1) AND RATING.fd_process_status ='060' AND RATING.FD_RATE_STATUS='020')\n" +
								") AND INX.FD_LEVEL='2' GROUP BY INX.FD_INDEXNAME";
		
		List<Map<String, Object>> insDxIndex = jdbc.queryForList(insDxSql);
		List<Map<String,Object>> insDxTempList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> insDx : insDxIndex) {
			String indexName = insDx.get("FD_INDEXNAME").toString();
			String avgVal = insDx.get("AVGVAL").toString();
			
			Map<String,Object> inxData = new HashMap<String, Object>();
			inxData.put(indexName, avgVal);
			insDxTempList.add(inxData);
		}
		//保存行业定性指标
		resultData.put("INS_QUALITATIVE_EDIT", insDxTempList);
				
		return resultData;
	}
	
	public List<String> getZH(){
		List<String> orgs = jdbcTemplate.queryForList("select * from (select fd_code from fr_aa_org start with fd_code = '1009999' connect by prior fd_id = fd_parent_id)"+
				" where fd_code not in ('1009699','1007599')" ,String.class);
		return orgs;
	}
	
	@Override
	public boolean isZH(){
		User currUser = null;
		try {
			currUser = userService.findById(SecurityUtil.getUserId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Org currOrg = null;
		if(currUser.getOrgs() !=null && currUser.getOrgs().size() > 0) {
			currOrg = currUser.getOrgs().get(0); 
		}
		return currOrg==null?false:getZH().contains(currOrg.getCode());
	}

	@Override
	public List<Object> ratingGroup(String type) {
		List<Object> result = new ArrayList<Object>();
		
		try {
			
			User currUser = userService.findById(SecurityUtil.getUserId());
			Org currOrg = null;
			if(currUser.getOrgs() !=null && currUser.getOrgs().size() > 0) {
				currOrg = currUser.getOrgs().get(0); 
			}
			
			String sql = 
					"SELECT\n" +
							" SC.FD_SCALE_LEVEL as PD_LEVEL,\n" + 
							" nvl((\n" + 
							"     SELECT COUNT(1) FROM NS_COMPANY_RATING AA \n" + 
							"     WHERE AA.FD_FIN_LEVEL=SC.FD_SCALE_LEVEL AND AA.FD_VAILD='1' AND AA.FD_RATE_STATUS='010' AND AA.Fd_Source ='IRB' \n" + 
							"     GROUP BY AA.FD_FIN_LEVEL \n" + 
							" ),0) AS RATINGNUM \n" + 
							" FROM NS_CFG_MAIN_SCALE SC  WHERE SC.FD_TYPE='010'  ORDER BY SC.FD_SORT";
			
			if(type.equals("1")) { //本行数据
				if(currOrg != null) {
					result.add(currOrg.getName());
					//工厂类处理
					PJFBQuery query = new PJFBFactory().getPJFB(currOrg,getZH());
					sql =query.get();
				}else {
					result.add("");
				}
		
			}else {
				result.add("全行");
			}
			
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			List<String> rating = new ArrayList<String>();
			List<String> number = new ArrayList<String>();
			for (int z = 0; z < list.size(); z++) {
				rating.add(list.get(z).get("PD_LEVEL").toString());
				number.add(list.get(z).get("RATINGNUM") == null ? "0" : list.get(z).get("RATINGNUM").toString());
			}
			
			result.add(rating);
			result.add(number);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, Object> findCustNo( String custNo){
		List<Map<String, Object>> custOreder = jdbc.queryForList(
				"select cust.FD_CRDT_AMT,rating.FD_PD,rating.FD_FIN_LEVEL,rating.FD_QUAL_SCO,rating.FD_QUAN_SCO from NS_CUSTOMER cust "
						+ "left JOIN (select * from NS_COMPANY_RATING rating where FD_RATE_STATUS='010' and fd_modelcode!='S3' ) rating on cust.FD_CUSTNO=rating.FD_CUSTNO where cust.FD_CUSTNO =? ",
				custNo);

		if(custOreder.size()==0) {
			return null;
		}
		 String qudUpdateDate = jdbc.queryForObject("select * from (select fd_last_modifyDate from NS_CUSTOMER where fd_custNo = ?  order by fd_last_modifyDate desc nulls last ) where rownum<=1", String.class,custNo);
		 Map<String, Object> map = custOreder.get(0);
		 map.put("qudUpdateDate", "");
		 if (null != qudUpdateDate) {
			 map.put("qudUpdateDate", qudUpdateDate);
		 }
		if (null != custOreder) {
			
			for (String str : map.keySet()) {
				if (map.get(str) == null) {
					map.put(str, "");
				}
			}
			return map;
		}
		return null;
	}
	

	//已完成
	public List<Map<String, Object>> completedTask(Integer start,Integer end) {
		
		String sql = "select *\r\n" + 
				"  from (select *\r\n" + 
				"          from (select id,\r\n" + 
				"                       custid,\r\n" + 
				"                       FD_CUSTNAME,\r\n" + 
				"                       type,\r\n" + 
				"                       tmp.Start_Time_ as taskDate,\r\n" + 
				"                       url\r\n" + 
				"                  from (select id,\r\n" + 
				"                               custid,\r\n" + 
				"                               case state\r\n" + 
				"                                 when 'COMPLETED' then\r\n" + 
				"                                  '090'\r\n" + 
				"                                 else\r\n" + 
				"                                  state\r\n" + 
				"                               end as state,\r\n" + 
				"                               type,\r\n" + 
				"                               taskDate,\r\n" + 
				"                               url\r\n" + 
				"                          from (select fd_id as id,\r\n" + 
				"                                       FD_PROCESS_STATE as state,\r\n" + 
				"                                       iss.FD_CUSTID as custid,\r\n" + 
				"                                       '发债企业评级' as type,\r\n" + 
				"                                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                                       '/irs/financialOrIssueBonds/issueBondsRatingStart.html' as url\r\n" + 
				"                                  from NS_ISSUE_BONDS_DATA_RATING iss\r\n" + 
				"                                union all\r\n" + 
				"                                select fd_id as id,\r\n" + 
				"                                       FD_PROCESS_STATE as state,\r\n" + 
				"                                       inte.FD_CUSTID as custid,\r\n" + 
				"                                       '金融同业评级' as type,\r\n" + 
				"                                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                                       '/irs/financialOrIssueBonds/financialRatingStart.html' as url\r\n" + 
				"                                  from NS_INTERBANK_DATA_RATING inte\r\n" + 
				"                                union all\r\n" + 
				"                                select fd_id as id,\r\n" + 
				"                                       FD_STATUS as state,\r\n" + 
				"                                       nsdx.FD_DEFAULT_CUST_NO as custid,\r\n" + 
				"                                       '违约重生' as type,\r\n" + 
				"                                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                                       '/irs/rebirthDefaultCustomerMan/rebirthDefaultCustomerStart.html' as url\r\n" + 
				"                                  from NS_DC_CUSTOMER_REBIRTH nsdx\r\n" + 
				"                                union all\r\n" + 
				"                                select risk.contract_num as id,\r\n" + 
				"                                       null as state,\r\n" + 
				"                                       risk.ctm_num as custid,\r\n" + 
				"                                       '风险暴露' as type,\r\n" + 
				"                                       start_dt as taskDate,\r\n" + 
				"                                       '/irs/risk/approval.html' as url\r\n" + 
				"                                  from irs_risk_exposure risk\r\n" + 
				"                                union all\r\n" + 
				"                                select fd_id as id,\r\n" + 
				"                                       FD_PROCESS_STATUS as state,\r\n" + 
				"                                       nsc.FD_CUSTNO as custid,\r\n" + 
				"                                       '客户评级' as type,\r\n" + 
				"                                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                                       '/irs/companyRating/companyRating.html' as url\r\n" + 
				"                                  from NS_COMPANY_RATING nsc) t)\r\n" + 
				"                 inner join (select fd_custno, fd_custname\r\n" + 
				"                              from Ns_Customer\r\n" + 
				"                            union all\r\n" + 
				"                            select fd_custno, fd_custname\r\n" + 
				"                              from NS_FINANCIAL_INDUSTRY\r\n" + 
				"                            union all\r\n" + 
				"                            select fd_custno, fd_custname from NS_ISSUE_BONDS \r\n"+
				"                            union \r\n" + 
				"                            select fd_custno, fd_custname from esb_rating_customer"+
				") cust\r\n" + 
				"                    on cust.FD_CUSTNO = custid\r\n" + 
				"                \r\n" + 
				"                 right join (\r\n" + 
				"                            \r\n" + 
				"                            SELECT resp.NAME_ AS processName,\r\n" + 
				"                                    ht.Start_Time_,\r\n" + 
				"                                    pro.business_key_,\r\n" + 
				"                                    count(1) AS taskNum\r\n" + 
				"                              FROM act_hi_taskinst ht\r\n" + 
				"                              LEFT JOIN act_re_procdef resp\r\n" + 
				"                                ON ht.PROC_DEF_ID_ = resp.ID_\r\n" + 
				"                              left join ACT_HI_PROCINST pro\r\n" + 
				"                                on ht.proc_inst_id_ = pro.proc_inst_id_\r\n" + 
				"                             WHERE ht.ASSIGNEE_ = ?\r\n" + 
				"                             GROUP BY resp.NAME_,\r\n" + 
				"                                       pro.business_key_,\r\n" + 
				"                                       ht.Start_Time_) tmp\r\n" + 
				"                    on id = business_key_\r\n" + 
				"                 where 1 = 1\r\n" + 
				"                   and business_key_ not in\r\n" + 
				"                       (select business_key_\r\n" + 
				"                          from ACT_RU_EXECUTION\r\n" + 
				"                         where business_key_ is not null)\r\n" + 
				"                   and taskDate is not null)\r\n" + 
				"        \r\n" + 
				"        union all (select nsd.fd_id as id,\r\n" + 
				"                         nsd.FD_CUST_NO as custid,\r\n" + 
				"                         c.fd_custname as fd_custname,\r\n" + 
				"                         '违约认定' as type,\r\n" + 
				"                         nsd.FD_CREATE_DATE as taskDate,\r\n" + 
				"                         '/irs/defaultCustomerMan/defaultCustomerStart.html' as url\r\n" + 
				"                    from NS_DC_CUSTOMER nsd\r\n" + 
				"                    left join ns_dc_customer_status s\r\n" + 
				"                      on nsd.fd_id = s.fd_custno\r\n" + 
				"                    left join ns_customer c\r\n" + 
				"                      on nsd.fd_cust_no = c.fd_custno\r\n" + 
				"                   where nsd.fd_vaild in (0, 1)\r\n" + 
				"                     and s.fd_creator = '" + SecurityUtil.getLoginName() + "'))\r\n" + 
				" order by taskDate desc\r\n" ;
//		String sql = "select  id,custid,FD_CUSTNAME,type,tmp.Start_Time_ as taskDate ,url from (\r\n"
//				+ "select  id,custid,case state when 'COMPLETED' then '090' else state end as state,type,taskDate, url from (\r\n"
//				+ "select fd_id as id,FD_PROCESS_STATE as state,iss.FD_CUSTID as custid,'发债企业评级' as type,FD_CREATE_DATE as taskDate,'/irs/financialOrIssueBonds/issueBondsRatingStart.html' as url  from NS_ISSUE_BONDS_DATA_RATING iss\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_PROCESS_STATE as state,inte.FD_CUSTID as custid,'金融同业评级'as type,FD_CREATE_DATE as taskDate ,'/irs/financialOrIssueBonds/financialRatingStart.html' as url  from NS_INTERBANK_DATA_RATING inte\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_STATUS as state,nsd.FD_CUST_NO as custid,'违约认定'as type,FD_CREATE_DATE  as taskDate,'/irs/defaultCustomerMan/defaultCustomerStart.html' as url  from NS_DC_CUSTOMER nsd\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_STATUS as state,nsdx.FD_DEFAULT_CUST_NO as custid ,'违约重生'as type,FD_CREATE_DATE as taskDate,'/irs/rebirthDefaultCustomerMan/rebirthDefaultCustomerStart.html' as url  from NS_DC_CUSTOMER_REBIRTH nsdx\r\n"
//				+ "union all \n"
//				+" select risk.contract_num as id,null as state,risk.ctm_num as custid,'风险暴露'as type,start_dt  as taskDate,'/irs/risk/approval.html' as url  from irs_risk_exposure risk \n" 
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_PROCESS_STATUS as state,nsc.FD_CUSTNO as custid,'客户评级'as type,FD_CREATE_DATE  as taskDate,'/irs/companyRating/companyRating.html' as url  from NS_COMPANY_RATING nsc )t)\r\n"
//				+ "inner join  (\r\n" +
//				"select fd_custno,fd_custname from Ns_Customer\r\n" +
//				"union all\r\n" +
//				"select fd_custno,fd_custname from NS_FINANCIAL_INDUSTRY\r\n" +
//				"union all\r\n" +
//				"select fd_custno,fd_custname from NS_ISSUE_BONDS)cust on cust.FD_CUSTNO=custid\r\n" + "\r\n" + "right join (\r\n" + "\r\n" + "SELECT \r\n"
//				+ "              resp.NAME_ AS processName,ht.Start_Time_, \r\n" + "              pro.business_key_,\r\n"
//				+ "              count( 1 ) AS taskNum  \r\n" + "            FROM \r\n"
//				+ "              act_hi_taskinst ht \r\n"
//				+ "              LEFT JOIN act_re_procdef resp ON ht.PROC_DEF_ID_ = resp.ID_  \r\n"
//				+ "              left join ACT_HI_PROCINST pro on ht.proc_inst_id_ = pro.proc_inst_id_ \r\n"
//				+ "            WHERE \r\n" + "              ht.ASSIGNEE_ = ?   \r\n" + "            GROUP BY \r\n"
//				+ "              resp.NAME_,\r\n" + "               pro.business_key_,ht.Start_Time_ ) tmp  on id=business_key_  \r\n"
//				+ "              where 1=1 and business_key_ not in(select business_key_ from ACT_RU_EXECUTION where business_key_ is not null )  and taskDate is not null order by  taskDate desc";
		List<Map<String, Object>> task = jdbc.queryForList(ArticleBeforeRow(sql,start,end), SecurityUtil.getLoginName());
		if(task!=null&&task.size()!=0) {
			// 将总条数放到第一条记录中
			Map<String, Object> totalMap = jdbc.queryForMap(getCountSql(sql), SecurityUtil.getLoginName());
			task.get(0).put("total",totalMap.get("total"));
		}
		return checkData(task);
	}
	
	//已办
	public List<Map<String, Object>> haveToDoTask(Integer start,Integer end) {
		String sql = "select * from (select * from (select id,\r\n" + 
				"       custid,\r\n" + 
				"       FD_CUSTNAME,\r\n" + 
				"       type || '-' || to_char(taskName) as type,\r\n" + 
				"       task.Start_Time_ as taskDate,\r\n" + 
				"       url\r\n" + 
				"  from (select id,\r\n" + 
				"               custid,\r\n" + 
				"               case state\r\n" + 
				"                 when 'COMPLETED' then\r\n" + 
				"                  '090'\r\n" + 
				"                 else\r\n" + 
				"                  state\r\n" + 
				"               end as state,\r\n" + 
				"               type,\r\n" + 
				"               taskDate,\r\n" + 
				"               url\r\n" + 
				"          from (select fd_id as id,\r\n" + 
				"                       FD_PROCESS_STATE as state,\r\n" + 
				"                       iss.FD_CUSTID as custid,\r\n" + 
				"                       '发债企业评级' as type,\r\n" + 
				"                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                       '/irs/financialOrIssueBonds/issueBondsRatingStart.html' as url\r\n" + 
				"                  from NS_ISSUE_BONDS_DATA_RATING iss\r\n" + 
				"                union all\r\n" + 
				"                select fd_id as id,\r\n" + 
				"                       FD_PROCESS_STATE as state,\r\n" + 
				"                       inte.FD_CUSTID as custid,\r\n" + 
				"                       '金融同业评级' as type,\r\n" + 
				"                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                       '/irs/financialOrIssueBonds/financialRatingStart.html' as url\r\n" + 
				"                  from NS_INTERBANK_DATA_RATING inte\r\n" + 
				"                union all\r\n" + 
				"                \r\n" + 
				"                select fd_id as id,\r\n" + 
				"                       FD_STATUS as state,\r\n" + 
				"                       nsdx.FD_DEFAULT_CUST_NO as custid,\r\n" + 
				"                       '违约重生' as type,\r\n" + 
				"                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                       '/irs/rebirthDefaultCustomerMan/rebirthDefaultCustomerStart.html' as url\r\n" + 
				"                  from NS_DC_CUSTOMER_REBIRTH nsdx\r\n" + 
				"                union all\r\n" + 
				"                select risk.contract_num as id,\r\n" + 
				"                       null as state,\r\n" + 
				"                       risk.ctm_num as custid,\r\n" + 
				"                       '风险暴露' as type,\r\n" + 
				"                       start_dt as taskDate,\r\n" + 
				"                       '/irs/risk/approval.html' as url\r\n" + 
				"                  from irs_risk_exposure risk\r\n" + 
				"                union all\r\n" + 
				"                select fd_id as id,\r\n" + 
				"                       FD_PROCESS_STATUS as state,\r\n" + 
				"                       nsc.FD_CUSTNO as custid,\r\n" + 
				"                       '客户评级' as type,\r\n" + 
				"                       FD_CREATE_DATE as taskDate,\r\n" + 
				"                       '/irs/companyRating/companyRating.html' as url\r\n" + 
				"                  from NS_COMPANY_RATING nsc) t)\r\n" + 
				"  left join (select fd_custno, fd_custname\r\n" + 
				"               from Ns_Customer\r\n" + 
				"             union all\r\n" + 
				"             select fd_custno, fd_custname\r\n" + 
				"               from NS_FINANCIAL_INDUSTRY\r\n" + 
				"             union all\r\n" + 
				"             select fd_custno, fd_custname from NS_ISSUE_BONDS"+
				"                            union \r\n" + 
				"                            select fd_custno, fd_custname from esb_rating_customer"+
				 ") cust\r\n" + 
				"    on cust.FD_CUSTNO = custid\r\n" + 
				" right join (select a.Start_Time_,\r\n" + 
				"                    a.business_key_,\r\n" + 
				"                    b.end_time_,\r\n" + 
				"                    b.name_ as taskName\r\n" + 
				"               from act_ru_execution a\r\n" + 
				"              inner join act_hi_taskinst b\r\n" + 
				"                 on b.proc_inst_id_ = a.id_\r\n" + 
				"             where b.assignee_ = '" + SecurityUtil.getLoginName() + "'\r\n" + 
				"              ) task\r\n" + 
				"    on id = business_key_\r\n" + 
				" where 1 = 1\r\n" + 
				"   and task.end_time_ is not null\r\n" + 
				"   and taskDate is not null\r\n" + 
				" )\r\n" + 
				" union all \r\n" + 
				" (select nsd.fd_id as id,\r\n" + 
				"        nsd.FD_CUST_NO as custid,\r\n" + 
				"    r.FD_CUSTNAME as FD_CUSTNAME,\r\n" + 
				"    '违约认定-提交申请' as type,\r\n" + 
				"        nsd.fd_create_date,\r\n" + 
				"        '/irs/defaultCustomerMan/defaultCustomerStart.html' as url\r\n" + 
				"        from NS_DC_CUSTOMER nsd left join ns_customer r on nsd.fd_cust_no = r.fd_custno left join ns_dc_customer_status s on nsd.fd_id = s.fd_custno\r\n" + 
				"    where s.fd_creator = '" + SecurityUtil.getLoginName()  + "' and nsd.fd_vaild not in (0,1,3))) order by taskDate desc\r\n" + 
				"               \r\n";
//		String sql = "select  id,custid,FD_CUSTNAME,type||'-'||taskName as type,task.Start_Time_ as taskDate,url from (\r\n"
//				+ "select  id,custid,case state when 'COMPLETED' then '090' else state end as state,type,taskDate,url from (\r\n"
//				+ "select fd_id as id,FD_PROCESS_STATE as state,iss.FD_CUSTID as custid,'发债企业评级' as type,FD_CREATE_DATE as taskDate,'/irs/financialOrIssueBonds/issueBondsRatingStart.html' as url  from NS_ISSUE_BONDS_DATA_RATING iss\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_PROCESS_STATE as state,inte.FD_CUSTID as custid,'金融同业评级'as type,FD_CREATE_DATE as taskDate ,'/irs/financialOrIssueBonds/financialRatingStart.html' as url  from NS_INTERBANK_DATA_RATING inte\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_STATUS as state,nsd.FD_CUST_NO as custid,'违约认定'as type,FD_CREATE_DATE  as taskDate,'/irs/defaultCustomerMan/defaultCustomerStart.html'  as url from NS_DC_CUSTOMER nsd\r\n"
//				+ "union all\r\n"
//				+ "select fd_id as id,FD_STATUS as state,nsdx.FD_DEFAULT_CUST_NO as custid ,'违约重生'as type,FD_CREATE_DATE as taskDate,'/irs/rebirthDefaultCustomerMan/rebirthDefaultCustomerStart.html' as url  from NS_DC_CUSTOMER_REBIRTH nsdx\r\n"
//				+ "union all\r\n"
//				+" select risk.contract_num as id,null as state,risk.ctm_num as custid,'风险暴露'as type,start_dt  as taskDate,'/irs/risk/approval.html' as url  from irs_risk_exposure risk \n" 
//				+ "union all \n"
//				+ "select fd_id as id,FD_PROCESS_STATUS as state,nsc.FD_CUSTNO as custid,'客户评级'as type,FD_CREATE_DATE  as taskDate,'/irs/companyRating/companyRating.html' as url  from NS_COMPANY_RATING nsc )t)\r\n"
//				+ "left join  (\r\n" +
//				"select fd_custno,fd_custname from Ns_Customer\r\n" +
//				"union all\r\n" +
//				"select fd_custno,fd_custname from NS_FINANCIAL_INDUSTRY\r\n" +
//				"union all\r\n" +
//				"select fd_custno,fd_custname from NS_ISSUE_BONDS)cust  on cust.FD_CUSTNO=custid\r\n"
//				+ "right join (select a.Start_Time_,a.business_key_,b.end_time_,b.name_ as taskName from act_ru_execution a \r\n" +
//				"						 inner join  act_hi_taskinst b on b.proc_inst_id_ = a.id_ \r\n" +
//				"						  where b.assignee_=? ) task \r\n"
//				+ "               on id=business_key_  \r\n"
//				+ "               where 1=1 " 
//				+" and task.end_time_ is not null and taskDate is not null order by  taskDate desc";
		List<Map<String, Object>> task = jdbc
				.queryForList(ArticleBeforeRow(sql,start,end));
		if(task!=null&&task.size()!=0) {
			// 将总条数放到第一条记录中
			Map<String, Object> totalMap = jdbc.queryForMap(getCountSql(sql));
			task.get(0).put("total",totalMap.get("total"));
		}
		return checkData(task);
	}
	
	//代办
	public List<Map<String, Object>> queryTask(String loginNo,Integer start,Integer end) {
		String sql ="SELECT id,custid,FD_CUSTNAME,type,TA.Start_Time_ as taskDate,DECODE(type,'客户评级',DECODE(task_def_key_,'A1','/irs/companyRating/companyRating.html',url),url) as url FROM\r\n" +
				"    (\r\n" +
				"      SELECT T1.Start_Time_,T.ID_ AS TASK_ID,T.PROC_INST_ID_ AS PROC_INST_ID,T1.BUSINESS_KEY_,TASK_DEF_ID_,T.TASK_DEF_KEY_ FROM\r\n" +
				"      (\r\n" +
				"        SELECT DISTINCT RES.*\r\n" +
				"            FROM ACT_RU_TASK RES\r\n" +
				"              INNER JOIN ACT_RE_PROCDEF D ON RES.PROC_DEF_ID_ = D.ID_\r\n" +
				"                    WHERE (RES.ASSIGNEE_ = '"+loginNo+"')\r\n" +
				"         ) T\r\n" +
				"         LEFT JOIN\r\n" +
				"         ACT_RU_EXECUTION  T1\r\n" +
				"         ON T.PROC_INST_ID_ = T1.PROC_INST_ID_ AND T1.PARENT_ID_ IS NULL\r\n" +
				"     ) TA\r\n" +
				"     left JOIN (\r\n" +
				"            select  id,custid,type,taskDate,url from (\r\n" +
				"select  id,custid,case state when 'COMPLETED' then '090' else state end as state,type,taskDate,url from (\r\n" +
				"select fd_id as id,FD_PROCESS_STATE as state,iss.FD_CUSTID as custid,'发债企业评级' as type,FD_CREATE_DATE as taskDate,'/irs/financialOrIssueBonds/issueBondsRatingAudit.html' as url  from NS_ISSUE_BONDS_DATA_RATING iss\r\n" +
				"union all\r\n" +
				"select fd_id as id,FD_PROCESS_STATE as state,inte.FD_CUSTID as custid,'金融同业评级'as type,FD_CREATE_DATE as taskDate ,'/irs/financialOrIssueBonds/financialRatingAudit.html' as url  from NS_INTERBANK_DATA_RATING inte\r\n" +
				"union all\r\n" +
				"select fd_id as id,FD_STATUS as state,nsd.FD_CUST_NO as custid,'违约认定'as type,FD_CREATE_DATE  as taskDate,decode(FD_STATUS,'TO_BE_CONFIRMED','/irs/defaultCustomerMan/defaultCustomerStart.html','/irs/defaultCustomerMan/defaultCustomerApprove.html')  as url from NS_DC_CUSTOMER nsd\r\n" +
				"union all\r\n" +
				"select fd_id as id,FD_STATUS as state,nsdx.FD_DEFAULT_CUST_NO as custid ,'违约重生'as type,FD_CREATE_DATE as taskDate,'/irs/rebirthDefaultCustomerMan/rebirthDefaultCustomerApprove.html' as url  from NS_DC_CUSTOMER_REBIRTH nsdx\r\n" +
				 "union all \n"
				+" select risk.contract_num as id,null as state,risk.ctm_num as custid,'风险暴露'as type,start_dt  as taskDate,'/irs/risk/approval.html' as url  from irs_risk_exposure risk \n" 
				+ "union all\r\n" +
				"select fd_id as id,FD_PROCESS_STATUS as state,nsc.FD_CUSTNO as custid,'客户评级'as type,FD_CREATE_DATE  as taskDate,'/irs/companyRating/ratingMain_task.html' as url  from NS_COMPANY_RATING nsc where nsc.fd_id not in (select fd_id from NS_COMPANY_RATING a where a.fd_modelcode = 'S3' and a.fd_process_status = '020' and a.fd_vaild ='1' ) )t)\r\n" +
				"     ) R\r\n" +
				"     ON  TA.BUSINESS_KEY_ = R.id \r\n" +
				"     left join  (\r\n" +
				"select fd_custno,fd_custname from Ns_Customer\r\n" +
				"union all\r\n" +
				"select fd_custno,fd_custname from NS_FINANCIAL_INDUSTRY\r\n" +
				"union all\r\n" +
				"select fd_custno,fd_custname from NS_ISSUE_BONDS"+
				/*"             union all\r\n" + 
				"             select fd_custno, fd_custname from NS_ISSUE_BONDS"+*/
				")cust  on cust.FD_CUSTNO=custid\r\n" +
				"     WHERE 1=1 and taskDate is not null order by  taskDate desc\r\n";
		List<Map<String, Object>> task = jdbc.queryForList(ArticleBeforeRow(sql,start,end) );
		if(task!=null&&task.size()!=0) {
			// 将总条数放到第一条记录中
			Map<String, Object> totalMap = jdbc.queryForMap(getCountSql(sql));
			task.get(0).put("total",totalMap.get("total"));
		}
		return checkData(task);
	}
	
	public List<Map<String, Object>>  checkData(List<Map<String, Object>>  data){
		for (Map<String, Object> map : data) {
			if(null!=map.get("CUSTID")) {
				String count="select count(*) from ESB_RATING_CUSTOMER where fd_custNo = ?";
				Integer num=jdbc.queryForObject(count, Integer.class,map.get("CUSTID"));
				if(num>0) {
					String sql="select *  from (select fd_custname from ESB_RATING_CUSTOMER where fd_custNo = ? order by FD_CREATE_DATE desc)t where rownum<=1";
					String name=jdbc.queryForObject(sql, String.class,map.get("CUSTID"));
					map.put("FD_CUSTNAME", name);
				}	
			}
		}
		return data;
	}
	
	
	public String ArticleBeforeRow(String sql,Integer start,Integer end) {
		if(start==null||end==null) {
			start=0;
			end=5;
		}
		 sql="select * from (select t.*,rownum as rn from ("+sql+") t where rownum<="+end+") where rn>"+start;
		return sql;
	}

	public String getCountSql(String sql){
		sql="select count(1) as total from ("+sql+") t ";
		return sql;
	}
	
	public Map<String, Object> queryDetails(String id) {
		List<Map<String, Object>> msg = jdbc.queryForList(ArticleBeforeRow(
				"select * from fr_sys_notification where FD_RECEIVER = ? and FD_ID=?",1,5), SecurityUtil.getLoginName(), id);
		return msg == null ? null : msg.get(0);
	}
	
	public List<Object> ratingFxfb() {
		List<Map<String, Object>> msg = jdbc
				.queryForList("select org2_name,profit_rate from NS_PROFIT_RATE order by input_date");
		List<Object> list = new ArrayList<Object>();
		List<String> orgName = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		for (Map<String, Object> map : msg) {
			orgName.add(map.get("ORG2_NAME").toString());
			Double value = Double.parseDouble(map.get("PROFIT_RATE").toString());
			/*
			 * value=value*100; DecimalFormat df = new DecimalFormat("######0.00");
			 */
			result.add(value.toString());
		}

		list.add(orgName);
		list.add(result);
		 List<String> fxfbCqsj = jdbc.queryForList("select INPUT_DATE from NS_PROFIT_RATE  ORDER BY INPUT_DATE DESC nulls last",String.class);
			if(fxfbCqsj.size()>0) {
				list.add(fxfbCqsj.get(0));
			}
		return list;
	}
	
	public String queryCustCode() throws Exception{
		List<String> roleCode = jdbc.queryForList("select fd_code from  fr_aa_role where fd_id in(\r\n" + 
				"				select fd_role_id from fr_aa_user_role where fd_user_id = (select fd_id from fr_aa_user where fd_loginname = ?)" + 
				"				)", String.class,SecurityUtil.getLoginName());
		// 查客户经理管辖的5大客户
		//查当前用户所在的机构
		List<String> orgCode = jdbc.queryForList("select fd_org_id from fr_aa_user_org where fd_user_id=(select fd_id from fr_aa_user where fd_loginname = ?)", String.class,SecurityUtil.getLoginName());
		
		String sqlQx="";

		//总行科技金融部人员
		String orgXqy=systemParameterServiceImpl.getParameter("org_id_jrxqy");
		String orgKjjr=systemParameterServiceImpl.getParameter("org_id_kjjrb");
		if(orgCode.contains(orgKjjr)) {
			sqlQx="select FD_CUSTNO from NS_CUSTOMER where FD_INPUT_ORGID='1007599' ";
		}else if(orgCode.contains(orgXqy)) {//总行小企业部人员
			sqlQx="select FD_CUSTNO from NS_CUSTOMER where FD_ENTERPRISE_SCALE='4'";
		}else {
			Integer count = jdbc.queryForObject("select count(*) from fr_aa_org where fd_code in(select fd_org_id from fr_aa_user_org where fd_user_id=? and fd_org_id not in ('"+orgXqy+"','"+orgKjjr+"')) and \r\n" + 
					" fd_parent_id = '1009999'",Integer.class,SecurityUtil.getLoginName());
			if(count>0||roleCode.contains("admin")) {
				sqlQx="select  FD_CUSTNO from NS_CUSTOMER";
			}
			count = jdbc.queryForObject("select count(*) from fr_aa_org where fd_code in(select fd_org_id from fr_aa_user_org where fd_user_id=? and fd_org_id <> '1009999')",Integer.class,SecurityUtil.getLoginName());
			if(count>0) {
				//查询
				sqlQx="select  FD_CUSTNO from NS_CUSTOMER where FD_INPUT_ORGID='"+orgCode.get(0)+"'";
				//FD_MANAGEMENT_INSTITUTIONS
			}
		}
		if(sqlQx.equals("")) {
			sqlQx="''";
		}
		return sqlQx;
		
	}
	
	public List<Map<String, Object>> queryAnnouncement() {
		
		List<Map<String, Object>> msg = jdbc.queryForList("select  rownum,t.* from( "+
				" select * from  fr_sys_announcement a "+
				" where not exists ( "+
				" select 1 from FR_SYS_ANNO_USER b where b.fd_a_id = a.fd_id  and b.fd_u_id=? "+
				" )) t where rownum<=10", SecurityUtil.getUserId());
		
		if(msg!=null&&msg.size()!=0) {
			// 将总条数放到第一条记录中
			msg.get(0).put("total",queryAnnoCnt());
		}
		return msg;
	}
	
	public List<Map<String, Object>> queryMsg() {
		List<Map<String, Object>> msg = jdbc.queryForList("select rownum,t.*  from (select * from fr_sys_notification where FD_RECEIVER = ? and fd_receive_date is null order by FD_SEND_DATE desc)t where rownum<=10",
				SecurityUtil.getLoginName());
		if(msg!=null&&msg.size()!=0) {
			// 将总条数放到第一条记录中
			msg.get(0).put("total",queryMsgCnt());
		}
		return msg;
	}
	
	public List<Map<String, Object>> getCustOreder(){
		List<Map<String, Object>> custOreder = new ArrayList<Map<String, Object>>();
		 try {
			custOreder = jdbc.queryForList(
						"select * from (select * from (select FD_CRDT_AMT,FD_CUSTNO,FD_UNCUSTNO,FD_CUSTNAME,fd_last_modifydate from NS_CUSTOMER c where c.FD_CUSTNO in"
						+ "(select a.FD_CUSTNO from NS_CUSTOMER a left join ns_company_rating r "
						+ "on r.fd_custno = a.FD_CUSTNO "
						+ " where a.FD_CUSTNO in ("+queryCustCode()+")"
         +" and r.fd_vaild=1  "
         + " AND R.FD_PROCESS_STATUS = '060' AND R.Fd_Rate_Status='010' AND R.Fd_Modelcode!='S3' "
         + " and r.fd_source ='IRB'   "
+ "  )  "
		+ " ORDER BY fd_last_modifydate desc nulls last) where rownum<=5)");
		}  catch (Exception e) {
			  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			
		}
		 return custOreder;
	}

	@Override
	public int queryMsgCnt() {
		Map<String, Object> totalMap = jdbc.queryForMap("select count(1) as total from fr_sys_notification where FD_RECEIVER = ? and fd_receive_date is  null ",SecurityUtil.getLoginName());
		return Integer.parseInt(totalMap.get("total").toString());
	}
	
	@Override
	public int queryAnnoCnt() {
		Map<String, Object> totalMap = jdbc.queryForMap("select count(1) as total from  fr_sys_announcement a "+
				" where not exists ( "+
				" select 1 from FR_SYS_ANNO_USER b where b.fd_a_id = a.fd_id  and b.fd_u_id=? "+
				") ", SecurityUtil.getUserId());
		return Integer.parseInt(totalMap.get("total").toString());
	}
}
  
