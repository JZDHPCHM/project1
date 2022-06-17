package net.gbicc.app.irr.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.entity.QOrg;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.flowable.entity.TaskInfo;
import com.gbicc.aicr.system.flowable.service.FlowableProcessOperationService;
import com.gbicc.aicr.system.flowable.service.FlowableTaskService;
import com.gbicc.aicr.system.in2oa.service.TaskToOAService;
import com.gbicc.aicr.system.util.DateUtils;
import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.entity.QIrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.QIrrProjResultEntity;
import net.gbicc.app.irr.jpa.entity.QIrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.QIrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrAuthTaskLogRepository;
import net.gbicc.app.irr.jpa.repository.IrrTaskRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProjTypeEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrPromptInfoEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrAuthTaskLogService;
import net.gbicc.app.irr.service.IrrChannelResultService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexResultService;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrProjResultService;
import net.gbicc.app.irr.service.IrrProjTypeService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.gbicc.app.irr.service.IrrTotalScoreResultService;
import net.gbicc.app.irr.service.IrrUploadResultService;
import net.sf.json.JSONObject;

/**
* 任务
*/
@Service
@Transactional
public class IrrTaskServiceImpl extends DaoServiceImpl<IrrTaskEntity, String, IrrTaskRepository> 
	implements IrrTaskService {

	private static final Logger LOG = LogManager.getLogger(IrrTaskServiceImpl.class);
	@Autowired private IrrSplitIndexService irrSplitIndexService;
	@Autowired private FlowableProcessOperationService flowableProcessOperationService;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private FlowableTaskService flowableTaskService;
	@Autowired private OrgService orgService;
	@Autowired private IrrChannelResultService irrChannelResultService;
	@Autowired private IrrOrgResultService irrOrgResultService;
	@Autowired private IrrIndexInfoService irrIndexInfoService;
	@Autowired private HistoryService historyService;
	@Autowired private UserService userService;
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private IrrIndexResultService indexResultService;
	@Autowired private IrrTotalScoreResultService totalScoreResultService;
	@Autowired private IrrProjResultService irrProjResultService;
	@Autowired private IrrUploadResultService irrUploadResultService;
	@Autowired private IrrProjTypeService irrProjTypeService;
    @Autowired
    private IrrAuthTaskLogService irrAuthTaskLogService;
    @Autowired
    private IrrAuthTaskLogRepository irrAuthTaskLogRepository;
    @Autowired
    private TaskToOAService taskToOAService;
	private LinkedHashMap linkedHashMap;
	
	@Override
	@Transactional
	public Map<String, Object> startTask(String taskName, String taskBatch, String deadPlanDate) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(taskBatch)) {
			map.put("flag", false);
			LOG.error("任务期次为空！");
			map.put("data", "任务期次为空！");
			return map;
		}
		taskBatch = taskBatch.toUpperCase();
		List<IrrTaskEntity> list = repository.findByTaskBatch(taskBatch);
		if(list != null && list.size() > 0) {
			map.put("flag", false);
			LOG.error("已经存在该期次的任务,如果继续执行，请先删除！");
			map.put("data", "已经存在该期次的任务,如果继续执行，请先删除！");
			return map;
		}
		IrrTaskEntity task = new IrrTaskEntity(taskName, taskBatch, DateUtils.parseDate(deadPlanDate), IrrProcessEnum.TASK_STATUS_CJZ.getValue().toString());
		/*启动流程并保存任务*/
		startProcess(task);
		map.put("flag", true);
		map.put("data", "任务发送成功！");
		return map;
	}

	/**
	 * 启动流程
	 * @param task 任务信息
	 */
	@Transactional
	private void startProcess(IrrTaskEntity task) throws Exception{
		List<Map<String, Object>> collUsers = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString(),null);
		if(CollectionUtils.isEmpty(collUsers)) {
			throw new IrrProcessException("无法发起流程：没有配置采集人!");
		}
		List<Org> orgList = orgService.getRepository().findAll();
		if(CollectionUtils.isEmpty(orgList)) {
			throw new IrrProcessException("无法发起流程：系统没有初始化组织机构!");
		}
		//风险管理部审核
		List<Map<String, Object>> summList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_SUMM.getValue().toString(),null);
		if(CollectionUtils.isEmpty(summList) || summList.get(0) == null || IrrUtil.strObjectIsEmpty(summList.get(0).get("FD_LOGINNAME"))) {
			throw new IrrProcessException("无法发起流程:风险管理部审核人不存在!");
		}
		Object summLoginName = summList.get(0).get("FD_LOGINNAME");
		//风险管理部总经理审核
		List<Map<String, Object>> managerList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_MANAGER.getValue().toString(),null);
		if(CollectionUtils.isEmpty(managerList) || managerList.get(0) == null || IrrUtil.strObjectIsEmpty(managerList.get(0).get("FD_LOGINNAME"))) {
			throw new IrrProcessException("无法发起流程:风险管理部总经理审核人不存在!");
		}
		Object managerLoginName = managerList.get(0).get("FD_LOGINNAME");
		//总公司子流程变量
		List<Map<String, Object>> headUser = new ArrayList<Map<String, Object>>();
		//分公司子流程变量
		List<Map<String, Object>> branchUser = new ArrayList<Map<String, Object>>();
		/*组装总公司、分公司子流程变量*/
		for(Org org : orgList) {
			if(IrrEnum.ORG_ROOT_CODE.getValue().equals(org.getCode())) {
				continue;
			}
			Map<String, Object> map = packSubProcessData(org);
			if(MapUtils.isEmpty(map)) {
				continue;
			}
			map.put(IrrProcessEnum.IRR_PROCESS_SUMM.getValue().toString(), summLoginName);
			map.put(IrrProcessEnum.IRR_PROCESS_MANAGER.getValue().toString(), managerLoginName);
			if(org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {//分公司
				branchUser.add(map);
			}else if(org.getName().contains(IrrProcessEnum.IRR_HEAD_SUFFIX.getValue().toString())) {//总部
				headUser.add(map);
			}
		}
		//会签子流程变量
		List<Map<String, Object>> signUser = new ArrayList<Map<String, Object>>();
		//查询会签子流程种类
		List<Map<String, Object>> signCollRoleList = findRoleUserByCondition(IrrProcessEnum.IRR_SIGN_PROCESS_COLL_PREFIX.getValue().toString());
		if(CollectionUtils.isEmpty(signCollRoleList)) {
			throw new IrrProcessException("无法发起流程:会签流程不存在采集人!");
		}
		//组装会签子流程
		for(Map<String, Object> signCollMap : signCollRoleList) {
			if(MapUtils.isEmpty(signCollMap) || IrrUtil.strObjectIsEmpty(signCollMap.get("FD_CODE"))) {
				continue;
			}
			Map<String, Object> signMap = packSignProcess(signCollMap.get("FD_CODE").toString());
			if(MapUtils.isEmpty(signMap)) {
				continue;
			}
			signMap.put(IrrProcessEnum.IRR_PROCESS_SUMM.getValue().toString(), summLoginName);
			signMap.put(IrrProcessEnum.IRR_PROCESS_MANAGER.getValue().toString(), managerLoginName);
			signUser.add(signMap);
		}
        add(task);
        String issue = task.getTaskBatch();
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put(IrrProcessEnum.IRR_HEAD_PROCESS_VAR.getValue().toString(),headUser);
		variables.put(IrrProcessEnum.IRR_BRANCH_PROCESS_VAR.getValue().toString(),branchUser);
		variables.put(IrrProcessEnum.IRR_SIGN_PROCESS_VAR.getValue().toString(),signUser);
        variables.put(IrrProcessEnum.PLAN_ISSUE_CODE.getValue().toString(), issue);
		flowableProcessOperationService.startProcessByKey(SecurityUtil.getLoginName(), IrrProcessEnum.IRR_PROCESS_DEFINITION_KEY.getValue().toString(), 
				task.getId(), variables, null, false);
        //发送任务成功后，发送采集人的OA提醒
        Date createDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        cal.add(Calendar.DATE, 2);
        Date endDate = cal.getTime();
        String createDateStr = DateUtils.formatDate(createDate);
        String endDateStr = DateUtils.formatDate(endDate);
        //发送分公司采集人OA提醒
        for (Map<String, Object> temp : headUser) {
            Object loginNameObj = temp.get(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString());
            if (IrrUtil.strObjectIsEmpty(loginNameObj)) {
                continue;
            }
            taskToOAService.sendOARemind(loginNameObj.toString(), issue, createDateStr, endDateStr);
        }
        //发送总公司采集人OA提醒
        for (Map<String, Object> temp : branchUser) {
            Object loginNameObj = temp.get(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString());
            if (IrrUtil.strObjectIsEmpty(loginNameObj)) {
                continue;
            }
            taskToOAService.sendOARemind(loginNameObj.toString(), issue, createDateStr, endDateStr);
        }
        //发送会签部门采集人OA提醒
        for (Map<String, Object> temp : signUser) {
            Object loginNameObj = temp.get(IrrProcessEnum.IRR_SIGN_PROCESS_COLL_VAR.getValue().toString());
            if (loginNameObj == null) {
                continue;
            }
            List<String> signCollList = (List<String>) loginNameObj;
            if (CollectionUtils.isEmpty(signCollList)) {
                continue;
            }
            for (String collUser : signCollList) {
                if (StringUtils.isBlank(collUser)) {
                    continue;
                }
                taskToOAService.sendOARemind(collUser, issue, createDateStr, endDateStr);
            }
        }
	}

	@Override
	public Map<String, Object> deleteTask(String taskId) throws Exception {
		if(StringUtils.isBlank(taskId)) {
			return IrrUtil.getMap(false, "无法删除：评估任务ID为空！");
		}
		flowableTaskService.deleteProcessInstanceByTaskId(taskId, "删除评估计划");
		/*删除此评估任务采集结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_UPLOAD_RESULT WHERE TASK_ID='"+taskId+"'");
		/*删除此评估任务机构结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_ORG_RESULT WHERE TASK_ID='"+taskId+"'");
		/*删除此评估任务渠道结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_CHANNEL_RESULT WHERE TASK_ID='"+taskId+"'");
		/*删除此评估任务指标结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_INDEX_RESULT WHERE TASK_ID='"+taskId+"'");
		/*删除此评估任务评估项目结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_PROJ_RESULT WHERE TASK_ID='"+taskId+"'");
		/*删除此评估任务评估项目多维度结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_TOTALSCORE_RESULT WHERE TASK_ID='"+taskId+"'");
		remove(taskId);
		return IrrUtil.getMap(true, IrrPromptInfoEnum.REMOVE_SUCCESS.getValue());
	}

	@Override
    public Map<String, Object> examBack(String taskId, String collUser, String comment, String id) throws Exception {
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("无法退回：流程任务ID为空");
		}
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_BACK_CODE.getValue());
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), collUser);
		/*如果是会签节点，则填写一票否决变量*/
		Task flowableTask = flowableTaskService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		if(flowableTask != null && flowableTask.getName().contains(IrrProcessEnum.IRR_SIGN_NAME_PREFIX.getValue().toString())) {
			transientVariables.put(IrrProcessEnum.IRR_SIGN_EXAM_ONE_TICKET_VETO.getValue().toString(), true);
		}
		String assignee = flowableTask.getAssignee();//获取审批人的账号
		QueryParameter qu = new QueryParameter();
		qu.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qu.setTextMatchStyle(TextMatchStyle.substring);
        User examUser = new User();
		examUser.setLoginName(assignee);
		List<User> users = userService.fetch(examUser, qu);
		String comms = "";
		if(!CollectionUtils.isEmpty(users) && users.get(0) != null) {
			comms+="*"+users.get(0).getOrgs().get(0).getName();
		}
        comment = IrrProcessEnum.IRR_COMMENT_BACK_PREFIX.getValue() + comment+comms;
        flowableTaskService.completeTask(taskId, null, transientVariables, null, null, comment);
        /*授权日志*/
        irrAuthTaskLogService.saveAuthTaskLog(id, flowableTask, comment);
		return IrrUtil.getMap(true,"退回成功！");
	}

	@Override
	public Map<String, Object> examSubmit(String taskId, String collUser, String comment,String id) throws Exception{
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("无法提交：流程任务ID为空");
		}
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_SUBMIT_CODE.getValue());
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), collUser);
		/*如果是会签节点，则填写一票否决变量*/
		Task flowableTask = flowableTaskService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		String assignee = flowableTask.getAssignee();//获取审批人的账号
		QueryParameter qu = new QueryParameter();
		qu.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qu.setTextMatchStyle(TextMatchStyle.substring);
        User examUser = new User();
		examUser.setLoginName(assignee);
		List<User> users = userService.fetch(examUser, qu);
		String comms = "";
		if(!CollectionUtils.isEmpty(users) && users.get(0) != null) {
			comms+="*"+users.get(0).getOrgs().get(0).getName();
		}
		if(flowableTask != null && flowableTask.getName().contains(IrrProcessEnum.IRR_SIGN_NAME_PREFIX.getValue().toString())) {
			transientVariables.put(IrrProcessEnum.IRR_SIGN_EXAM_ONE_TICKET_VETO.getValue().toString(), false);
		}
        comment = IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue() + comment+comms;
        flowableTaskService.completeTask(taskId, null, transientVariables, null, null, comment);
		String sql="UPDATE T_IRR_UPLOAD_RESULT UR SET UR.IS_COMMIT='"+IrrEnum.YESNO_Y.getValue()+"' WHERE UR.TASK_ID "
				 + "LIKE '%"+id+"%' AND UR.FD_CREATOR LIKE '%"+collUser+"%'";
		jdbcTemplate.execute(sql);
        /*授权日志*/
        irrAuthTaskLogService.saveAuthTaskLog(id, flowableTask, comment);
		return IrrUtil.getMap(true,"提交成功！");
	}

	@Override
	public Map<String, Object> summBack(String taskId, String loginNames, String comment) throws Exception{
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("无法退回：流程任务ID为空");
		}
		if(StringUtils.isBlank(loginNames)) {
			throw new IrrProcessException("无法退回：需要退回的采集人为空！");
		}
		String[] loginNameArr = loginNames.split(",");
		List<String> branchExamUser = irrSplitIndexService.findExamUserByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), null);
		List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
		for(String loginName : loginNameArr) {
			if(StringUtils.isBlank(loginName)) {
				continue;
			}
			List<String> orgIds = findOrgIdByCondition(loginName);
			if(CollectionUtils.isEmpty(orgIds) || StringUtils.isBlank(orgIds.get(0))) {
				continue;
			}
			Map<String, Object> user = null;//packSubProcessData(orgIds.get(0), branchExamUser);
			if(user == null || MapUtils.isEmpty(user)) {
				continue;
			}
			user.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_CODE.getValue().toString(), loginName);
			users.add(user);
		}
		Map<String, Object> transientVariables = new HashMap<String,Object>();
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_VAR_CODE.getValue().toString(), users);
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_BACK_CODE.getValue().toString());
		flowableTaskService.completeTask(taskId, null, transientVariables, null,null, IrrProcessEnum.IRR_COMMENT_BACK_PREFIX.getValue()+comment);
		return IrrUtil.getMap(true, "退回成功！");
	}

	@Override
	public Map<String, Object> indexSumm(String id) throws Exception {
		if(StringUtils.isBlank(id)) {
			throw new IrrProcessException("无法汇总：传入的评估计划ID为空！");
		}
		/*删除此评估任务渠道结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_CHANNEL_RESULT WHERE TASK_ID='"+id+"'");
		/*删除此评估任务分公司结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_ORG_RESULT WHERE TASK_ID='"+id+"'");
		/*删除此评估任务指标结果数据*/
		jdbcTemplate.execute("DELETE FROM T_IRR_INDEX_RESULT WHERE TASK_ID='"+id+"'");
		IrrTaskEntity task = findById(id);
		/*计算分公司指标*/
		branchIndexSumm(task);
		/*计算总公司指标*/
        headIndexSumm(task);
        /*计算渠道得分指标*/
        irrChannelResultService.calcChannelScoreIndex(task);
        /*进行数据验证*/
        irrOrgResultService.summCalcDataVali(task);
		/*计算源指标(计算结果暂时没有用到,故不计算)
        calcIndexSource(task);*/
		return IrrUtil.getMap(true, "指标汇总成功！");
	}

	@Override
	public Map<String, Object> projSumm(String id) throws Exception{
		if(StringUtils.isBlank(id)) {
			throw new IrrProcessException("任务ID参数为空!");
		}
		IrrTaskEntity task = findById(id);
		/*评估项目维度汇总*/
		totalScoreResultService.totalScoreSumm(task);
		return IrrUtil.getMap(true, "评估项目多维度汇总成功！");
	}

	@Override
	public Map<String, Object> summAll(String id) throws Exception{
		indexSumm(id);
        projSumm(id);
		return IrrUtil.getMap(true, "汇总全部成功！");
	}

	@Override
	public List<Map<String, Object>> findRoleUserAndOrgByCondition(String roleCode,String orgId) throws Exception {
		String sql = "SELECT DISTINCT UR.FD_USER_ID,U.FD_LOGINNAME,U.FD_USERNAME,UO.FD_ORG_ID,ORG.FD_CODE,ORG.FD_NAME FROM FR_AA_USER U INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID\r\n" + 
				"INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID INNER JOIN FR_AA_ORG ORG ON UO.FD_ORG_ID=ORG.FD_ID WHERE 1=1 ";
		if(StringUtils.isNotBlank(roleCode)) {
			sql += "AND R.FD_CODE='"+roleCode+"' ";
		}
		if(StringUtils.isNotBlank(orgId)) {
			sql += "AND ORG.FD_ID='"+orgId+"'";
		}
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<String, Object> packSubProcessData(Org org) throws Exception {
		if(org == null) {
			throw new IrrProcessException("机构为空!");
		}
		Map<String, Object> user = new HashMap<String,Object>();
		//采集人
		List<Map<String, Object>> collList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString(),org.getId());
		if(CollectionUtils.isEmpty(collList)) {
			return user;
		}
		user.put(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString(), collList.get(0).get("FD_LOGINNAME"));
		//审核人
		List<Map<String, Object>> examList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_EXAM.getValue().toString(),org.getId());
		if(CollectionUtils.isEmpty(examList)) {
			throw new IrrProcessException(org.getName()+"组织机构缺少审核人!");
		}
		user.put(IrrProcessEnum.IRR_PROCESS_EXAM.getValue().toString(), examList.get(0).get("FD_LOGINNAME"));
		return user;
	}

	@Override
	public Workbook createProcessCommentById(String id) throws Exception {
		Workbook hfwb = new HSSFWorkbook();
		Sheet sheet = hfwb.createSheet(IrrTemplateEnum.IRR_PLAN_COMMENT_SHEET_NAME.getValue());
		List<TaskInfo> list = flowableTaskService.findFinishedTaskDetailByBussinessKey(id);
		if(list == null || list.size() <= 0) {
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue(IrrTemplateEnum.IRR_PLAN_COMMENT_EMPTY.getValue().toString());
			return hfwb;
		}
        //进行授权人信息
        addAuthUserInfo(list);
		/*生成title*/
		Row titleRow = sheet.createRow(0);
		String[] titles = IrrTemplateEnum.IRR_PLAN_COMMENT_TITLE.getValue().split("-");
		int titleCellIndex = 0;
		for(String title : titles) {
			Cell titleCell = titleRow.createCell(titleCellIndex);
			titleCell.setCellStyle(POIUtil.createBorderCellStyle(hfwb, BorderStyle.THIN));
			titleCell.setCellStyle(POIUtil.createBackgroundColor(hfwb, HSSFColorPredefined.LIGHT_ORANGE.getIndex(),FillPatternType.SOLID_FOREGROUND));
			titleCell.setCellStyle(POIUtil.createFontCellStyle(hfwb, IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(), 
					Font.COLOR_NORMAL, true));
			titleCell.setCellValue(title);
			titleCellIndex ++;
		}	
		/*生成数据*/
		int dataRowIndex = 0;
		CellStyle dataCellBorderStyle = POIUtil.createBorderCellStyle(hfwb, BorderStyle.THIN);
		SimpleDateFormat simpleDateFormatEnd = new SimpleDateFormat("yyyy-MM-dd");
		for(TaskInfo info : list) {
			dataRowIndex ++;
			int dataCellIndex = 0;
			Row dataRow = sheet.createRow(dataRowIndex);
			/*任务名称*/
			Cell taskCell = dataRow.createCell(dataCellIndex);
			taskCell.setCellStyle(dataCellBorderStyle);
			taskCell.setCellValue(info.getName());
			dataCellIndex ++;
			/*任务人*/
			Cell nameCell = dataRow.createCell(dataCellIndex);
			nameCell.setCellStyle(dataCellBorderStyle);
			nameCell.setCellValue(info.getOwnerInfo());
			dataCellIndex ++;
			/*审批意见*/
			Cell commentCell = dataRow.createCell(dataCellIndex);
			commentCell.setCellStyle(dataCellBorderStyle);
			commentCell.setCellValue(info.getTaskComment());
			dataCellIndex ++;
			/*创建时间*/
			Cell createCell = dataRow.createCell(dataCellIndex);
			createCell.setCellStyle(dataCellBorderStyle);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			createCell.setCellValue(simpleDateFormat.format(info.getCreateDate()));
			dataCellIndex ++;
			/*完成时间*/
			Cell finishedCell = dataRow.createCell(dataCellIndex);
			finishedCell.setCellStyle(dataCellBorderStyle);
			if(info.getFinishDate() != null) {
				finishedCell.setCellValue(simpleDateFormatEnd.format(info.getFinishDate()));
			}
		}
		return hfwb;
	}

    /**
     * 查询出的任务添加授权信息
     *
     * @param list
     * @throws Exception
     */
    private void addAuthUserInfo(List<TaskInfo> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (TaskInfo info : list) {
            IrrAuthTaskLogEntity authTaskLog = irrAuthTaskLogRepository.findByTaskId(info.getId());
            if (authTaskLog != null && StringUtils.isNotBlank(authTaskLog.getAuthId())) {
                User authUser = userService.findById(authTaskLog.getAuthId());
                if (authUser != null) {
                    info.setOwnerInfo(
                            info.getOwnerInfo() + "->" + authUser.getUserName() + "(" + authUser.getLoginName() + ")");
                }
            }
        }
    }

	@Override
	public List<Map<String, Object>> findCollOrgByCondition(String roleCode,String enable) throws Exception {
		String sql = "SELECT DISTINCT ORG.FD_ID,ORG.FD_CODE,ORG.FD_NAME FROM FR_AA_ORG ORG INNER JOIN FR_AA_USER_ORG UO ON ORG.FD_ID=UO.FD_ORG_ID \r\n" + 
				"INNER JOIN FR_AA_USER U ON UO.FD_USER_ID=U.FD_ID INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID\r\n" + 
				"INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID WHERE R.FD_CODE='"+roleCode+"' AND ORG.FD_ENABLE="+enable;
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<IrrOrgResultEntity> branchIndexSumm(IrrTaskEntity task) throws Exception {
		if(task == null || StringUtils.isBlank(task.getId())) {
			return null;
		}
		/*查询分公司不适用的指标*/
		IrrIndexInfoEntity indexInfoExam = new IrrIndexInfoEntity();
		indexInfoExam.setIndexStatus(IrrEnum.INDEX_STATUS_ENABLE.getValue());
		indexInfoExam.setIndexLevel(IrrEnum.INDEX_LEVEL_BRANCH.getValue());
		indexInfoExam.setIsApplicabie(IrrEnum.YESNO_N.getValue());
		List<IrrIndexInfoEntity> branchDisableList = irrIndexInfoService.fetch(indexInfoExam, IrrQueryParameter.getIrrDefaultQP());
		/*计算分公司不适用的指标*/
		List<IndexValueDTO> branchDisableValue = irrIndexInfoService.summIndexDisable(branchDisableList);
		/*分公司不分渠道*/
		/*查询分公司没有公式的指标*/
		List<IrrIndexInfoEntity> branchNoFormulaList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_BRANCH.getValue(), IrrEnum.YESNO_Y.getValue(), true, null,IrrEnum.YESNO_Y.getValue(),null);
		/*查询分公司有公式的非得分的指标*/
		List<IrrIndexInfoEntity> branchFormulaNoScoreList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_BRANCH.getValue(), IrrEnum.YESNO_Y.getValue(), false, IrrEnum.YESNO_N.getValue(),IrrEnum.YESNO_Y.getValue(),null);
		/*查询分公司有公式且为得分的指标*/
		List<IrrIndexInfoEntity> branchFormulaScoreList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_BRANCH.getValue(), IrrEnum.YESNO_Y.getValue(), false, IrrEnum.YESNO_Y.getValue(),IrrEnum.YESNO_Y.getValue(),null);
		/*分公司结果*/
		List<IrrOrgResultEntity> branchResult = new ArrayList<IrrOrgResultEntity>();
		/*查询采集指标的分公司*/
		List<Map<String,Object>> orgList = findCollOrgByCondition(IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString(), IrrProcessEnum.ENABLE.getValue().toString());
		if(CollectionUtils.isEmpty(orgList)) {
			throw new IrrProcessException("无法汇总：查询不到采集机构相关信息！");
		}
		for(Map<String,Object> org : orgList) {
			if(IrrUtil.strObjectIsEmpty(org.get("FD_ID"))) {
				continue;
			}
			if(org.get("FD_NAME").toString().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {//分公司
				List<IndexValueDTO> result = new ArrayList<IndexValueDTO>();
				/*分公司不适用指标*/
				if(CollectionUtils.isNotEmpty(branchDisableValue)) {
					result.addAll(branchDisableValue);
				}
				/*获取分公司没有公式的指标结果*/
				List<IndexValueDTO> noFromulaResult = irrUploadResultService.getIndexResult(task.getId(), org.get("FD_ID").toString(), branchNoFormulaList);
				if(CollectionUtils.isNotEmpty(noFromulaResult)) {
					result.addAll(noFromulaResult);
				}
				/*计算分公司有公式且非得分的指标*/
				if(CollectionUtils.isNotEmpty(branchFormulaNoScoreList)) {
					List<IndexValueDTO> formulaNoScoreResult = irrIndexInfoService.summIndexNum(branchFormulaNoScoreList,result);
					if(CollectionUtils.isNotEmpty(formulaNoScoreResult)) {
						result.addAll(formulaNoScoreResult);
					}
				}
				/*计算分公司有公式且得分的指标*/
				if(CollectionUtils.isNotEmpty(branchFormulaScoreList)) {
					List<IndexValueDTO> formulaScoreResult = irrIndexInfoService.summIndexNum(branchFormulaScoreList, result);
					if(CollectionUtils.isNotEmpty(formulaScoreResult)) {
						result.addAll(formulaScoreResult);
					}
				}
				for(IndexValueDTO branchValue : result) {
					IrrOrgResultEntity orgResult = new IrrOrgResultEntity();
					orgResult.setIndexId(branchValue.getIndexId());
					orgResult.setIndexCode(branchValue.getCode());
					orgResult.setIndexName(branchValue.getName());
					orgResult.setIndexResultQ2(branchValue.getValue());
					orgResult.setTaskId(task.getId());
					orgResult.setTaskBatch(task.getTaskBatch());
					orgResult.setOrgId(org.get("FD_ID").toString());
					orgResult.setOrgCode(org.get("FD_CODE").toString());
					orgResult.setOrgName(org.get("FD_NAME").toString());
					branchResult.add(irrOrgResultService.add(orgResult));
				}
				/*计算得分*/
				irrOrgResultService.calcOrgIndexScore(org.get("FD_ID").toString(), task);
			}
		}
		return branchResult;
	}

	@Override
	public List<IrrOrgResultEntity> headIndexSumm(IrrTaskEntity task) throws Exception {
		if(task == null || StringUtils.isBlank(task.getId())) {
			return null;
		}
		List<IrrOrgResultEntity> headResult = new ArrayList<IrrOrgResultEntity>();
		List<IndexValueDTO> result = new ArrayList<IndexValueDTO>();
		Org rootOrg = new Org();
		rootOrg.setCode(IrrEnum.ORG_ROOT_CODE.getValue());
		List<Org> orgList = orgService.fetch(rootOrg, IrrQueryParameter.getIrrDefaultQP());		
		if(CollectionUtils.isNotEmpty(orgList)) {
			rootOrg = orgList.get(0);
		}
		/*查询总公司不适用的指标*/
		IrrIndexInfoEntity indexInfoExam = new IrrIndexInfoEntity();
		indexInfoExam.setIndexStatus(IrrEnum.INDEX_STATUS_ENABLE.getValue());
		indexInfoExam.setIsXbrl(IrrEnum.YESNO_Y.getValue());
		indexInfoExam.setIsApplicabie(IrrEnum.YESNO_N.getValue());
		indexInfoExam.setIndexLevel(IrrEnum.INDEX_LEVEL_HEAD.getValue());
		List<IrrIndexInfoEntity> disableList = irrIndexInfoService.fetch(indexInfoExam, IrrQueryParameter.getIrrDefaultQP());
		/*计算总公司不适用的指标*/
		List<IndexValueDTO> disableValue = irrIndexInfoService.summIndexDisable(disableList);
		if(CollectionUtils.isNotEmpty(disableValue)) {
			result.addAll(disableValue);
		}
		/*保存总公司渠道指标结果*/
		irrChannelResultService.saveHeadChannelResult(task.getId(), rootOrg);
		/*计算总公司渠道指标结果*/
		List<IndexValueDTO> channelDeptSum = irrIndexInfoService.sumHeadChannelIndex(task.getId());
		if(CollectionUtils.isNotEmpty(channelDeptSum)) {
			result.addAll(channelDeptSum);
		}
		/*获取总公司没有公式的指标结果*/
		List<IrrIndexInfoEntity> headNoFormulaList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_HEAD.getValue(), IrrEnum.YESNO_Y.getValue(), true, null,null,false);
		List<IndexValueDTO> headNoFormulaResult = irrUploadResultService.getIndexResult(task.getId(), null, headNoFormulaList);
		if(CollectionUtils.isNotEmpty(headNoFormulaResult)) {
			result.addAll(headNoFormulaResult);
		}
		/*由于分配的指标是需要采集的，具有公式的指标不需要分配，所以不作为查询条件*/
		/*计算总公司具有公式且非得分指标*/
		List<IrrIndexInfoEntity> headFormulaNoScoreList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_HEAD.getValue(), IrrEnum.YESNO_Y.getValue(), false, IrrEnum.YESNO_N.getValue(),null,null);
		if(CollectionUtils.isNotEmpty(headFormulaNoScoreList)) {
			result.addAll(irrIndexInfoService.summIndexNum(headFormulaNoScoreList, result));
		}
		/*计算总公司具有公式且得分指标*/
		List<IrrIndexInfoEntity> headFormulaScoreList = irrIndexInfoService.findIndexInfoByCondtion(IrrEnum.INDEX_STATUS_ENABLE.getValue(),
				IrrEnum.INDEX_LEVEL_HEAD.getValue(), IrrEnum.YESNO_Y.getValue(), false, IrrEnum.YESNO_Y.getValue(),null,null);
		if(CollectionUtils.isNotEmpty(headFormulaScoreList)) {
			result.addAll(irrIndexInfoService.summIndexNum(headFormulaScoreList, result));
		}
		/*保存总公司上报结果*/
		for(IndexValueDTO dto : result) {
			IrrOrgResultEntity orgResult = new IrrOrgResultEntity();
			orgResult.setIndexId(dto.getIndexId());
			orgResult.setIndexCode(dto.getCode());
			orgResult.setIndexName(dto.getName());
			orgResult.setIndexResultQ2(dto.getValue());
			orgResult.setOrgCode(rootOrg.getCode());
			orgResult.setOrgId(rootOrg.getId());
			orgResult.setOrgName(rootOrg.getName());
			orgResult.setTaskId(task.getId());
			orgResult.setTaskBatch(task.getTaskBatch());
			headResult.add(irrOrgResultService.add(orgResult));
		}
		/*计算得分*/
		irrOrgResultService.calcOrgIndexScore(rootOrg.getId(), task);
		return headResult;
	}

	/**
	 * 机构结果存放在计算集合中
	 * @param orgResultList 需要存放的机构结果(已经存在，不为null)
	 * @return
	 */
	private List<IndexValueDTO> orgResultTransIndexValue(List<IrrOrgResultEntity> orgResultList){
		List<IndexValueDTO> valueList = new ArrayList<IndexValueDTO>();
		for(IrrOrgResultEntity org : orgResultList) {
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(org.getIndexId());
			value.setCode(org.getIndexCode());
			value.setName(org.getIndexName());
			value.setValue(org.getIndexResultQ2());
			valueList.add(value);
		}
		return valueList;
	}

	@Override
	public Map<String, String> getOrgMap(String parentId) throws Exception{
		String sql = "SELECT * FROM FR_AA_ORG";
		if(StringUtils.isNotBlank(parentId)) {
			sql += " WHERE FD_PARENT_ID='"+parentId+"'";
		}
		List<Map<String,Object>> orgs = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(orgs)) {
			return null;
		}
		Map<String,String> map = new HashMap<String,String>();
		for(Map<String,Object> org : orgs) {
			map.put(org.get("FD_ID").toString(), org.get("FD_NAME").toString());
		}
		return map;
	}

	@Override
	public void updateTask(String taskId, String taskStatus) {
		if(StringUtils.isBlank(taskId)) {
			LOG.error("更新评估计划状态的时候失败，评估计划ID为空");
		}
		try {
			IrrTaskEntity task = findById(taskId);
			task.setTaskStatus(taskStatus);
			task.setEndDate(new Date());
			update(task.getId(), task);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}

	@Override
	public Map<String, Object> findSummBackInfo(String id) {
		if(StringUtils.isBlank(id)) {
			return null;
		}
		String sql = "SELECT DISTINCT HA.ASSIGNEE_ LOGINNAME,U.FD_USERNAME USERNAME,ORG.FD_NAME ORGNAME FROM ACT_HI_PROCINST HP INNER JOIN ACT_HI_ACTINST HA ON HP.PROC_INST_ID_=HA.PROC_INST_ID_\r\n" + 
				"INNER JOIN FR_AA_USER U ON HA.ASSIGNEE_=U.FD_LOGINNAME INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID\r\n" + 
				"INNER JOIN FR_AA_ORG ORG ON UO.FD_ORG_ID=ORG.FD_ID WHERE HP.BUSINESS_KEY_='"+id+"' AND HA.ACT_ID_='"+IrrProcessEnum.COLL_USER_TASK_ID.getValue().toString()+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		ResponsePage<Map<String, Object>> response = new ResponsePage<Map<String, Object>>();
		response.setAllData(list);
		response.setNumber(1L);
		response.setSize(100);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("response", response.getPageData());
		return map;
	}

	@Override
	public List<String> findOrgIdByCondition(String loginName) {
		String sql = "SELECT DISTINCT UO.FD_ORG_ID FROM FR_AA_USER U INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID WHERE U.FD_LOGINNAME='"+loginName+"'";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public Map<String, String> getTaskMap(String taskStatus) throws Exception {
        String sql = "SELECT * FROM T_IRR_TASK ";
		if(StringUtils.isNotBlank(taskStatus)) {
            sql += " WHERE TASK_STATUS='" + taskStatus + "'";
		}
		sql += " order by TASK_BATCH DESC";
        List<IrrTaskEntity> list = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<IrrTaskEntity>(IrrTaskEntity.class));
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String,String> map = new LinkedHashMap<String,String>();
		//Map<String,String> map = new HashMap<String,String>();
		for(IrrTaskEntity task : list) {
			map.put(task.getId(), task.getTaskName());
		}
		return map;
	}

	@Override
	public Map<String, Object> findProcessComment(String id, Integer size, Long page) throws Exception {
		if(StringUtils.isBlank(id)) {
			throw new IrrProcessException("评估项目ID为空!");
		}
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
		List<TaskInfo> comments = flowableTaskService.myFinishedTaskDetailByProIns(processInstance.getId());
		if(CollectionUtils.isEmpty(comments)) {
			return null;
		}
        addAuthUserInfo(comments);
		ResponsePage<TaskInfo> response = new ResponsePage<TaskInfo>();
		response.setAllData(comments);
		if(size != null) {
			response.setSize(size);
		}
		if(page != null) {
			response.setNumber(page);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", response.getPageData());
		return map;
	}

	@Override
	public JSONObject getUserInfo(String loginName) throws Exception {
		JSONObject returnObj = new JSONObject();
		if(StringUtils.isBlank(loginName)) {
			return returnObj;
		}
		User examUser = new User();
		examUser.setLoginName(loginName);
		List<User> userList = userService.fetch(examUser, IrrQueryParameter.getIrrDefaultQP());
		User user = null;
		Org org = null;
		if(CollectionUtils.isNotEmpty(userList)) {
			user = userList.get(0);
			List<Org> orgList = user.getOrgs();
			if(CollectionUtils.isNotEmpty(orgList)) {
				org = orgList.get(0);
			}
		}
		returnObj.put("userName", user.getUserName());
		returnObj.put("orgName", org.getName());
		return returnObj;
	}

	@Override
	public Object calcFormula(String formulaStr, List<String> codeList, Map<String, String> valueMap) throws Exception {
		if(StringUtils.isBlank(formulaStr)) {
			return null;
		}
		if(CollectionUtils.isEmpty(codeList)) {
			return formulaStr;
		}
		for(String str : codeList) {
			if(StringUtils.isBlank(str)) {
				continue;
			}
			String value = valueMap.get(str);
			if(StringUtils.isBlank(value)) {
				formulaStr = formulaStr.replace(str, "null");
			}else {
				formulaStr = formulaStr.replace(str, value);
			}
		}
        return JavaJsEvalUtil.evalStr(formulaStr);
	}

	@Override
	public List<IrrIndexResultEntity> calcIndexSource(IrrTaskEntity task) throws Exception{
		if(task == null) {
			return null;
		}
		jdbcTemplate.execute("DELETE FROM T_IRR_INDEX_RESULT WHERE TASK_ID='"+task.getId()+"'");
		Map<String, String> projMap = systemDictionaryService.getDictionaryMap(IrrEnum.XBRL_PROJ.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(projMap)) {
			throw new IrrProcessException("汇总权重指标失败:系统没有配置上报评估项目数据字典!");
		}
		List<IrrIndexResultEntity> returnList = new ArrayList<IrrIndexResultEntity>();
		/*上期结果*/
		IrrIndexResultEntity indexResult = new IrrIndexResultEntity();
		indexResult.setTaskBatch(IrrUtil.getPreviousPeriod(task.getTaskBatch()));
		List<IrrIndexResultEntity> list = indexResultService.fetch(indexResult, IrrQueryParameter.getIrrDefaultQP());
		Map<String,IrrIndexResultEntity> perivousIndexResultMap = new HashMap<String,IrrIndexResultEntity>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(IrrIndexResultEntity result : list) {
				perivousIndexResultMap.put(result.getIndexCode(), result);
			}
		}
		/*分公司总数*/
		Integer branchNum = Integer.valueOf(IrrEnum.DEFAULT_BRANCH_NUM.getValue());
		QOrg qOrg = QOrg.org;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qOrg.name.like("%"+IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString()));
		List<Org> branchOrgList = (List<Org>) orgService.getRepository().findAll(builder.getValue());
		if(CollectionUtils.isNotEmpty(branchOrgList)) {
			branchNum = branchOrgList.size();
		}
		Map<String, String> branchXBRLOrgExcludeMap = systemDictionaryService.getDictionaryMap(IrrEnum.BRANCH_XBRL_ORG_EXCLUDE.getValue(), Locale.CHINA);
		if(MapUtils.isNotEmpty(branchXBRLOrgExcludeMap)) {
			branchNum -= branchXBRLOrgExcludeMap.size();
		}
		/*计算本期*/
		for(String key : projMap.keySet()) {
			List<IrrProjTypeEntity> projList = irrProjTypeService.getRepository().findByTypeCode(key);
			if(CollectionUtils.isEmpty(projList)) {
				continue;
			}
			String projName = projMap.get(key);
			if(StringUtils.isBlank(projName) || key.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
				continue;
			}
			IrrProjTypeEntity projType = projList.get(0);
			if(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().equals(projName.substring(0, 3))) {
				//分公司
				List<IrrIndexResultEntity> branchIndexSource =  irrOrgResultService.calcBranchIndexSource(task,projType,perivousIndexResultMap,branchNum);
				if(CollectionUtils.isNotEmpty(branchIndexSource)) {
					returnList.addAll(branchIndexSource);
				}
			}
		}
		/*总公司(由于总公司可从机构结果表中直接取到，不需要计算，故无意义)*/
		/*List<IrrIndexResultEntity> headIndexSource = irrOrgResultService.calcHeadIndexSource(task);
		if(CollectionUtils.isNotEmpty(headIndexSource)) {
			returnList.addAll(headIndexSource);
		}*/
		return returnList;
	}

	@Override
	public Map<String, String> findTaskYear() {
		String sql = "SELECT DISTINCT SUBSTR(TASK_BATCH,0,4) YEAR FROM T_IRR_TASK";
		List<String> list = jdbcTemplate.queryForList(sql, String.class);
		Map<String, String> returnMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(String str : list) {
				returnMap.put(str, str);
			}
		}
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> findRoleUserByCondition(String rolePrefix) throws Exception {
		String sql = "SELECT DISTINCT R.FD_CODE FROM FR_AA_USER U INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID \r\n" + 
				"INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID WHERE R.FD_CODE LIKE '"+rolePrefix+"%'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<String, Object> packSignProcess(String signCollRoleCode) throws Exception {
		if(StringUtils.isBlank(signCollRoleCode)) {
			throw new IrrProcessException("会签子流程采集角色编码为空!");
		}
		List<Map<String, Object>> signCollList = findRoleUserAndOrgByCondition(signCollRoleCode, null);
		if(CollectionUtils.isEmpty(signCollList)) {
			return null;
		}
		//会签采集人
		List<String> signCollUserList = new ArrayList<String>();
		for(Map<String, Object> signCollUser : signCollList) {
			if(signCollUser == null || IrrUtil.strObjectIsEmpty(signCollUser.get("FD_LOGINNAME"))) {
				continue;
			}
			signCollUserList.add(signCollUser.get("FD_LOGINNAME").toString());
		}
		//截取会签角色后缀
		String suffix = signCollRoleCode.substring(IrrProcessEnum.IRR_SIGN_PROCESS_COLL_PREFIX.getValue().toString().length());
		//会签审核人
		List<Map<String, Object>> signExamList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_SIGN_PROCESS_EXAM_PREFIX.getValue().toString()+suffix, null);
		if(CollectionUtils.isEmpty(signExamList)) {
			throw new IrrProcessException("会签流程后缀"+suffix+"没有配置审核人!");
		}
		List<String> signExamUserList = new ArrayList<String>();
		for(Map<String, Object> signExamUser : signExamList) {
			if(signExamUser == null || IrrUtil.strObjectIsEmpty(signExamUser.get("FD_LOGINNAME"))) {
				continue;
			}
			signExamUserList.add(signExamUser.get("FD_LOGINNAME").toString());
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put(IrrProcessEnum.IRR_SIGN_PROCESS_COLL_VAR.getValue().toString(), signCollUserList);
		map.put(IrrProcessEnum.IRR_SIGN_PROCESS_EXAM_VAR.getValue().toString(), signExamUserList);
		return map;
	}

	@Override
	public Org findOrgByUserId(String userId) throws Exception {
		List<Org> orgList = findOrgListByUserId(userId);
		if(CollectionUtils.isNotEmpty(orgList)) {
			return orgList.get(0);
		}
		return null;
	}

	@Override
	public List<Org> findOrgListByUserId(String userId) throws Exception {
		User user = userService.findById(userId);
		if(user != null) {
			return user.getOrgs();
		}
		return null;
	}

	@Override
	public Map<String, Object> findRoleByCondition(String roleCode,String userId) throws Exception {
		String sql = "SELECT R.* FROM FR_AA_ROLE R INNER JOIN FR_AA_USER_ROLE UR ON R.FD_ID=UR.FD_ROLE_ID INNER JOIN FR_AA_USER U ON UR.FD_USER_ID=U.FD_ID\r\n" + 
                "WHERE U.FD_ID='" + userId + "' AND R.FD_CODE LIKE '" + roleCode + "%'";
		List<Map<String, Object>> listMap = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isNotEmpty(listMap) && listMap.get(0) != null 
				&& !IrrUtil.strObjectIsEmpty(listMap.get(0).get("FD_CODE"))) {
			return listMap.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> weightSumm(String taskId) throws Exception {
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("任务ID参数为空!");
		}
		IrrTaskEntity task = findById(taskId);
		/*计算权重*/
		irrProjResultService.calcProjRate(task);
		return IrrUtil.getMap(true, "权重汇总成功");
	}

	@Override
	public Map<String, Object> aTicketCross(String id,String taskId) throws Exception {
		if(StringUtils.isBlank(taskId)) {
			return null;
		}
		TaskService taskService = flowableTaskService.getTaskService();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task == null) {
			return null;
		}
        String assignee = flowableTaskService.findTaskAssignee(taskId);
        if (StringUtils.isBlank(assignee)) {
            return null;
        }
		List<Task> needComplateTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(id).taskName(task.getName()).taskAssignee(assignee).list();
		if(CollectionUtils.isEmpty(needComplateTask)) {
			return null;
		}
		String comment = IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue().toString() + IrrProcessEnum.A_TICKET_COMMENT.getValue().toString();
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_SUBMIT_CODE.getValue());
		for(Task temp : needComplateTask) {
			flowableTaskService.completeTask(temp.getId(), null, transientVariables, comment);
		}
        //授权日志
        irrAuthTaskLogService.saveAuthTaskLog(id, task, comment);
		return IrrUtil.getMap(true, "一票全过成功!");
	}

	@Override
	public Map<String, Object> findXbrlRequestInfo(String taskId, String orgId) throws Exception {
		if(StringUtils.isBlank(taskId) || StringUtils.isBlank(orgId)) {
			return null;
		}
		QIrrTaskEntity qTask = QIrrTaskEntity.irrTaskEntity;
		BooleanBuilder taskBuilder = new BooleanBuilder();
		taskBuilder.and(qTask.id.eq(taskId));
		Optional<IrrTaskEntity> task = getRepository().findOne(taskBuilder.getValue());
		if(task == null) {
			return null;
		}
		Map<String, Object> map = IrrUtil.getMap(true);
		map.put("task", task);
		QOrg qOrg = QOrg.org;
		BooleanBuilder orgBuilder = new BooleanBuilder();
		orgBuilder.and(qOrg.id.eq(orgId));
		Optional<Org> org = orgService.getRepository().findOne(orgBuilder.getValue());
		if(org != null) {
			map.put("org", org);
		}
		return map;
	}

	@Override
	public Map<String, String> getOrgsMap(String prefix) throws Exception {
		String sql = "SELECT * FROM FR_AA_ORG WHERE 1=1 ";
		if(StringUtils.isNotBlank(prefix)) {
			sql += " AND FD_CODE LIKE '"+prefix+"%' ORDER BY FD_CODE ";
		}
		List<Map<String,Object>> orgs = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(orgs)) {
			return null;
		}
		Map<String,String> map = new TreeMap<String,String>();
		for(Map<String,Object> org : orgs) {
			map.put(org.get("FD_ID").toString(), org.get("FD_NAME").toString());
		}
		return map;
	}

    @Override
    public Map<String, String> getOrgMapByName(String orgName) throws Exception {
        String sql = "SELECT * FROM FR_AA_ORG ";
        if (StringUtils.isNotBlank(orgName)) {
            sql += " WHERE FD_NAME LIKE '%" + orgName + "%' AND FD_NAME <> '河北分公司' ORDER BY FD_CODE";
        }
        List<Map<String, Object>> orgs = jdbcTemplate.queryForList(sql);
        if (CollectionUtils.isEmpty(orgs)) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Map<String, Object> org : orgs) {
            map.put(org.get("FD_ID").toString(), org.get("FD_NAME").toString());
        }
        return map;
    }

    @Override
    public void resultSetScale(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return;
        }
        //查询结果为数字的指标配置
        QIrrIndexInfoEntity qIndexInfo = QIrrIndexInfoEntity.irrIndexInfoEntity;
        BooleanBuilder indexInfoBuilder = new BooleanBuilder();
        indexInfoBuilder.and(qIndexInfo.isXbrl.eq(IrrEnum.YESNO_Y.getValue()));
        indexInfoBuilder.and(qIndexInfo.isApplicabie.eq(IrrEnum.YESNO_Y.getValue()));
        indexInfoBuilder.and(qIndexInfo.indexResultType.eq(IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue()));
        Iterator<IrrIndexInfoEntity> indexInfoIterator = irrIndexInfoService.getRepository().findAll(indexInfoBuilder)
                .iterator();
        //key：指标编码；value:指标信息
        Map<String, IrrIndexInfoEntity> indexInfoEntityMap = new HashMap<String, IrrIndexInfoEntity>();
        while (indexInfoIterator.hasNext()) {
            IrrIndexInfoEntity indexInfo = indexInfoIterator.next();
            indexInfoEntityMap.put(indexInfo.getIndexCode(), indexInfo);
        }
        //指标结果为数字的格式化
        String numberSql = "SELECT IOR.* FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_CODE=II.INDEX_CODE\r\n"
                + "WHERE II.INDEX_RESULT_TYPE='" + IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue() + "' AND IOR.TASK_ID='"
                + id + "' AND II.IS_APPLICABIE='" + IrrEnum.YESNO_Y.getValue() + "'";
        List<IrrOrgResultEntity> list = jdbcTemplate.query(numberSql,
                new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
        Map<String, IrrOrgResultEntity> orgResultMap = new HashMap<String, IrrOrgResultEntity>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (IrrOrgResultEntity orgResult : list) {
                //结果为不适用的，则不进行格式化
                String indexResult = orgResult.getIndexResultQ2();
                if (StringUtils.isBlank(indexResult)
                        || indexResult.contains(IrrEnum.INDEX_NOT_APPLICABIE_VALUE_CONTENT.getValue()))
                    continue;
                String indexCode = orgResult.getIndexCode();
                if (StringUtils.isBlank(indexCode))
                    continue;
                IrrIndexInfoEntity indexInfo = indexInfoEntityMap.get(indexCode);
                if (indexInfo == null)
                    continue;
                BigDecimal decimalPlace = indexInfo.getDecimalPlace();
                if (decimalPlace == null)
                    continue;
                orgResult.setIndexResultQ2(
                        IrrUtil.formatNumber(decimalPlace.intValue(), indexResult, true));
                orgResultMap.put(orgResult.getId(), orgResult);
            }
        }
        //指标结果为无的格式化
        /*String noneSql = "SELECT IOR.* FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_CODE=II.INDEX_CODE\r\n"
                + "WHERE II.INDEX_RESULT_TYPE='" + IrrEnum.INDEX_RESULT_TYPE_NONE.getValue() + "' AND IOR.TASK_ID='"
                + id + "' AND II.IS_APPLICABIE='" + IrrEnum.YESNO_Y.getValue() + "'";
        List<IrrOrgResultEntity> noneList = jdbcTemplate.query(noneSql,
                new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
        if (CollectionUtils.isNotEmpty(noneList)) {
            for (IrrOrgResultEntity orgResult : noneList) {
                String indexResult = orgResult.getIndexResultQ2();
                if (StringUtils.isBlank(indexResult))
                    continue;
                orgResult.setIndexResultQ2(IrrEnum.INDEX_RESULT_TYPE_NONE_FORMATE_PREFIX.getValue() + indexResult
                        + IrrEnum.INDEX_RESULT_TYPE_NONE_FORMATE_SUFFIX.getValue());
                orgResultMap.put(orgResult.getId(), orgResult);
            }
        }*/
        irrOrgResultService.update(orgResultMap);
    }

    @Override
    public void scoreSetScale(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return;
        }
        Integer scale = Integer.valueOf(IrrEnum.DEFAULT_SCORE_SCALE.getValue());
        //所有的得分指标得分保留2位小数
        String sql = "SELECT IOR.* FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_CODE=II.INDEX_CODE \r\n"
                + "WHERE II.IS_SCORE_INDEX='" + IrrEnum.YESNO_Y.getValue()
                + "' AND IOR.INDEX_SCORE_Q2 IS NOT NULL AND IOR.TASK_ID='" + id + "'";
        List<IrrOrgResultEntity> list = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, IrrOrgResultEntity> orgResultMap = new HashMap<String, IrrOrgResultEntity>();
        for (IrrOrgResultEntity entity : list) {
            String score = IrrUtil.formatNumber(scale, entity.getIndexScoreQ2(), true);
            if (StringUtils.isNotBlank(score)) {
                entity.setIndexScoreQ2(score);
                orgResultMap.put(entity.getId(), entity);
            }
        }
        irrOrgResultService.update(orgResultMap);
    }

    @Override
    public void weightSetScale(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new IrrProcessException("无法保留小数：传入的ID为空！");
        }
        Integer scale = Integer.valueOf(IrrEnum.DEFAULT_SCORE_SCALE.getValue());
        QIrrProjResultEntity qProjResult = QIrrProjResultEntity.irrProjResultEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProjResult.taskId.eq(id));
        Iterator<IrrProjResultEntity> iterator = irrProjResultService.getRepository().findAll(builder).iterator();
        Map<String, IrrProjResultEntity> map = new HashMap<String, IrrProjResultEntity>();
        while (iterator.hasNext()) {
            IrrProjResultEntity projResult = iterator.next();
            String branchScore = IrrUtil.formatNumber(scale, projResult.getBranchScore().toPlainString(), true);
            String headScore = IrrUtil.formatNumber(scale, projResult.getHeadScore().toPlainString(), true);
            String totalScore = IrrUtil.formatNumber(scale, projResult.getTotalScore().toPlainString(), true);
            if (StringUtils.isNotBlank(branchScore)) {
                projResult.setBranchScore(new BigDecimal(branchScore));
            }
            if (StringUtils.isNotBlank(headScore)) {
                projResult.setHeadScore(new BigDecimal(headScore));
            }
            if (StringUtils.isNotBlank(totalScore)) {
                projResult.setTotalScore(new BigDecimal(totalScore));
            }
            map.put(projResult.getId(), projResult);
        }
        irrProjResultService.update(map);
        QIrrTotalScoreResultEntity qTotalScore = QIrrTotalScoreResultEntity.irrTotalScoreResultEntity;
        BooleanBuilder totalScoreBuilder = new BooleanBuilder();
        totalScoreBuilder.and(qTotalScore.taskId.eq(id));
        Iterator<IrrTotalScoreResultEntity> totalScoreIterator = totalScoreResultService.getRepository()
                .findAll(totalScoreBuilder).iterator();
        Map<String, IrrTotalScoreResultEntity> totalScoreMap = new HashMap<String, IrrTotalScoreResultEntity>();
        while (totalScoreIterator.hasNext()) {
            IrrTotalScoreResultEntity totalScore = totalScoreIterator.next();
            String projResult = IrrUtil.formatNumber(scale, totalScore.getProjResult(), true);
            if (StringUtils.isNotBlank(projResult)) {
                totalScore.setProjResult(projResult);
            }
            totalScoreMap.put(totalScore.getId(), totalScore);
        }
        totalScoreResultService.update(totalScoreMap);
    }

    /* (non-Javadoc)
     * @see net.gbicc.app.irr.service.IrrTaskService#isProcessCollUser(java.lang.String)
     */
    @Override
    public Boolean isProcessCollUser(String loginName) throws Exception {
        if (StringUtils.isBlank(loginName)) {
            throw new IrrProcessException("登录名为空");
        }
        String sql = "SELECT COUNT(R.FD_ID) FROM FR_AA_USER U \r\n"
                + "INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID\r\n"
                + "INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID\r\n"
                + "WHERE U.FD_LOGINNAME='" + loginName + "' AND (R.FD_CODE = '"
                + IrrProcessEnum.IRR_PROCESS_COLL.getValue().toString() + "' OR R.FD_CODE LIKE '"
                + IrrProcessEnum.IRR_SIGN_PROCESS_COLL_PREFIX.getValue().toString() + "%')";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }

}
