package gbicc.irs.risk.exposure.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.flowable.controller.support.ProcessProperties;
import org.wsp.framework.flowable.service.ProcessEntityService;
import org.wsp.framework.flowable.service.ProcessOperationService;
import org.wsp.framework.flowable.service.impl.AbstractFlowableDaoServiceImpl;
import org.wsp.framework.jdbc.sql.dialect.Dialect;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;

import gbicc.irs.risk.exposure.Wrapper.RiskWrapper;
import gbicc.irs.risk.exposure.entity.RiskApplyForLogEntity;
import gbicc.irs.risk.exposure.entity.RiskEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskRepository;
import gbicc.irs.risk.exposure.service.RiskApplyForLogService;
import gbicc.irs.risk.exposure.service.RiskService;
import gbicc.irs.risk.exposure.support.DefaultRiskConstants;
import gbicc.irs.risk.exposure.support.DefaultRiskProcessStatus;

@Service("RiskService")
public class RiskServiceImpl  extends AbstractFlowableDaoServiceImpl<RiskEntity, String,RiskRepository> implements RiskService {
	//查询排序
	private static Map<String,String> ratinTaskOrderFieldNameMap =new HashMap<String,String>();
	@Autowired
	private ProcessOperationService processOperationService;
	@Autowired
	private ProcessEntityService  processEntityService;
	@Autowired 
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RiskApplyForLogService applyForLogService;
	@Autowired
	private UserService userService;

	
	static{
		ratinTaskOrderFieldNameMap.put("contractNum", "CONTRACT_NUM");
		ratinTaskOrderFieldNameMap.put("ctmNum", "CTM_NUM");
		ratinTaskOrderFieldNameMap.put("ctmName", "CTN_NAME");
		ratinTaskOrderFieldNameMap.put("scale", "SCALE");
		ratinTaskOrderFieldNameMap.put("regulatingMethods", "REGULATING_METHODS");
		ratinTaskOrderFieldNameMap.put("processState", "PROCESS_STATE");
	}
	@Autowired 
	private Dialect dialect;
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	@Override
	public Page<RiskWrapper> fetchRisk(RiskWrapper riskWrapper, Pageable pageable) throws Exception {
		String sql="SELECT"  
				+"	ire.CONTRACT_NUM as contractNum,"
				+"  ire.CONTRACT_SUM as contractSum,"
				+"  ire.PRODUCT_TYPE as productType,"
				+"	ire.LOANS_TO as loansTo,"
				+"  ire.MAIN_GUARANTEE_METHOD as mainGuaranteeMethod,"
				+"	ire.CTM_NUM as ctmNum,"
				+"	ire.CTM_NAME as ctmName,"
				+"	ire.SYSTEM_DECISION_RESULT as systemDecisionResult,"
				+"	ire.MANUAL_DECISION_RESULT as manualDecisionResult,"
				+"	ire.FINAL_DECISION as finalDecision,"
				+"	ire.SYSTEM_DECISION_DATE as systemDecisionDate,"
				+"	ire.START_DATE as startDate,"
				+"	ire.PROCESS_STATE as processState,"
				+"	decode(ire.CURRENT_TASK_TERSON,'null',ire.FD_LAST_MODIFIER,'',ire.FD_LAST_MODIFIER,ire.CURRENT_TASK_TERSON) as currentTaskTerson,"  //FD_LAST_MODIFIER
				+"	ire.FINSH_DATE as finshDate,"
				+"	ire.REGULATING_METHODS as regulatingMethods,"
				+"	ire.START_USER as startUser,"   
				+"	ire.FD_INPUT_ORGID as agency,"   
				+"	ire.BALANCE as balance,"   
				+"	ire.CURRENCY as currency,"   
				+"	ire.START_DT as startDt,"   
				+"	ire.END_DT as endDt,"   
				+"	ire.BUSINESS_TYPE as occurrenceType,"   
				+"	nc.FD_ENTERPRISE_SCALE as scale,"
				+"	nc.FD_CTM_TYPE as ctmType,"
				+"	nc.FD_ANNUAL_INCOME as taking,"
				+"	nc.FD_TOTAL_ASSETS as totalAssets,"
				+" 	PROCESS.Id_ as taskId,"
				+"  PROCESS.Assignee_ as assigneeId,"
				+"  nc.FD_GB_INDUSTRY as gbIndustry  "
				+" FROM"
				+"	irs_risk_exposure ire "
				+" LEFT JOIN ns_customer nc on nc.FD_CUSTNO = ire.CTM_NUM"
				+" LEFT JOIN ("
				+"	SELECT"
				+"		TASK.Id_,"
				+"		TASK.Name_,"
				+"		TASK.Proc_Inst_Id_,"
				+"		TASK.Assignee_,"
				+"		EXECUTION.Business_Key_"
				+"	FROM"
				+"		ACT_RU_TASK TASK,"
				+"		ACT_RU_EXECUTION EXECUTION"
				+"	WHERE"
				+"		TASK.PROC_INST_ID_ = EXECUTION.Proc_Inst_Id_"
				+"	AND EXECUTION.Parent_Id_ IS NULL"
				+") PROCESS ON PROCESS.BUSINESS_KEY_ = ire.CONTRACT_NUM"
				+" LEFT JOIN fr_aa_user t6 ON t6.FD_LOGINNAME = PROCESS.Assignee_"
				+" WHERE 1=1 ";
	
		if("CUSTOMER_MANAGER".equals(SecurityUtil.getDefaultRoleCode().trim())){//		
			sql+=" and  nc.FD_MANAGER_CODE = '"+SecurityUtil.getLoginName().trim()+"'";
			
		}
		if(!"CUSTOMER_MANAGER".equals(SecurityUtil.getDefaultRoleCode().trim())&&
				!"QUERY_POST".equals(SecurityUtil.getDefaultRoleCode().trim())){
			List<String> orgcode = jdbcTemplate.queryForList("select fd_org_id from fr_aa_user_org where fd_user_id = '" + SecurityUtil.getUserId() + "'" ,String.class);
			if(orgcode.size()!=0)
				sql+= " and nc.fd_input_orgid = '" +  orgcode.get(0) + "'";
		}
		sql=createCondition(riskWrapper,sql);
		String orderBy =getOrderSql(pageable);
		
		if(StringUtils.hasText(orderBy)){
			sql =sql + " " + orderBy;
		}
		String countSql= "select count(1) " +"   from (" + sql + ")";
		Long size=jdbcTemplate.queryForObject(countSql, Long.class);
		sql =dialect.limit(sql, pageable.getPageNumber()+1, pageable.getPageSize());
		List<RiskWrapper> rs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RiskWrapper>(RiskWrapper.class));
		for(RiskWrapper bean:rs) {
			if(!StringUtils.isEmpty(bean.getStartDate())) {
				bean.setStartDate(bean.getStartDate().substring(0, bean.getStartDate().indexOf(".")));
			}
			if(!StringUtils.isEmpty(bean.getFinshDate())) {
				bean.setFinshDate(bean.getFinshDate().substring(0, bean.getFinshDate().indexOf(".")));
			}
		}
		return new PageImpl<RiskWrapper>(rs,pageable,size);
	}
	@Override
	public Page<RiskWrapper> fetchEnableRisk(RiskWrapper riskWrapper, Pageable pageable) throws Exception {
		String sql="SELECT"
				+"	ire.CONTRACT_NUM as contractNum,"
				+"  ire.CONTRACT_SUM as contractSum,"
				+"  ire.PRODUCT_TYPE as productType,"
				+"	ire.LOANS_TO as loansTo,"
				+"  ire.MAIN_GUARANTEE_METHOD as mainGuaranteeMethod,"
				+"	ire.CTM_NUM as ctmNum,"
				+"	ire.CTM_NAME as ctmName,"
				+"	ire.SYSTEM_DECISION_RESULT as systemDecisionResult,"
				+"	ire.MANUAL_DECISION_RESULT as manualDecisionResult,"
				+"	ire.FINAL_DECISION as finalDecision,"
				+"	ire.system_decision_date as systemDecisionDate,"
				+"	ire.START_DATE as startDate,"
				+"	ire.PROCESS_STATE as processState,"
				+"	decode(ire.CURRENT_TASK_TERSON,'null',ire.FD_LAST_MODIFIER,'',ire.FD_LAST_MODIFIER,ire.CURRENT_TASK_TERSON) as currentTaskTerson,"  //FD_LAST_MODIFIER
				+"	ire.FINSH_DATE as finshDate,"
				+"	ire.REGULATING_METHODS as regulatingMethods,"
				+"	ire.START_USER as startUser,"
				+"	ire.FD_INPUT_ORGID as agency,"   
				+"	ire.BALANCE as balance,"   
				+"	ire.CURRENCY as currency,"   
				+"	ire.START_DT as startDt,"   
				+"	ire.END_DT as endDt,"   
				+"	ire.BUSINESS_TYPE as occurrenceType,"   
				+"	nc.FD_ENTERPRISE_SCALE as scale,"
				+"	nc.FD_CTM_TYPE as ctmType,"
				+"	nc.FD_ANNUAL_INCOME as taking,"
				+"	nc.FD_TOTAL_ASSETS as totalAssets,"
				+" 	PROCESS.Id_ as taskId,"
				+"  PROCESS.Assignee_ as assigneeId,"
				+"  nc.FD_GB_INDUSTRY as gbIndustry  "
				+" FROM"
				+"	irs_risk_exposure ire "
				+" LEFT JOIN ns_customer nc on nc.FD_CUSTNO = ire.CTM_NUM"
				+" LEFT JOIN ("
				+"	SELECT"
				+"		TASK.Id_,"
				+"		TASK.Name_,"
				+"		TASK.Proc_Inst_Id_,"
				+"		TASK.Assignee_,"
				+"		EXECUTION.Business_Key_"
				+"	FROM"
				+"		ACT_RU_TASK TASK,"
				+"		ACT_RU_EXECUTION EXECUTION"
				+"	WHERE"
				+"		TASK.PROC_INST_ID_ = EXECUTION.Proc_Inst_Id_"
				+"	AND EXECUTION.Parent_Id_ IS NULL"
				+") PROCESS ON PROCESS.BUSINESS_KEY_ = ire.CONTRACT_NUM"
				+" LEFT JOIN fr_aa_user t6 ON t6.FD_LOGINNAME = PROCESS.Assignee_"
				+" WHERE 1=1  ";
	
		if("CUSTOMER_MANAGER".equals(SecurityUtil.getDefaultRoleCode().trim())){//		
			sql+=" and  nc.FD_MANAGER_CODE = '"+SecurityUtil.getLoginName().trim()+"'";
			
		}

		sql=createCondition(riskWrapper,sql);
		sql =sql + " order by startDate desc ";

//		String orderBy =getOrderSql(pageable);
//		
//		if(StringUtils.hasText(orderBy)){
//			sql =sql + " " + orderBy;
//		}
		String countSql= "select count(1) " +"   from (" + sql + ")";
		Long size=jdbcTemplate.queryForObject(countSql, Long.class);
		sql =dialect.limit(sql, pageable.getPageNumber()+1, pageable.getPageSize());
		List<RiskWrapper> rs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RiskWrapper>(RiskWrapper.class));
		for(RiskWrapper bean:rs) {
			if(!StringUtils.isEmpty(bean.getStartDate())) {
				bean.setStartDate(bean.getStartDate().substring(0, bean.getStartDate().indexOf(".")));
			}
			if(!StringUtils.isEmpty(bean.getFinshDate())) {
				bean.setFinshDate(bean.getFinshDate().substring(0, bean.getFinshDate().indexOf(".")));
			}
		}
		return new PageImpl<RiskWrapper>(rs,pageable,size);
	}
	private String createCondition(RiskWrapper riskWrapper,String sql){
		if(!StringUtils.isEmpty(riskWrapper.getContractNum())){
			sql+=" and ire.CONTRACT_NUM LIKE  CONCAT(CONCAT('%','"+riskWrapper.getContractNum()+"'),'%')";
		}
		if(!StringUtils.isEmpty(riskWrapper.getGbIndustry())){
			sql+=" and nc.FD_GB_INDUSTRY='"+riskWrapper.getGbIndustry()+"'";
		}
		if(!StringUtils.isEmpty(riskWrapper.getCtmName())){
			sql+=" and ire.CTM_NAME LIKE  CONCAT(CONCAT('%','"+riskWrapper.getCtmName()+"'),'%')";
		}
		if(!StringUtils.isEmpty(riskWrapper.getCtmNum())){
			sql+=" and ire.CTM_NUM LIKE  CONCAT(CONCAT('%','"+riskWrapper.getCtmNum()+"'),'%')";
		}
		if(!StringUtils.isEmpty(riskWrapper.getScale())){
			sql+=" and nc.FD_ENTERPRISE_SCALE='"+riskWrapper.getScale()+"'";
		}
		if(!StringUtils.isEmpty(riskWrapper.getProcessState())){
			sql+=" and ire.PROCESS_STATE='"+riskWrapper.getProcessState()+"'";
		}
		if(!StringUtils.isEmpty(riskWrapper.getRegulatingMethods())){
			sql+=" and ire.REGULATING_METHODS='"+riskWrapper.getRegulatingMethods()+"'";
		}
		if(!StringUtils.isEmpty(riskWrapper.getRiskFinalType())){
			sql+=" and ire.final_decision='"+riskWrapper.getRiskFinalType()+"'";
		}
		return sql;
	}
	

	private String getOrderSql(Pageable pageable){
		if(pageable==null){
			return null;
		}
		Sort sort =pageable.getSort();
		if(sort!=null){
			Iterator<Order> orders =sort.iterator();
			if(orders.hasNext()){
				List<String> orderFields =new ArrayList<String>();
				while(orders.hasNext()){
					Order order =orders.next();
					String orderFieldName =ratinTaskOrderFieldNameMap.get(order.getProperty());
					if(StringUtils.hasText(orderFieldName)){
						boolean isDescending =order.isDescending();
						if(isDescending){
							orderFields.add(orderFieldName + " desc");
						}else{
							orderFields.add(orderFieldName);
						}
					}
				}
				if(orderFields.size()>0){
					return "order by " + Joiner.on(",").skipNulls().join(orderFields);
				}
			}
		}
		return "order by finshDate desc";
	}
	@Transactional
	public void applyfor(ProcessProperties properties) throws Exception{
		Map<String,Object> variables = properties.getVariables();//持久化变量
		Map<String,Object> transientVariables = properties.getTransientVariables();//临时变量
		String defaultId = transientVariables.get("defaultId").toString();
		RiskEntity  riskEntity=repository.getOne(defaultId);
		if(transientVariables.containsKey("taskId") && !StringUtils.isEmpty(transientVariables.get("taskId"))) { //判断是否为退回任务
			super.completeTask(transientVariables.get("taskId").toString(),variables,transientVariables);
		}else {
			String deployedKey = processEntityService.getEngineDeployedKey(DefaultRiskConstants.PWY);
			super.startProcessByKey(deployedKey, defaultId, variables, transientVariables, properties.getAutoCompleteFirstTask());
		}
		riskEntity.setProcessState(DefaultRiskProcessStatus.IN_APPROVAL);
		riskEntity.setStartDate(new Date());
		riskEntity.setStartUser(SecurityUtil.getLoginName().trim());
		riskEntity.setCurrentTaskTerson(String.valueOf(transientVariables.get("assignee")));
		repository.save(riskEntity);
		RiskApplyForLogEntity applyForLogEntity=new RiskApplyForLogEntity();
		applyForLogEntity.setRiskExposureId(defaultId);
		applyForLogEntity.setMaintainResult(String.valueOf(transientVariables.get("manualDecisionResult")));
		applyForLogEntity.setReason(String.valueOf(transientVariables.get("reason")));
		applyForLogEntity.setMaintainDt(new Date());
		applyForLogEntity.setOperate("提交");
		applyForLogEntity.setRoleName(SecurityUtil.getDefaultRoleName());
		applyForLogEntity.setProcessor(String.valueOf(transientVariables.get("assignee")));//String.valueOf(transientVariables.get("assignee"))
		applyForLogService.add(applyForLogEntity);
	}

	@Override
	@Transactional
	public void removeRiskApplyfor(String id, String taskId) throws Exception {
		RiskEntity RiskEntity = this.findById(id);
		RiskEntity.setProcessState(DefaultRiskProcessStatus.NOT_SUBMIT);
		RiskEntity.setManualDecisionResult(null);
		RiskEntity.setReason(null);
		RiskEntity.setStartDate(new Date());
		RiskEntity.setStartUser(SecurityUtil.getLoginName().trim());
		RiskEntity.setCurrentTaskTerson(SecurityUtil.getLoginName().trim());
		repository.save(RiskEntity);
		//applyForLogService.deleteByRiskExposureIdAndProcessorNot(id, "system");
		if(!StringUtils.isEmpty(taskId)) {
			processOperationService.terminateTask(taskId);
		}
		
	}
	@Override
	@Transactional
	public void riskPeopleDone(String id, String taskId,String doneType) throws Exception {
		RiskEntity RiskEntity = this.findById(id);
		RiskEntity.setProcessState(DefaultRiskProcessStatus.COMPLETED_STOP);
		RiskEntity.setManualDecisionResult(null);
		RiskEntity.setReason(null);
		RiskEntity.setStartDate(new Date());
		RiskEntity.setStartUser(SecurityUtil.getLoginName().trim());
		RiskEntity.setCurrentTaskTerson(SecurityUtil.getLoginName().trim());
		RiskEntity.setFinshDate(new Date());
		repository.save(RiskEntity);
//		RiskApplyForLogEntity applyForLogEntity=new RiskApplyForLogEntity();
//		applyForLogEntity.setRiskExposureId(id);
//		applyForLogEntity.setMaintainResult(null);
//		applyForLogEntity.setReason(null);
//		applyForLogEntity.setMaintainDt(new Date());;
//		applyForLogEntity.setProcessor(SecurityUtil.getLoginName().trim());
//		applyForLogService.add(applyForLogEntity);
		//applyForLogService.deleteByRiskExposureIdAndProcessorNot(id, "system");
		if(!StringUtils.isEmpty(taskId)) {
			processOperationService.terminateTask(taskId);
		}
		
	}
	@Override
	@Transactional
	public void approve(ProcessProperties properties) throws Exception {
		Map<String,Object> variables = properties.getVariables();//持久化变量
		Map<String,Object> transientVariables = properties.getTransientVariables();//临时变量
		String taskId =transientVariables.get("taskId").toString();
		RiskEntity riskEntity = repository.getOne(transientVariables.get("defaultId").toString());
		String manualDecisionResult = transientVariables.get("manualDecisionResult")==null?null:transientVariables.get("manualDecisionResult").toString();
		String reason = transientVariables.get("reason")==null?null:transientVariables.get("reason").toString();
		if(StringUtils.isEmpty(taskId)){			
			throw new Exception("流程任务ID为空！");
		}		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		processOperationService.completeTask(taskId,variables,transientVariables);
		if(transientVariables.containsKey("goback") 
				&& !StringUtils.isEmpty(transientVariables.get("goback")) 
				&& !Integer.toString(Integer.MIN_VALUE).equals(transientVariables.get("goback").toString())) 
		{ //判断是否为退回
			//riskEntity.setProcessState(DefaultRiskProcessStatus.SEND_BACK);
		}else {
			riskEntity.setProcessState(DefaultRiskProcessStatus.IN_APPROVAL);
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
			if(processInstance==null || processInstance.isEnded()){	
				riskEntity.setFinshDate(new Date());
				riskEntity.setProcessState(DefaultRiskProcessStatus.COMPLETED);
				}		
		}
		riskEntity.setManualDecisionResult(manualDecisionResult);
		riskEntity.setFinalDecision(manualDecisionResult);
		riskEntity.setRegulatingMethods("MAN");
		riskEntity.setFinshDate(new Date());
		riskEntity.setReason(reason);
		riskEntity.setCurrentTaskTerson(String.valueOf(transientVariables.get("assignee")));//riskEntity.getStartUser()
		if(StringUtils.isEmpty(String.valueOf(transientVariables.get("assignee")))) {
			riskEntity.setLastModifier(SecurityUtil.getLoginName());
		}else {
			riskEntity.setLastModifier(String.valueOf(transientVariables.get("assignee")));
		}
		RiskApplyForLogEntity applyForLogEntity=new RiskApplyForLogEntity();
		applyForLogEntity.setRiskExposureId(transientVariables.get("defaultId").toString());
		applyForLogEntity.setMaintainResult(String.valueOf(transientVariables.get("manualDecisionResult")));
		applyForLogEntity.setReason(String.valueOf(transientVariables.get("reason")));
		applyForLogEntity.setMaintainDt(new Date());
		applyForLogEntity.setOperate("提交");
		applyForLogEntity.setRoleName(SecurityUtil.getDefaultRoleName());
		applyForLogEntity.setProcessor(String.valueOf(transientVariables.get("assignee")));//String.valueOf(transientVariables.get("assignee"))
		if(StringUtils.isEmpty(String.valueOf(transientVariables.get("assignee")))) {
			applyForLogEntity.setLastModifier(SecurityUtil.getLoginName());
		}else {
			applyForLogEntity.setLastModifier(String.valueOf(transientVariables.get("assignee")));
		}
		applyForLogService.add(applyForLogEntity);
		repository.save(riskEntity);
		
	}

	@Override
	public Page<RiskWrapper> fetchRiskApprove(RiskWrapper riskWrapper, Pageable pageable) throws Exception {
		String deployedKey = processEntityService.getEngineDeployedKey(DefaultRiskConstants.PWY);
	    String conditionSql="";
/*	    if(!"admin".equals(SecurityUtil.getLoginName().trim())){//	
		 conditionSql="	AND (RES.ASSIGNEE_ = '"+SecurityUtil.getLoginName()+"'" + 
			"  OR ( " + 
			"  RES.ASSIGNEE_ IS NULL AND  " + 
			"  I.TYPE_ = 'CANDIDATE' AND (I.USER_ID_ = '"+SecurityUtil.getLoginName()+"' " + 
			"  OR I.GROUP_ID_ IN ( '"+SecurityUtil.getDefaultRoleName()+"') " + 
			"  ))) "; 
		   }*/
		String sql=" SELECT"
				+"	ire.CONTRACT_NUM as contractNum,"
				+" 	ire.CONTRACT_SUM AS contractSum,"
				+" 	ire.PRODUCT_TYPE AS productType,"
				+" 	ire.LOANS_TO AS loansTo,"
				+" 	ire.MAIN_GUARANTEE_METHOD AS mainGuaranteeMethod,"
				+" 	ire.CTM_NUM AS ctmNum,"
				+" 	ire.CTM_NAME AS ctmName,"
				+" 	ire.SYSTEM_DECISION_RESULT AS systemDecisionResult,"
				+" 	ire.MANUAL_DECISION_RESULT AS manualDecisionResult,"
				+" 	ire.FINAL_DECISION AS finalDecision,"
				+" 	ire.system_decision_date AS systemDecisionDate,"
				+" 	ire.START_DATE AS startDate,"
				+" 	ire.PROCESS_STATE AS processState,"
				+"	decode(ire.CURRENT_TASK_TERSON,'null',ire.FD_LAST_MODIFIER,'',ire.FD_LAST_MODIFIER,ire.CURRENT_TASK_TERSON) as currentTaskTerson,"  //FD_LAST_MODIFIER
				+" 	ire.FINSH_DATE AS finshDate,"
				+"	ire.START_USER as startUser,"
				+" 	ire.REGULATING_METHODS AS regulatingMethods,"
				+"	ire.FD_INPUT_ORGID as agency,"   
				+"	ire.BALANCE as balance,"   
				+"	ire.CURRENCY as currency,"   
				+"	ire.START_DT as startDt,"   
				+"	ire.END_DT as endDt,"   
				+"	ire.BUSINESS_TYPE as occurrenceType,"  
				+" 	nc.FD_ENTERPRISE_SCALE AS scale,"
				+"	nc.FD_CTM_TYPE as ctmType,"
				+"	nc.FD_ANNUAL_INCOME as taking,"
				+"	nc.FD_TOTAL_ASSETS as totalAssets,"
				+"  TA.TASK_ID as taskId,"
				+"  nc.FD_GB_INDUSTRY as gbIndustry  "
				+" FROM"
				+" 	(   "
				+"  select * from ( "
				+" select * from ( "
				+" select c.id_ as TASK_ID,a.PROC_INST_ID_ AS PROC_INST_ID,a.assignee_ as ASSIGNEE ,pro.business_key_ from act_hi_taskinst a "
				+" left join act_re_procdef b "
				+" on a.proc_def_id_ = b.id_ "
				+" left join ACT_HI_PROCINST pro "
				+"  on pro.proc_inst_id_ = a.proc_inst_id_ "
				+" left join ACT_RU_TASK c  "
				+" on c.PROC_INST_ID_ = a.proc_inst_id_ "
				+" and b.key_ = '"+deployedKey+"' "
				+" ) group by PROC_INST_ID,business_key_,TASK_ID,ASSIGNEE ) "
				+" where ASSIGNEE = '"+SecurityUtil.getLoginName()+"'  "
				+" 	) TA"
				+" LEFT JOIN irs_risk_exposure ire ON TA.BUSINESS_KEY_ = ire.CONTRACT_NUM "
				+" LEFT JOIN ns_customer nc ON nc.FD_CUSTNO = ire.CTM_NUM"
				+" LEFT JOIN fr_aa_user t6 ON t6.FD_LOGINNAME = TA.ASSIGNEE"
				+" WHERE"
				+" 	ire.CONTRACT_NUM is not null ";
		sql=createCondition(riskWrapper,sql);
		String orderBy =getOrderSql(pageable);
		String countSql=" SELECT count(1) from ( " + sql +")";
		if(StringUtils.hasText(orderBy)){
			sql =sql + " " + orderBy;
		}
		sql =dialect.limit(sql, pageable.getPageNumber()+1, pageable.getPageSize());
		List<RiskWrapper> rs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RiskWrapper>(RiskWrapper.class));

		Long size=jdbcTemplate.queryForObject(countSql, Long.class);
		return new PageImpl<RiskWrapper>(rs,pageable,size);
	}
	
	
	@Override
	public JSONObject findOrgs() {
		
		List<Map<String, Object>> orgs = jdbcTemplate.queryForList("select fd_code,fd_name from fr_aa_org");
		
		JSONObject json = new JSONObject();
		
		for (Map<String, Object> map : orgs) {
			json.put(map.get("fd_code").toString(), map.get("fd_name").toString());
		}
		
		return json;
	}
	
}
