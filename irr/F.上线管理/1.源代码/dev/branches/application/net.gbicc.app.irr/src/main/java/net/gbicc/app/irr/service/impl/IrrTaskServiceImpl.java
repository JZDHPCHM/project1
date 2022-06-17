package net.gbicc.app.irr.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.flowable.entity.TaskInfo;
import com.gbicc.aicr.system.flowable.service.FlowableProcessOperationService;
import com.gbicc.aicr.system.flowable.service.FlowableTaskService;
import com.gbicc.aicr.system.util.DateUtils;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrTaskRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrPromptInfoEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrChannelResultService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrTaskService;
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
		List<IrrSplitIndexEntity> examNullSplit = irrSplitIndexService.findSplitExamIsNullByCondition(IrrEnum.YESNO_Y.getValue());
		if(CollectionUtils.isNotEmpty(examNullSplit)) {
			throw new IrrProcessException("无法发起流程：已启用的指标审核人存在空!");
		}
		List<String> branchExamUser = irrSplitIndexService.findExamUserByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), null);
		if(CollectionUtils.isEmpty(branchExamUser)) {
			throw new IrrProcessException("无法发起流程：分公司指标没有配置审核人!");
		}
		/*防止一个机构有多个采集人，去重*/
		Map<String, Map<String, Object>> collUserMap = new HashMap<String, Map<String, Object>>();
		for(Map<String,Object> user : collUsers) {
			Object orgId = user.get("FD_ORG_ID");
			if(IrrUtil.strObjectIsEmpty(orgId)) {
				continue;
			}
			collUserMap.put(orgId.toString(), user);
		}
		/*组装子流程*/
		List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
		for(String orgId : collUserMap.keySet()) {
			Map<String,Object> user = collUserMap.get(orgId);
			if(MapUtils.isEmpty(user) || IrrUtil.strObjectIsEmpty(user.get("FD_LOGINNAME"))) {
				throw new IrrProcessException("无法发起流程：没有采集人！");
			}
			Map<String,Object> userMap = packSubProcessData(orgId, branchExamUser);
			if(userMap == null || MapUtils.isEmpty(userMap)) {
				continue;
			}
			userMap.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_CODE.getValue().toString(), user.get("FD_LOGINNAME"));
			users.add(userMap);
		}
		add(task);
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put(IrrProcessEnum.IRR_SUB_PROCESS_VAR_CODE.getValue().toString(), users);
		flowableProcessOperationService.startProcessByKey(SecurityUtil.getLoginName(), IrrProcessEnum.IRR_PROCESS_DEFINITION_KEY.getValue().toString(), 
				task.getId(), variables, null, false);
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
	public Map<String, Object> examBack(String taskId, String collUser, String comment)  throws Exception{
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("无法退回：流程任务ID为空");
		}
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_BACK_CODE.getValue());
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), collUser);
		flowableTaskService.completeTask(taskId, null, transientVariables, null,null,IrrProcessEnum.IRR_COMMENT_BACK_PREFIX.getValue()+comment);
		return IrrUtil.getMap(true,"退回成功！");
	}

	@Override
	public Map<String, Object> examSubmit(String taskId, String collUser, String comment) throws Exception{
		if(StringUtils.isBlank(taskId)) {
			throw new IrrProcessException("无法提交：流程任务ID为空");
		}
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_PROCESS_PATH_CODE.getValue().toString(), IrrProcessEnum.IRR_PROCESS_SUBMIT_CODE.getValue());
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), collUser);
		flowableTaskService.completeTask(taskId, null, transientVariables, null,null,IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue()+comment);
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
			Map<String, Object> user = packSubProcessData(orgIds.get(0), branchExamUser);
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
		/*计算源指标总结果(待开发)*/
		/*进行数据验证*/
		irrOrgResultService.summCalcDataVali(task);
		return IrrUtil.getMap(true, "指标汇总成功！");
	}

	@Override
	public Map<String, Object> projSumm(String id) throws Exception{
		return IrrUtil.getMap(true, "评估项目汇总成功！");
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
	public Map<String, Object> packSubProcessData(String id,List<String> branchExamUser) throws Exception {
		if(StringUtils.isBlank(id)) {
			throw new IrrProcessException("机构ID为空！");
		}
		Org org = orgService.findById(id);
		Map<String, Object> user = new HashMap<String,Object>();
		if(org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {//分公司
			if(CollectionUtils.isEmpty(branchExamUser)) {
				throw new IrrProcessException("分公司没有查到审核人！");
			}
			user.put(IrrProcessEnum.IRR_SUB_PROCESS_EXAM_CODE.getValue().toString(), branchExamUser);
			List<Map<String, Object>> branchReviewMapList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_REVIEW.getValue().toString(),id);
			if(CollectionUtils.isEmpty(branchReviewMapList) || MapUtils.isEmpty(branchReviewMapList.get(0)) || IrrUtil.strObjectIsEmpty(branchReviewMapList.get(0).get("FD_LOGINNAME"))) {
				throw new IrrProcessException(org.getName() + "没有查到复核人！");
			}
			user.put(IrrProcessEnum.IRR_SUB_PROCESS_REVIEW_CODE.getValue().toString(), branchReviewMapList.get(0).get("FD_LOGINNAME"));
		}else if(org.getName().contains(IrrProcessEnum.IRR_HEAD_SUFFIX.getValue().toString())) {//总部
			/*判断总部的是否有需要采集的指标*/
			List<IrrSplitIndexEntity> list = irrSplitIndexService.findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), null,
					IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_HEAD.getValue(), id);
			if(CollectionUtils.isEmpty(list)) {
				return null;
			}
			List<String> headExamUsers = irrSplitIndexService.findExamUserByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_HEAD.getValue(), id);
			if(CollectionUtils.isEmpty(headExamUsers)) {
				throw new IrrProcessException(org.getName() + "没有查到审核人！");
			}
			user.put(IrrProcessEnum.IRR_SUB_PROCESS_EXAM_CODE.getValue().toString(), headExamUsers);
			List<Map<String,Object>> reviewUserList = findRoleUserAndOrgByCondition(IrrProcessEnum.IRR_PROCESS_REVIEW.getValue().toString()+org.getCode(),null);
			if(CollectionUtils.isEmpty(reviewUserList) || MapUtils.isEmpty(reviewUserList.get(0)) || IrrUtil.strObjectIsEmpty(reviewUserList.get(0).get("FD_LOGINNAME"))) {
				throw new IrrProcessException(org.getName() + "没有查到复核人！");
			}
			user.put(IrrProcessEnum.IRR_SUB_PROCESS_REVIEW_CODE.getValue().toString(), reviewUserList.get(0).get("FD_LOGINNAME"));
		}
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
		/*保存分公司渠道结果*/
		irrChannelResultService.summChannelResult(task.getId());
		List<IrrOrgResultEntity> branchResult = new ArrayList<IrrOrgResultEntity>();
		/*保存分公司本身的机构结果*/
		List<IrrOrgResultEntity> myselfResult = irrOrgResultService.summMyselfResult(task.getId(),IrrEnum.INDEX_LEVEL_BRANCH.getValue());
		if(!CollectionUtils.isEmpty(myselfResult)) {
			branchResult.addAll(myselfResult);
		}
		/*查询分公司指标具有公式的指标*/
		List<IrrIndexInfoEntity> branchEvalIndexList = irrIndexInfoService.findFormulaIndexByCondition(IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), IrrEnum.YESNO_Y.getValue());
		/*查询分公司不适用的指标*/
		IrrIndexInfoEntity indexInfoExam = new IrrIndexInfoEntity();
		indexInfoExam.setIndexStatus(IrrEnum.INDEX_STATUS_ENABLE.getValue());
		indexInfoExam.setIsApplicabie(IrrEnum.YESNO_N.getValue());
		indexInfoExam.setIndexLevel(IrrEnum.INDEX_LEVEL_BRANCH.getValue());
		List<IrrIndexInfoEntity> branchDisableList = irrIndexInfoService.fetch(indexInfoExam, IrrQueryParameter.getIrrDefaultQP());
		/*计算分公司不适用的指标*/
		List<IndexValueDTO> branchDisableValue = irrIndexInfoService.summIndexDisable(branchDisableList);
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
				//采集渠道的结果相加
				List<IndexValueDTO> branchChannelNumResult = irrChannelResultService.sumBranchChannelIndexResult(task.getId(),org.get("FD_ID").toString());
				if(!CollectionUtils.isEmpty(branchChannelNumResult)) {
					result.addAll(branchChannelNumResult);
				}
				//计算分公司的具有公式的指标
				List<IndexValueDTO> branchEvalIndexValue = irrIndexInfoService.summIndexNum(branchEvalIndexList, branchChannelNumResult);
				if(!CollectionUtils.isEmpty(branchEvalIndexValue)) {
					result.addAll(branchEvalIndexValue);
				}
				if(!CollectionUtils.isEmpty(branchDisableValue)) {
					result.addAll(branchDisableValue);
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
			}
		}
		/*计算得分(待开发)*/
		return branchResult;
	}

	@Override
	public List<IrrOrgResultEntity> headIndexSumm(IrrTaskEntity task) throws Exception {
		if(task == null || StringUtils.isBlank(task.getId())) {
			return null;
		}
		List<IrrOrgResultEntity> headResult = new ArrayList<IrrOrgResultEntity>();
		List<IndexValueDTO> result = new ArrayList<IndexValueDTO>();
		/*保存总公司部门结果(不包含总公司渠道部门)*/
		List<IrrOrgResultEntity> deptResult = irrOrgResultService.summHeadDeptResult(task.getId());
		if(!CollectionUtils.isEmpty(deptResult)) {
			headResult.addAll(deptResult);
			result.addAll(orgResultTransIndexValue(deptResult));
		}
		/*保存总公司本身的机构结果*/
		List<IrrOrgResultEntity> myselfResult = irrOrgResultService.summMyselfResult(task.getId(),IrrEnum.INDEX_LEVEL_HEAD.getValue());
		if(!CollectionUtils.isEmpty(myselfResult)) {
			headResult.addAll(myselfResult);
			result.addAll(orgResultTransIndexValue(myselfResult));
		}
		/*总公司分配到渠道所属部门为其他的采集指标相加*/
		List<IndexValueDTO> channelOtherDeptSum = irrOrgResultService.sumHeadChannelDeptResult(task.getId());
		if(!CollectionUtils.isEmpty(channelOtherDeptSum)) {
			result.addAll(channelOtherDeptSum);
		}
		/*总公司渠道部门采集指标相加*/
		List<IndexValueDTO> channelDeptSum = irrIndexInfoService.sumHeadChannelIndex(task.getId());
		if(!CollectionUtils.isEmpty(channelDeptSum)) {
			result.addAll(channelDeptSum);
		}
		/*查询总公司不适用的指标*/
		IrrIndexInfoEntity indexInfoExam = new IrrIndexInfoEntity();
		indexInfoExam.setIndexStatus(IrrEnum.INDEX_STATUS_ENABLE.getValue());
		indexInfoExam.setIsApplicabie(IrrEnum.YESNO_N.getValue());
		indexInfoExam.setIndexLevel(IrrEnum.INDEX_LEVEL_HEAD.getValue());
		List<IrrIndexInfoEntity> disableList = irrIndexInfoService.fetch(indexInfoExam, IrrQueryParameter.getIrrDefaultQP());
		/*计算总公司不适用的指标*/
		List<IndexValueDTO> disableValue = irrIndexInfoService.summIndexDisable(disableList);
		if(!CollectionUtils.isEmpty(disableValue)) {
			result.addAll(disableValue);
		}
		/*查询总公司具有公式的指标*/
		List<IrrIndexInfoEntity> evalIndexList = irrIndexInfoService.findFormulaIndexByCondition(IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_HEAD.getValue(), IrrEnum.YESNO_Y.getValue());
		/*计算总公司具有公式的指标*/
		List<IndexValueDTO> evalValue = irrIndexInfoService.summIndexNum(evalIndexList, result);
		if(!CollectionUtils.isEmpty(evalValue)) {
			result.addAll(evalValue);
		}
		/*保存总公司上报结果*/
		Org rootOrg = new Org();
		rootOrg.setCode(IrrEnum.ORG_ROOT_CODE.getValue());
		List<Org> orgList = orgService.fetch(rootOrg, IrrQueryParameter.getIrrDefaultQP());
		if(!CollectionUtils.isEmpty(orgList)) {
			rootOrg = orgList.get(0);
		}
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
		/*计算得分(待开发)*/
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
		IrrTaskEntity examTask = new IrrTaskEntity();
		if(StringUtils.isNotBlank(taskStatus)) {
			examTask.setTaskStatus(taskStatus);
		}
		List<IrrTaskEntity> list = fetch(examTask, IrrQueryParameter.getIrrDefaultQP());
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String,String> map = new HashMap<String,String>();
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
	
}
