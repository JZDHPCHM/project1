package gbicc.irs.warning.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.service.FrDownloadFileService;
import com.gbicc.aicr.system.support.enums.DownloadFileEnum;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.warning.controller.AftWarnInfoController;
import gbicc.irs.warning.jpa.entity.AftWarnInfoEntity;
import gbicc.irs.warning.jpa.repository.AftWarnInfoRepository;
import gbicc.irs.warning.jpa.support.enums.WarnEnum;
import gbicc.irs.warning.jpa.vo.WarnResult;
import gbicc.irs.warning.service.AftWarnInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 预警信息表
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
@Service("AftWarnInfoServiceImpl")
public class AftWarnInfoServiceImpl extends DaoServiceImpl<AftWarnInfoEntity, String, AftWarnInfoRepository>
		implements AftWarnInfoService {
	private static Log log = LogFactory.getLog(AftWarnInfoController.class);

	@Autowired
	SystemParameterServiceImpl systemParameterServiceImpl;
	@Autowired
	public JdbcTemplate jdbc;

	@Autowired
	private SystemDictionaryService systemDictionaryService;
	
	@Autowired
	private FrDownloadFileService downloadFileService;

	@Override
	public Map<String, Object> findWarnCate(String parentCode) throws Exception {
		if (StringUtils.isBlank(parentCode)) {
			parentCode = WarnEnum.SYSTEM_DICTIONARY_WARN_CATE.getValue();
		}
		return AppUtil.getMap(true, systemDictionaryService.getDictionaryMap(parentCode, Locale.CHINA));
	}

	/**
	 * @预警规则名称
	 */
	@Override
	public Map<String, String> findRuleName() throws Exception {
		String sql = " select rule_code,Rule_name from t_aft_warn_rule";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map : list) {
			result.put(map.get("RULE_CODE").toString(), map.get("RULE_NAME").toString());
		}
		return result;
	}

	//查询客户
	@Override
	public Map<String, String> findCustName() throws Exception {
		String sql = " select LESSEE_ID,LESSEE_NAME from t_aft_atten_customer";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		Map<String, String> result = new HashMap<String, String>();
		for (Map<String, Object> map : list) {
			result.put(map.get("LESSEE_ID").toString(), map.get("LESSEE_NAME").toString());
		}
		return result;
	}
	//拼接sql
	public String sqlPar(String par, String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(par)) {
			sqlQuery = " and " + sqlCol + "='" + par + "'";
		}
		return sqlQuery;
	}
	/**
     * 模糊查询
     *
     * @param par
     * @param sqlCol
     * @return
     */
    public String sqlFuzzyPar(String par, String sqlCol) {
        String sqlQuery = "";
        if (StringUtils.isNotBlank(par)) {
            sqlQuery = " and " + sqlCol + " like '%" + par + "%'";
        }
        return sqlQuery;
    }
	public String sqlParDate(String date1,String date2,String sqlCol) {
		String sqlQuery = "";
		
		if (StringUtils.isNotBlank(date1)) {
			sqlQuery+= " and " + sqlCol + ">=to_date('" + date1 + "','yyyy-mm-dd')";
		}
		if (StringUtils.isNotBlank(date2)) {
		    date2 = formateDate(date2);
			sqlQuery+= " and " + sqlCol + "<to_date('" + date2 + "','yyyy-mm-dd')";
		}
		return sqlQuery;
	}
	
	/**
     * 格式化查询条件endtime，day+1
     *
     * @param args
     * @return
     */
    private String formateDate(String strDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String strDay = "";
            if(day<10) {
                strDay = "0" + day;
            }else {
                strDay = "" + day;
            }
            
            return year+"-"+month+"-"+strDay;
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }
	/**
	 * @查询高中预警数量
	 */
	@Override
	public List<Map<String,Object>> groupWarn(String waterID,Integer page, Integer rows, String custType, 
			String custname, String date1,
			String date2,String date3,String date4,String level, 
			String status, String warnRule, String warnSmalt, 
			String warnType,String dispResult,String assoName) {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlPar(waterID, "info.ID"));
		sqlQuery.append(sqlPar(custType, "info.ASSO_TYPE"));
		sqlQuery.append(sqlFuzzyPar(custname, "cust.LESSEE_NAME"));
		sqlQuery.append(sqlParDate(date2,date1, "info.WARN_TIME"));
		sqlQuery.append(sqlParDate(date4,date3, "info.DISP_TIME"));
		sqlQuery.append(sqlPar(level, "rule.WARN_LEVEL"));
		if(StringUtils.isNotBlank(status)) {
			if(status.equals("030")) {
				sqlQuery.append(sqlPar(status, "(info.PUSH_STATUS"));
				sqlQuery.append(" or info.PUSH_STATUS is null)");
			}else {
				sqlQuery.append(sqlPar(status, "info.PUSH_STATUS"));
			}
		}
		sqlQuery.append(sqlFuzzyPar(warnRule, "rule.RULE_Name"));
		sqlQuery.append(sqlPar(warnSmalt, "rule.RULE_SUB_TYPE"));
		sqlQuery.append(sqlPar(warnType, "rule.rule_TYPE"));
		sqlQuery.append(sqlPar(dispResult, "info.DISP_RESULT"));
		sqlQuery.append(sqlPar(assoName, "cust.asso_name"));
		String sql = "select WARN_LEVEL,count(WARN_LEVEL) as count\r\n" + 
				"  from T_AFT_WARN_INFO_DISTINCT info\r\n" + 
				" inner join t_aft_atten_customer cust\r\n" + 
				"   on info.LESSEE_ID = cust.LESSEE_ID\r\n" + 
				" inner join t_aft_warn_rule rule\r\n" + 
				"    on info.RULE_CODE = rule.RULE_CODE\r\n" + 
				"  where 1=1 " + sqlQuery+" group by WARN_LEVEL";
		
		List<Map<String,Object>> data = jdbc.queryForList(sql);
		return data;
	}

	/**
	 * @查询预警列表
	 */
	@Override
	public ResponseWrapper<WarnResult> warningList(String waterID,Integer page, Integer rows, String custType, 
			String custname, String date1,
			String date2,String date3,String date4,String level, 
			String status, String warnRule, String warnSmalt, 
			String warnType,String dispResult,String assoName,String highWarn,String medWarn) {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlPar(waterID, "info.ID"));
		sqlQuery.append(sqlPar(assoName, "cust.asso_name"));
		sqlQuery.append(sqlPar(custType, "info.ASSO_TYPE"));
		sqlQuery.append(sqlFuzzyPar(custname, "cust.LESSEE_NAME"));
		sqlQuery.append(sqlParDate(date2,date1, "info.WARN_TIME"));
		sqlQuery.append(sqlParDate(date4,date3, "info.DISP_TIME"));
		sqlQuery.append(sqlPar(level, "rule.WARN_LEVEL"));
		if(StringUtils.isNotBlank(status)) {
			if(status.equals("030")) {
				sqlQuery.append(sqlPar(status, "(info.PUSH_STATUS"));
				sqlQuery.append(" or info.PUSH_STATUS is null)");
			}else {
				sqlQuery.append(sqlPar(status, "info.PUSH_STATUS"));
			}
		}
		sqlQuery.append(sqlFuzzyPar(warnRule, "rule.RULE_Name"));
		sqlQuery.append(sqlFuzzyPar(warnRule, "rule.RULE_Name"));
		sqlQuery.append(sqlPar(warnSmalt, "rule.RULE_SUB_TYPE"));
		sqlQuery.append(sqlPar(warnType, "rule.rule_TYPE"));
		if(StringUtils.isNotBlank(dispResult)) {
			if(dispResult.equals("未处理")) {
				sqlQuery.append(sqlPar("010", "info.push_status"));
				sqlQuery.append(" and info.result_temp is null ");
			}else {
				sqlQuery.append(sqlPar(dispResult, "info.result_temp"));
				sqlQuery.append(" and  PUSH_STATUS ='010'");

				
			}
		}
		
		sqlQuery.append(sqlPar(highWarn, "rule.WARN_LEVEL"));
		sqlQuery.append(sqlPar(medWarn, "rule.WARN_LEVEL"));
		

		String sql = "select info.ID              as id,\r\n" + "       info.RULE_CODE       as ruleCode,\r\n"
				+ "       rule.RULE_Name       as ruleName,\r\n" + "       info.TASK_SEQNO      as taskSeqno,\r\n"
				+ "      cust.LESSEE_NAME as lesseeId,\r\n" + "      rule.WARN_LEVEL as warnSignLevel,\r\n"
				+ "      rule.rule_TYPE    as warnCateId,\r\n" + "      rule.RULE_SUB_TYPE     as warnSubId,\r\n"
				+ "      info.WARN_DESC       as warnDesc,\r\n" + "      cust.ASSO_TYPE       as assoType,\r\n"
				+ "      info.ASSO_ID         as assoId,\r\n" + "       to_char(info.WARN_TIME ,'yyyy-MM-dd')       as warnTime,\r\n"
				+ "       info.DISP_RESULT     as dispResult,\r\n" + "      info.DISP_TIME as dispTime,\r\n"
				+ "       info.BUSINESS_PROCESS     as businessProcess,\r\n" +"cust.asso_name         as assoName,"
				+ "       info.PUSH_STATUS       as pushStatus\r\n" + "  from T_AFT_WARN_INFO_DISTINCT info \r\n"
				+ "  inner join t_aft_atten_customer cust on info.LESSEE_ID = cust.LESSEE_ID and info.asso_type=cust.asso_type \r\n"
				+ "  inner join t_aft_warn_rule rule on info.RULE_CODE= rule.RULE_CODE where 1=1 " + sqlQuery;
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<WarnResult> list = jdbc.query(sqlpage, new BeanPropertyRowMapper<WarnResult>(WarnResult.class));
		ResponseWrapper<WarnResult> re = ResponseWrapperBuilder.query(list);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	public String warningList(Integer start, Integer end, String sql) {
		sql = "select * from (select t.*,rownum as rn from (" + sql + ") t where rownum<=" + end + ") where rn>"
				+ start;
		return sql;
	}
	/**
	 * @查询预警数量
	 */
	public Long count(String sql) {
		sql = "select count(*) from(" + sql + ")";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}

	/**
	 * @return
	 * @推送预警信息
	 */
	@Override
	public boolean warningByCode(String warnCode) {
		boolean flag = false;
		warnCode = warnCode.substring(0, warnCode.length() - 1);
		warnCode = warnCode.replace(",", "','");
		String sql = "select info.ID as ID, info.RULE_CODE       as ruleCode,\r\n" + 
				"       info.lessee_id as lesseeId,\r\n" + 
				"       info.ASSO_ID as assoId,\r\n" + 
				"        info.ASSO_TYPE as  assoType,\r\n" + 
				"       rule.WARN_LEVEL as warnSignLevel,\r\n" + 
				"       info.WARN_TIME as warnTime,\r\n" + 
				"       rule.RULE_TYPE    as warnCateId,\r\n" + 
				"       rule.RULE_SUB_TYPE     as warnSubId,\r\n" + 
				"       info.WARN_DESC       as warnDesc from T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "  left join T_AFT_WARN_RULE rule\r\n" + "    on info.RULE_CODE = rule.RULE_CODE where info.id in('"
				+ warnCode + "') ";

		List<WarnResult> list = jdbc.query(sql,
				new BeanPropertyRowMapper<WarnResult>(WarnResult.class));
		JSONArray json = JSONArray.fromObject(list);
		Map<String, String> map = new HashMap<String, String>();
		map.put("data", json.toString());
		map.put("flag", "010");
		map.put("type", "010");
		try { // 192.168.5.71
			//
			String ip = systemParameterServiceImpl.getParameter("ESB_IP");
			String dk = systemParameterServiceImpl.getParameter("ESB_PORT");
			String endRe = sendBySocket(map, ip, Integer.parseInt(dk));
			if(endRe.equals("false")) {
				flag=false;
			}else {
				 JSONObject result ;
				 result = JSONObject.fromObject(endRe);
				 if(result.get("flag").equals("010")) {
					 flag=true;
				 }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(flag) {
			String sqlStatus = "update T_AFT_WARN_INFO_DISTINCT set PUSH_STATUS='010' where id in('" + 
					 warnCode + "')";
			jdbc.update(sqlStatus);
		}else {
			String sqlStatus = "update T_AFT_WARN_INFO_DISTINCT set PUSH_STATUS='020' where id in('" + 
					 warnCode + "')";
			jdbc.update(sqlStatus);
		}
		return flag;
	}

	@Override
	public Map<String, String> updateResult(Map<String, Object> warn) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "update T_AFT_WARN_INFO_DISTINCT set DISP_RESULT=? ,DISP_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),BUSINESS_PROCESS=?,RESULT_TEMP=? where id=?";
		try {
			jdbc.update(sql, warn.get("dispositionResult").toString(), warn.get("dispositionDate").toString(),warn.get("approver"),warn.get("Result"),warn.get("id").toString());
			map.put("flag","010");
			map.put("msg", "成功！");
		} catch (Exception e) {
			log.info(e.getMessage());
			map.put("flag", "020");
			map.put("msg", e.getMessage());
			throw new Exception("预警结果保存失败！");
		}
		return map;
	}

	public static String sendBySocket(Map<String, String> map, String ip, int port) throws Exception {

		log.info("开始建立socket连接");
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			socket.setSoTimeout(70000);
		} catch (ConnectException e) {
			log.info("socket连接失败！");
			return "false";
		}

		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "GBK");
		// 2.获取客户端输出流
		OutputStream dos = socket.getOutputStream();

		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		log.info("连上服务端");
		os.writeObject(map);
		dos.flush();
		log.info("成功向服务器发送消息");
		BufferedReader br = null;
		String returnInfo = "";
		try {
			ObjectInputStream js = new ObjectInputStream(is);
			Object obj = js.readObject();
			JSONObject json = JSONObject.fromObject(obj);
			returnInfo = json.toString();
			// 4.获取输入流，并读取服务器端的响应信息
			/*try {
				br = new BufferedReader(isr);
				returnInfo = br.readLine();
				System.out.println(returnInfo);
			} finally{
				br.close();
			}*/
			log.info("服务器端返回数据为：" + json);
		} catch (Exception e) {
			e.printStackTrace();
			//log.info("服务器端返回数据异常！");
		} finally {
			
			isr.close();
			is.close();
			dos.close();
			socket.close();
		}
		return  returnInfo;
	}

	/**
	 *@查询主承租人所关联的关联人
	 */
	@Override
	public ResponseWrapper<Map<String,Object>> queryAllLessee(Integer page,Integer rows,String custName,String date1,String date2) throws Exception {
		//where asso_company_id='911101157650005557'
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlFuzzyPar(custName, "LESSEE_NAME"));
		sqlQuery.append(sqlParDate(date1,date2, "DATA_DT"));
		
		String sql = "select LESSEE_COMPANY_ID,LESSEE_NAME from t_aft_atten_customer where 1=1 "+sqlQuery
				+ "group by LESSEE_COMPANY_ID,lessee_name";
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String,Object>> list = jdbc.queryForList(sqlpage);	
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String,Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	@Override
	public ResponseWrapper<Map<String,Object>> findByAssoidN(String lesseeId,Integer page,Integer rows) throws Exception {
		String sql = "select ASSO_COMPANY_ID,ASSO_NAME,ASSO_TYPE_NAME from t_aft_atten_customer where (IS_ATTEN='N' or  IS_ATTEN is null) and LESSEE_COMPANY_ID='"+lesseeId+"'";
		//String sql = "select asso_company_ID,ASSO_NAME from t_aft_atten_customer  group by asso_company_ID,ASSO_NAME";
		
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String,Object>> list = jdbc.queryForList(sqlpage);	
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String,Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}
	
	
	@Override
	public ResponseWrapper<Map<String,Object>> findByAssoidY(String lesseeId,Integer page,Integer rows) throws Exception {
		String sql = "select ASSO_COMPANY_ID,ASSO_NAME,ASSO_TYPE_NAME from t_aft_atten_customer where IS_ATTEN='Y' and LESSEE_COMPANY_ID='"+lesseeId+"'";
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String,Object>> list = jdbc.queryForList(sqlpage);	
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String,Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}
	
	
	
	
	
	
	@Override
	public boolean changeFocusOn(String status,String assoID) throws Exception {
		String sql="update t_aft_atten_customer set IS_ATTEN=? where asso_company_id = ?";
		if(jdbc.update(sql,status,assoID)>0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * @抽取最新关注客户数据
	 * @throws Exception
	 */
	public void callWarnCust()
			throws Exception {
		try {
			String procedure = "{call PROC_ZGCZL_MBWH()}";
			jdbc.execute(procedure);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @汉德关注信息状态修改
	 */
	@Override
	public Map<String, Object> changeFocusOnInter(Map<String,Object> map) throws Exception {
		callWarnCust();
		String sql="update t_aft_atten_customer set IS_ATTEN=?"
				+ ",TO_WAY=?,TO_WAY_TIME=?,TO_WAY_USER=? where LESSEE_COMPANY_ID = ?";
		String type = "";
		  String url = getRequestUrl(map.get("regNumber").toString());
		  Map<String,Object> result = new HashMap<String,Object>();
		  boolean fbgx= false;
		  if(map.get("followStatus").toString().equals("Y")) {
			  fbgx = FbHttpUtil.requestPut(url);
		  }else {
			  fbgx = FbHttpUtil.requestDelete(url);
		  }
		  if(fbgx==false) {
			result.put("flag", "020");
			result.put("msg", "操作失败！"); 
			return result;
		  }
		if(map.get("followWays").toString().equals("0")) {
			type="接口关注";
		}else {
			type="手动关注";
		}
		if(jdbc.update(sql,map.get("followStatus"),type,map.get("followTime"),map.get("userId"),map.get("regNumber"))>0) {
			result.put("flag", "010");
			return result;
		}else {
			result.put("flag", "020");
			result.put("msg", "关注失败");
			return result;
		}
	}
	
	@Override
    public Map<String, Object> queryHistoryCount(String custname, String custType, String level, String warnRule,
            String warnType, String warnSmalt, String employee, String dispResult, String assoName, String date4,
            String date3, String date2, String date1,String highWarn,String medWarn) throws Exception {
	    
	    String sql = getExportHistorySql(custname, custType, level, warnRule, warnType, warnSmalt, employee, dispResult, assoName, date4, date3, date2, date1,highWarn,medWarn);
	    Long count = count(sql);
	    if(count==null || count<=0) {
	        return AppUtil.getMap(false,"该查询条件无结果\r\n请确认后操作");
	    }else {
	        return AppUtil.getMap(true);
	    }
    }

	/**
	 * 拼接导出sql语句
	 *
	 * @param custname
	 * @param custType
	 * @param level
	 * @param warnRule
	 * @param warnType
	 * @param warnSmalt
	 * @param employee
	 * @param dispResult
	 * @param assoName
	 * @param date4
	 * @param date3
	 * @param date2
	 * @param date1
	 * @return
	 */
    private String getExportHistorySql(String custname, String custType, String level, String warnRule, String warnType,
            String warnSmalt, String employee, String dispResult, String assoName, String date4, String date3,
            String date2, String date1,String highWarn,String medWarn) {
        
        StringBuffer sqlQuery = new StringBuffer();
        sqlQuery.append(sqlPar(custType, "info.ASSO_TYPE"));
        sqlQuery.append(sqlFuzzyPar(custname, "cust.LESSEE_NAME"));
        sqlQuery.append(sqlParDate(date2,date1, "info.WARN_TIME"));
        sqlQuery.append(sqlParDate(date4,date3, "info.DISP_TIME"));
        sqlQuery.append(sqlPar(level, "rule.WARN_LEVEL"));
        sqlQuery.append(sqlFuzzyPar(warnRule, "rule.RULE_Name"));
        sqlQuery.append(sqlPar(warnSmalt, "rule.RULE_SUB_TYPE"));
        sqlQuery.append(sqlPar(warnType, "rule.rule_TYPE"));
        sqlQuery.append(sqlPar(dispResult, "info.DISP_RESULT"));
        sqlQuery.append(sqlPar(assoName, "cust.asso_name"));
        sqlQuery.append(sqlPar(employee,"info.BUSINESS_PROCESS"));
        sqlQuery.append(sqlPar(highWarn, "rule.WARN_LEVEL"));
		sqlQuery.append(sqlPar(medWarn, "rule.WARN_LEVEL"));
		
        
        String sql = "select \r\n" + 
                "cust.LESSEE_NAME as \"承租人名称\",\r\n" + 
                "msg1.fd_message as \"关联人类型\",\r\n" + 
                "info.RULE_CODE as \"规则编码\",\r\n" + 
                "msg5.fd_message as \"预警级别\",\r\n" + 
                "msg2.fd_message as \"预警类别\",\r\n" + 
                "msg3.fd_message as \"预警小类\",\r\n" + 
                "rule.RULE_Name as \"预警规则\",\r\n" + 
                "info.WARN_DESC as \"预警详情\",\r\n" + 
                "to_char(info.WARN_TIME,'yyyy-MM-dd HH24:mi:ss') as \"预警时间\",\r\n" + 
                "info.BUSINESS_PROCESS as \"当前审批人\",\r\n" + 
                "info.DISP_RESULT as \"处置状态\",\r\n" + 
                "to_char(info.DISP_TIME,'yyyy-MM-dd HH24:mi:ss') as \"处置日期\",\r\n" + 
                "cust.asso_name as \"关联人名称\"\r\n" + 
                "  from T_AFT_WARN_INFO_DISTINCT info \r\n" + 
                "  inner join t_aft_atten_customer cust on info.LESSEE_ID = cust.LESSEE_ID \r\n" + 
                "  inner join t_aft_warn_rule rule on info.RULE_CODE= rule.RULE_CODE\r\n" + 
                "  inner join fr_sys_i18n_message msg1 on replace(msg1.fd_code,'dictionary.associates.','')=cust.asso_type\r\n" + 
                "  inner join fr_sys_i18n_message msg2 on replace(msg2.fd_code,'dictionary.WARN_CATE.','')=rule.rule_type\r\n" + 
                "  inner join fr_sys_i18n_message msg3 on replace(msg3.fd_code,'dictionary.'||rule.rule_type||'.','')=rule.rule_sub_type\r\n" + 
                "  inner join fr_sys_i18n_message msg5 on replace(msg5.fd_code,'dictionary.RULE_WARN_LEVEL.','')=rule.WARN_LEVEL\r\n" + 
                " where 1=1 " + sqlQuery;
        return sql;
    }

    @Override
    public Map<String, Object> exportHistoryData(String custname, String custType, String level, String warnRule, String warnType,
            String warnSmalt, String employee, String dispResult, String assoName, String date4, String date3, String date2, String date1,
            String fileName,String highWarn,String medWarn) throws Exception{
    	 String loginName = SecurityUtil.getLoginName();
        String sql = getExportHistorySql(custname,custType,level,warnRule,warnType,warnSmalt,employee,dispResult,assoName,date4,date3,date2,date1,highWarn,medWarn);
        if(fileName.endsWith(DownloadFileEnum.EXCEL.getValue())) {
            fileName = fileName.substring(0,fileName.lastIndexOf(DownloadFileEnum.EXCEL.getValue()));
        }
        downloadFileService.exportDataToExcel(loginName,sql, fileName+DownloadFileEnum.EXCEL.getValue());
        return AppUtil.getMap(true);
    }

    @Override
    public Map<String, Object> queryCount(String waterId, String custname, String warnType, String warnSmalt,
            String level, String pushStatus, String custType, String warnRule, String date2, String date1,String highWarn,String medWarn) {
        
        String sql = getExportSql(waterId,custname,warnType,warnSmalt,level,pushStatus,custType,warnRule,date2,date1,highWarn,medWarn);
        Long count = count(sql);
        if(count==null || count<=0) {
            return AppUtil.getMap(false,"该查询条件无结果\r\n请确认后操作");
        }else {
            return AppUtil.getMap(true);
        }
    }

    private String getExportSql(String waterId, String custname, String warnType, String warnSmalt, String level,
            String pushStatus, String custType, String warnRule, String date2,String date1,String highWarn,String medWarn) {

        StringBuffer sqlQuery = new StringBuffer();
        sqlQuery.append(sqlPar(waterId, "info.ID"));
        sqlQuery.append(sqlPar(custType, "info.ASSO_TYPE"));
        sqlQuery.append(sqlFuzzyPar(custname, "cust.LESSEE_NAME"));
        sqlQuery.append(sqlParDate(date2,date1, "info.WARN_TIME"));
        sqlQuery.append(sqlPar(level, "rule.WARN_LEVEL"));
        sqlQuery.append(sqlFuzzyPar(warnRule, "rule.RULE_Name"));
        sqlQuery.append(sqlPar(warnSmalt, "rule.RULE_SUB_TYPE"));
        sqlQuery.append(sqlPar(warnType, "rule.rule_TYPE"));
        sqlQuery.append(sqlPar(highWarn, "rule.WARN_LEVEL"));
		sqlQuery.append(sqlPar(medWarn, "rule.WARN_LEVEL"));
		
        if(StringUtils.isNotBlank(pushStatus)) {
            if(pushStatus.equals("030")) {
                sqlQuery.append(sqlPar(pushStatus, "(info.PUSH_STATUS"));
                sqlQuery.append(" or info.PUSH_STATUS is null)");
            }else {
                sqlQuery.append(sqlPar(pushStatus, "info.PUSH_STATUS"));
            }
        }
        String sql = "select \r\n" + 
                "info.id as \"评级流水号\",\r\n" + 
                "cust.LESSEE_NAME as \"承租人名称\",\r\n" + 
                "msg1.fd_message as \"关联人类型\",\r\n" + 
                "info.RULE_CODE as \"规则编码\",\r\n" + 
                "msg5.fd_message as \"预警级别\",\r\n" + 
                "msg2.fd_message as \"预警类别\",\r\n" + 
                "msg3.fd_message as \"预警小类\",\r\n" + 
                "rule.RULE_Name as \"预警规则\",\r\n" + 
                "info.WARN_DESC as \"预警详情\",\r\n" + 
                "to_char(info.WARN_TIME,'yyyy-MM-dd HH24:mi:ss') as \"预警时间\",\r\n" +
                "case when info.push_status is null then '未推送' else msg4.fd_message end as \"推送状态\"\r\n" +
                "  from T_AFT_WARN_INFO_DISTINCT info \r\n" + 
                "  inner join t_aft_atten_customer cust on info.LESSEE_ID = cust.LESSEE_ID \r\n" + 
                "  inner join t_aft_warn_rule rule on info.RULE_CODE= rule.RULE_CODE\r\n" + 
                "  inner join fr_sys_i18n_message msg1 on replace(msg1.fd_code,'dictionary.associates.','')=cust.asso_type\r\n" + 
                "  inner join fr_sys_i18n_message msg2 on replace(msg2.fd_code,'dictionary.WARN_CATE.','')=rule.rule_type\r\n" + 
                "  inner join fr_sys_i18n_message msg3 on replace(msg3.fd_code,'dictionary.'||rule.rule_type||'.','')=rule.rule_sub_type\r\n" +
                "  left join fr_sys_i18n_message msg4 on replace(msg4.fd_code,'dictionary.PushState.','')=info.PUSH_STATUS\r\n" + 
                "  inner join fr_sys_i18n_message msg5 on replace(msg5.fd_code,'dictionary.RULE_WARN_LEVEL.','')=rule.WARN_LEVEL\r\n" + 
                " where 1=1 " + sqlQuery;
        return sql;
    }

    @Override
    public Map<String, Object> exportData(String waterId, String custname, String warnType, String warnSmalt,
            String level, String pushStatus, String custType, String warnRule, String date2, String date1,
            String fileName,String highWarn,String medWarn) {
        String sql = getExportSql(waterId, custname, warnType, warnSmalt, level, pushStatus, custType, warnRule, date2, date1,highWarn,medWarn);
        if(fileName.endsWith(DownloadFileEnum.EXCEL.getValue())) {
            fileName = fileName.substring(0,fileName.lastIndexOf(DownloadFileEnum.EXCEL.getValue()));
        }	 String loginName = SecurityUtil.getLoginName();
        downloadFileService.exportDataToExcel(loginName,sql, fileName+DownloadFileEnum.EXCEL.getValue());
        return AppUtil.getMap(true);
    }

	
    private String getRequestUrl(String companyId) throws Exception{
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        String baseUrl = dictionaryMap.get(FbInterfaceEnums.FOLLOWING_FEED.getValue());
        baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf(FbInterfaceEnums.SEPRATOR.getValue())+1);
        String url = baseUrl+companyId.trim()+FbInterfaceEnums.APIKEY_SUFFIX.getValue()+dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue());
        return url;
    }
	
}
