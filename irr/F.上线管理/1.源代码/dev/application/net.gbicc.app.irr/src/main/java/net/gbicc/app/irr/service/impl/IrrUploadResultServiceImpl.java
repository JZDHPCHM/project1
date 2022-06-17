package net.gbicc.app.irr.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.wsp.framework.core.util.IterableUtil;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.entity.QOrg;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemDictionaryServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.flowable.service.FlowableTaskService;
import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;
import net.gbicc.app.irr.jpa.entity.QIrrUploadResultEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrUploadResultRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.dto.IrrSysIndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrPromptInfoEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrAuthTaskLogService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.gbicc.app.irr.service.IrrUploadResultService;
import net.sf.json.JSONObject;

/**
* 上传结果
*/
@Service
@Transactional
public class IrrUploadResultServiceImpl extends DaoServiceImpl<IrrUploadResultEntity, String, IrrUploadResultRepository> 
	implements IrrUploadResultService {

	private static final Logger LOG = LogManager.getLogger(IrrUploadResultServiceImpl.class);
	@Autowired private IrrSplitIndexService irrSplitIndexService;
	@Resource(name="frSystemDictionaryService")
	private SystemDictionaryServiceImpl frSystemDictionaryService;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Resource(name="frUserService")
	private UserService userService;
	@Autowired private IrrTaskService irrTaskService;
	@Autowired private IrrIndexInfoService irrIndexInfoService;
	@Autowired private FlowableTaskService flowableTaskService;
	@Autowired private OrgService orgService;
    @Autowired
    private IrrAuthTaskLogService irrAuthTaskLogService;

	@Override
    public Workbook createIrrCollTemplate(String processTaskId) throws Exception {
        String trueUserId = flowableTaskService.getTaskTrueAssigneeUserId(processTaskId, SecurityUtil.getLoginName());
        if (StringUtils.isBlank(trueUserId)) {
            throw new IrrProcessException("下载失败：无法查询出流程任务的真实用户ID！");
        }
		Workbook hfwb = new HSSFWorkbook();
		Sheet sheet = hfwb.createSheet(IrrTemplateEnum.IRR_TEMPLATE_SHEET_NAME.getValue());
		Map<String, Map<String,Object>> splitSourceIndexInfo = irrSplitIndexService.findSplitSourceIndexInfo();
        List<IrrSplitIndexEntity> list = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(trueUserId,
                IrrEnum.COLL_WAY_HAND.getValue());
		if(list == null || list.size() <= 0) {
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue(IrrEnum.COLL_EMPTY_INDEX_INFO.getValue().toString());
		}else {
            packDownloadSheet(hfwb, sheet, list, splitSourceIndexInfo, trueUserId);
		}
		/*如果是总公司，并且有填报的分公司指标，生成分公司采集的sheet*/
        Org userOrg = findOrgByCondition(trueUserId);
		if(userOrg != null && userOrg.getName().contains(IrrProcessEnum.IRR_HEAD_SUFFIX.getValue().toString())) {//总部
			List<IrrSplitIndexEntity> branchSplitList = irrSplitIndexService.findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.COLL_WAY_HAND.getValue(), 
					IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), userOrg.getId());
			if(CollectionUtil.isNotEmpty(branchSplitList)) {//有分公司指标
				List<Org> orgList = orgService.getRepository().findAll();
				Collections.sort(orgList, new Comparator<Org>() {
					@Override
					public int compare(Org org1, Org org2) {
						int diff=Integer.parseInt(org1.getCode())-Integer.parseInt(org2.getCode());
						if(diff>0) {
							return 1;
						}else if(diff<0) {
							return -1;
						}
						return 0;
					}
				});
				if(CollectionUtil.isEmpty(orgList)) {//没有机构
					return hfwb;
				}
				Map<String, String> branchXBRLOrgExcludeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.BRANCH_XBRL_ORG_EXCLUDE.getValue(), Locale.CHINA);
				for(Org org : orgList) {
					//循环分公司机构
					if(org == null || !org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {
						continue;
					}
					//判断是否为非上报分公司
					if(!IrrUtil.strObjectIsEmpty(branchXBRLOrgExcludeMap.get(org.getCode()))) {
						continue;
					}
					Sheet branchSheet = hfwb.createSheet(org.getCode()+IrrEnum.SEPARATOR.getValue()+org.getName());
                    packDownloadSheet(hfwb, branchSheet, branchSplitList, splitSourceIndexInfo, trueUserId);
				}
			}
		}
		return hfwb;
	}

	@Override
    public void readCollUploadFile(Workbook wb, String taskId, String processTaskId) throws Exception {
        String trueUserId = flowableTaskService.getTaskTrueAssigneeUserId(processTaskId, SecurityUtil.getLoginName());
        String assignee = flowableTaskService.findTaskAssignee(processTaskId);
        if (StringUtils.isBlank(trueUserId) || StringUtils.isBlank(assignee)) {
            throw new IrrProcessException("无法上传：找不到任务的真实所属人！");
        }
		Sheet sheet = wb.getSheetAt(0);
        List<Org> orgs = userService.findById(trueUserId).getOrgs();
		Org org = new Org();
		if(CollectionUtils.isNotEmpty(orgs)) {
			org = orgs.get(0);
		}
		IrrTaskEntity task = irrTaskService.findById(taskId);
        List<IrrSplitIndexEntity> splitIndexList = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(trueUserId,
                IrrEnum.COLL_WAY_HAND.getValue());
		Map<String,IrrSplitIndexEntity> splitMap = irrSplitIndexService.getSplitIndexMap(splitIndexList);
		List<IrrUploadResultEntity> uploadList = new ArrayList<IrrUploadResultEntity>();
        uploadList.addAll(packUploadResultData(task, splitMap, sheet, org, assignee));
		/*如果是总公司上传分公司的指标，则继续读取*/
		Integer sheetNum = wb.getNumberOfSheets();
		if(sheetNum > 1) {
			for(int i=1;i<sheetNum;i++) {
				Sheet branchSheet = wb.getSheetAt(i);
				String sheetName = branchSheet.getSheetName();
				if(IrrUtil.strObjectIsEmpty(sheetName)) {
					continue;
				}
				String[] orgInfo = sheetName.split(IrrEnum.SEPARATOR.getValue());
                QOrg qOrg = QOrg.org;
                BooleanBuilder orgBuilder = new BooleanBuilder();
                orgBuilder.and(qOrg.code.eq(orgInfo[0]));
                List<Org> branchOrgList = IterableUtil.toList(orgService.getRepository().findAll(orgBuilder));
				if(CollectionUtil.isEmpty(branchOrgList) || branchOrgList.get(0) == null) {
					continue;
				}
				List<IrrSplitIndexEntity> branchSplitList = irrSplitIndexService.findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.COLL_WAY_HAND.getValue(), 
						IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), org.getId());
                uploadList.addAll(packUploadResultData(task, irrSplitIndexService.getSplitIndexMap(branchSplitList),
                        branchSheet, branchOrgList.get(0), assignee));
			}
		}
		/*数据验证*/
		uploadList = indexDataValidation(uploadList,task.getTaskBatch());
		/*采集系统数据*/
        //规则：如果已经有系统数据了，那么不进行覆盖
		QIrrUploadResultEntity qUploadResult = QIrrUploadResultEntity.irrUploadResultEntity;
        BooleanBuilder uploadBuilder = new BooleanBuilder();
        uploadBuilder.and(qUploadResult.taskId.eq(taskId));
        uploadBuilder.and(qUploadResult.creator.eq(assignee));
        uploadBuilder.and(qUploadResult.collWay.eq(IrrEnum.COLL_WAY_SYS.getValue()));
        boolean isHaveSysData = repository.exists(uploadBuilder);
        if (isHaveSysData) {
            //已经存在系统数据，手工上传的覆盖，系统加载的不覆盖。由此只删除手工上传的。
            repository.deleteByTaskIdAndCreatorAndCollWay(taskId, assignee,
                    IrrEnum.COLL_WAY_HAND.getValue());
        } else {
            //不存在系统数据
            List<IrrUploadResultEntity> sysResultList = uploadSysResult(taskId, trueUserId);
            if (CollectionUtils.isNotEmpty(sysResultList)) {
                uploadList.addAll(sysResultList);
            }
            //删除当前期次当前人的采集数据
            getRepository().deleteByTaskIdAndCreator(taskId, assignee);
        }
        /*格式化上传的数值结果*/
        List<IrrUploadResultEntity> saveResultList = add(formatCollNumberIndexResult(uploadList));
        //如果是授权，则更新创建人
        Boolean isSame = flowableTaskService.taskAssigneeIsSame(processTaskId, SecurityUtil.getLoginName());
        if (isSame != null && !isSame) {
            for (IrrUploadResultEntity temp : saveResultList) {
                temp.setCreator(assignee);
                update(temp.getId(), temp);
            }
        }
        //授权日志
        Task processTask = flowableTaskService.getTaskService().createTaskQuery().taskId(processTaskId).singleResult();
        irrAuthTaskLogService.saveAuthTaskLog(taskId, processTask, "上传采集数据");
	}

	/**
	 * 数据验证
	 * @param list 需验证数据
	 * @param taskBatch 当前期次
	 * 规则：指标的值与上期次相同指标的值进行对比
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public List<IrrUploadResultEntity> indexDataValidation(List<IrrUploadResultEntity> list,String taskBatch) throws Exception{
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Map<String, String> dataValidMap = frSystemDictionaryService
                .getDictionaryMap(IrrEnum.INDEX_DATA_VALID_PARAM_CODE.getValue(), Locale.CHINA);
		String maxLimitParam = dataValidMap.get(IrrEnum.INDEX_DATA_VALID_PARAM_MAX.getValue());
		if(StringUtils.isBlank(maxLimitParam)) {
			maxLimitParam = IrrEnum.DEFAULT_MAX_LIMIT_PARAM.getValue();
		}
		maxLimitParam = maxLimitParam.replace(IrrEnum.MAX_LIMIT_PARAM_UNIT_CODE.getValue(), "");
        Map<String, IrrUploadResultEntity> previousUpload = getUploadMap(
                findUploadResult(IrrUtil.getPreviousPeriod(taskBatch)));
		for(int i=0;i<list.size();i++) {
			IrrUploadResultEntity upload = list.get(i);
            IrrUploadResultEntity previous = previousUpload.get(upload.getSplitCode() + upload.getCollOrgId());
			if(previous == null) {
				continue;
			}
			upload.setSplitResultQ1(previous.getSplitResultQ2());
			upload.setSplitScoreQ1(previous.getSplitScoreQ2());
			Object dataVali = calcDataVali(previous.getSplitResultQ2(),upload.getSplitResultQ2());
			if(dataVali == null || "".equals(dataVali)) {
				continue;
			}
			upload.setDataVali(new BigDecimal(dataVali.toString()));
			Boolean isFill = dataValiIsFill(dataVali.toString(),maxLimitParam);
			if(isFill) {
				upload.setIsFillReason(IrrEnum.YESNO_Y.getValue());
			}else {
				upload.setIsFillReason(IrrEnum.YESNO_N.getValue());
			}
		}
		return list;
	}
	
	@Override
	public Object calcDataVali(String valiValue,String beValiValue) {
		if(StringUtils.isBlank(valiValue) || StringUtils.isBlank(beValiValue)
				|| !IrrUtil.isIntOrDouble(valiValue) || !IrrUtil.isIntOrDouble(beValiValue)) {
			return null;
		}
		Object dataVali = null;
		Double uploadQ2D = Double.valueOf(beValiValue);
		Double previousQ2D = Double.valueOf(valiValue);
		if(!uploadQ2D.equals(0D) && previousQ2D.equals(0D)) {
			dataVali = "1";
		}else {
			if(uploadQ2D.equals(0D) && previousQ2D.equals(0D)) {
				dataVali = "0";
			}else {
				dataVali = JavaJsEvalUtil.evalStr(beValiValue+"/"+valiValue+"-1");
			}
		}
		return dataVali;
	}
	
	@Override
	public Boolean dataValiIsFill(String compareParam,String diffParam) throws Exception {
		Boolean flag = false;
		if(StringUtils.isBlank(compareParam)) {
			return flag;
		}
		Object dataDiff = JavaJsEvalUtil.evalStr(Math.abs(Double.valueOf(compareParam))+"*100-"+diffParam);
		if(Double.valueOf(dataDiff.toString()) > 0D) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 生成系统指标采集实例
	 * @param taskId 评估计划ID
	 * @param collUserId 采集人ID
	 * @return
	 */
	@Transactional
	public List<IrrUploadResultEntity> uploadSysResult(String taskId,String collUserId) throws Exception{
		List<IrrUploadResultEntity> list = new ArrayList<IrrUploadResultEntity>();
		List<IrrSplitIndexEntity> sysAllList = new ArrayList<IrrSplitIndexEntity>();
		List<IrrSplitIndexEntity> sysSplitList = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(collUserId, IrrEnum.COLL_WAY_SYS.getValue());
		if(CollectionUtils.isNotEmpty(sysSplitList)) {
			sysAllList.addAll(sysSplitList);
		}
		/*如果为总公司部门，查询是否需要获取分公司指标数据*/
		Org org = findOrgByCondition(collUserId);
		if(org != null && !IrrUtil.strObjectIsEmpty(org.getName()) 
				&& org.getName().contains(IrrProcessEnum.IRR_HEAD_SUFFIX.getValue().toString())) {
			List<IrrSplitIndexEntity> branchSplitIndexList = irrSplitIndexService.findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.COLL_WAY_SYS.getValue(), 
					IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(), org.getId());
			if(CollectionUtil.isNotEmpty(branchSplitIndexList)) {
				/*每个总公司指标生成需要获取的各分公司指标数据*/
				for(IrrSplitIndexEntity branchSplit : branchSplitIndexList) {
					List<IrrSplitIndexEntity> branchSysSplitIndexList = new ArrayList<IrrSplitIndexEntity>();
					List<Org> allOrg = orgService.getRepository().findAll();
					if(CollectionUtil.isNotEmpty(allOrg)) {
						Map<String, String> branchXBRLOrgExcludeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.BRANCH_XBRL_ORG_EXCLUDE.getValue(), Locale.CHINA);
						for(Org tempOrg : allOrg) {
							if(tempOrg != null && !IrrUtil.strObjectIsEmpty(tempOrg.getName())
									&& tempOrg.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())
									&& IrrUtil.strObjectIsEmpty(branchXBRLOrgExcludeMap.get(tempOrg.getCode()))) {
								IrrSplitIndexEntity branchTemp = new IrrSplitIndexEntity();
								BeanUtils.copyProperties(branchTemp, branchSplit);
								branchTemp.setOrgCode(tempOrg.getCode());
								branchTemp.setOrgId(tempOrg.getId());
								branchTemp.setOrgName(tempOrg.getName());
								branchSysSplitIndexList.add(branchTemp);
							}
						}
					}
					sysAllList.addAll(branchSysSplitIndexList);
				}
			}
		}
		User collUser = userService.findById(collUserId);
		IrrTaskEntity task = irrTaskService.findById(taskId);
		/*指标结果类型Map*/
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		for(IrrSplitIndexEntity split : sysAllList) {
			IrrUploadResultEntity sysUpload = new IrrUploadResultEntity();
			sysUpload.setSplitId(split.getId());
			sysUpload.setSplitName(split.getSplitName());
			sysUpload.setSplitCode(split.getSplitCode());
			sysUpload.setTaskBatch(task.getTaskBatch());
			sysUpload.setTaskId(taskId);
			sysUpload.setCollWay(split.getCollWay());
			if(split.getOrgId()==null||split.getOrgId().equals("")) {
				sysUpload.setCollOrgId(org.getId());
				sysUpload.setCollOrgName(org.getName());
			}else {
				sysUpload.setCollOrgId(split.getOrgId());
				sysUpload.setCollOrgName(split.getOrgName());
			}			
			sysUpload.setIsHandChange(IrrEnum.YESNO_N.getValue());
			sysUpload.setResultTypeName(resultTypeMap.get(split.getResultType()));
			sysUpload.setCollUserName(collUser.getUserName());
			sysUpload.setIsFillReason(IrrEnum.YESNO_N.getValue());
            sysUpload.setCreator(collUser.getLoginName());
			list.add(sysUpload);
		}
		/*加载系统数据*/
		list = loadSysData(list);
		/*验证系统数据*/
		list = indexDataValidation(list,task.getTaskBatch());
		return list;
	}
	@Override
	public void getBranchIndexValue(String taskId) throws Exception {
		StringBuffer remark=null;
		List<String> listIndexCode=new ArrayList<String>();
		listIndexCode.add("OR040052900");
		listIndexCode.add("OR040062900");
		listIndexCode.add("OR040072900");
		listIndexCode.add("OR040090000");
		listIndexCode.add("OR040100000");
		listIndexCode.add("OR130114082");
		listIndexCode.add("OR130124082");
		listIndexCode.add("OR130144082");
		listIndexCode.add("OR130154082");
		Map<String, String> mapChannel=new HashMap<String,String>();
		mapChannel.put("CS", "客服");
		mapChannel.put("FC", "个险");
		mapChannel.put("GP", "团险");
		mapChannel.put("BK", "银保");
		mapChannel.put("AD", "多元");
		mapChannel.put("RP", "续期");
		Set<String> setKey=mapChannel.keySet();
		for (String indexCode : listIndexCode) {
            String sql = "SELECT * FROM T_IRR_UPLOAD_RESULT WHERE SPLIT_CODE='" + indexCode + "' AND TASK_ID = '"
                    + taskId + "'";
			List<IrrUploadResultEntity> listIrrUpload=jdbcTemplate.query(sql,new BeanPropertyRowMapper<IrrUploadResultEntity>(IrrUploadResultEntity.class));
			if (CollectionUtils.isNotEmpty(listIrrUpload)) {
				for (IrrUploadResultEntity irrUploadResultEntity : listIrrUpload) {
					String subIndexCode=indexCode.substring(0, 7).trim();
					remark=new StringBuffer();
                    String dateSql = "SELECT * FROM E_ADS_IRR_INDEX_VALUE WHERE INDEX_ID = '" + subIndexCode
                            + "' ORDER BY LOAD_DT DESC";
					/*最新加载日期*/
					List<IrrSysIndexValueDTO> loadDateList=jdbcTemplate.query(dateSql, new BeanPropertyRowMapper<IrrSysIndexValueDTO>(IrrSysIndexValueDTO.class));
					if(CollectionUtils.isNotEmpty(loadDateList)) {
						String loadDate= loadDateList.get(0).getLoadDt().toString().substring(0, 10);
						for (String key : setKey) {												
							String sql1="SELECT * FROM E_ADS_IRR_INDEX_VALUE II INNER JOIN FR_AA_ORG ORG ON II.ORG_ID=ORG.FD_CODE "
                                    + "WHERE INDEX_ID = '" + subIndexCode + "' AND ORG.FD_ID = '"
                                    + irrUploadResultEntity.getCollOrgId().trim() + "'  AND II.CHANNEL_ID = " + "'"
                                    + key
                                    + "' AND II.CHANNEL_ID IS NOT NULL AND to_char(II.LOAD_DT,'yyyy-MM-dd HH24:mi:ss') LIKE '%"
                                    + loadDate + "%' AND II.EVAL_DATE = '" + irrUploadResultEntity.getTaskBatch().trim()
                                    + "'";
							List<IrrSysIndexValueDTO> irrSysIndexValueDTOList=jdbcTemplate.query(sql1, new BeanPropertyRowMapper<IrrSysIndexValueDTO>(IrrSysIndexValueDTO.class));
							if(CollectionUtils.isNotEmpty(irrSysIndexValueDTOList)) {
								IrrSysIndexValueDTO irrSysIndexValueDTO=irrSysIndexValueDTOList.get(0);
								remark.append(mapChannel.get(key).toString()+":"+irrSysIndexValueDTO.getIndexValue()+",");
							}
						}
						if(remark.toString().length()>0) {
							remark.substring(0, (remark.toString()).length()-1);
							irrUploadResultEntity.setRemark(remark.toString());	
                            String updateSql = "UPDATE T_IRR_UPLOAD_RESULT SET REMARK = '" + remark + "' WHERE ID = '"
                                    + irrUploadResultEntity.getId() + "'";
							jdbcTemplate.execute(updateSql);
						}
					}
			  }
			}
		}
	}
	@Override
    public List<IrrUploadResultEntity> findUploadResult(String taskBatch) throws Exception {
        QIrrUploadResultEntity qUploadResult = QIrrUploadResultEntity.irrUploadResultEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUploadResult.taskBatch.eq(taskBatch));
        return IterableUtil.toList(repository.findAll(builder));
	}

	@Override
	public Map<String, IrrUploadResultEntity> getUploadMap(List<IrrUploadResultEntity> list) {
		Map<String, IrrUploadResultEntity> map = new HashMap<String,IrrUploadResultEntity>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(IrrUploadResultEntity upload : list) {
                map.put(upload.getSplitCode() + upload.getCollOrgId(), upload);
			}
		}
		return map;
	}

	@Override
	public IrrUploadResultEntity editUploadResult(String id, String splitResultQ2, String dataDesc) throws Exception {
		if(StringUtils.isBlank(id)) {
			return null;
		}
		IrrUploadResultEntity dbEntity = findById(id);
		if(StringUtils.isNotBlank(splitResultQ2)) {
            dbEntity.setSplitResultQ2(splitResultQ2.trim());
			dbEntity.setIsHandChange(IrrEnum.YESNO_Y.getValue());
            IrrUploadResultEntity previous = getRepository().findBySplitCodeAndTaskBatchAndCollOrgId(
                    dbEntity.getSplitCode(), IrrUtil.getPreviousPeriod(dbEntity.getTaskBatch()),
                    dbEntity.getCollOrgId());
			if(previous != null) {
				Object dataVali = calcDataVali(previous.getSplitResultQ2(), dbEntity.getSplitResultQ2());
				if(dataVali != null && !"".equals(dataVali)) {
					dbEntity.setDataVali(new BigDecimal(dataVali.toString()));
					Map<String, String> dataValidMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_DATA_VALID_PARAM_CODE.getValue(), Locale.CHINA);
					String maxLimitParam = dataValidMap.get(IrrEnum.INDEX_DATA_VALID_PARAM_MAX.getValue());
					if(StringUtils.isBlank(maxLimitParam)) {
						maxLimitParam = IrrEnum.DEFAULT_MAX_LIMIT_PARAM.getValue();
					}
					maxLimitParam = maxLimitParam.replace(IrrEnum.MAX_LIMIT_PARAM_UNIT_CODE.getValue(), "");
					Boolean flag = dataValiIsFill(dataVali.toString(), maxLimitParam);
					if(flag) {
					//if("OR120064082".equals(dbEntity.getSplitCode())) {
						dbEntity.setIsFillReason(IrrEnum.YESNO_Y.getValue());
					}else {
						dbEntity.setIsFillReason(IrrEnum.YESNO_N.getValue());
					}
				}
			}
			update(id, dbEntity);
		}
		if(StringUtils.isNotBlank(dataDesc)) {
			dbEntity.setDataDesc(dataDesc);
			update(id, dbEntity);
		}
		return dbEntity;
	}

	@Override
    public Map<String, Object> loadSysIndexDataByTaskId(String taskId, String processTaskId) throws Exception {
        String assignee = flowableTaskService.findTaskAssignee(processTaskId);
        if(StringUtils.isBlank(assignee)) {
            throw new IrrProcessException("加载失败：查询不到流程任务的真实人！");
        }
        QIrrUploadResultEntity qUploadResult = QIrrUploadResultEntity.irrUploadResultEntity;
        BooleanBuilder uploadResultBuilder = new BooleanBuilder();
        uploadResultBuilder.and(qUploadResult.taskId.eq(taskId));
        uploadResultBuilder.and(qUploadResult.collWay.eq(IrrEnum.COLL_WAY_SYS.getValue()));
        uploadResultBuilder.and(qUploadResult.creator.eq(assignee));
        //加载系统数据，全部覆盖
        //upload.setIsHandChange(IrrEnum.YESNO_N.getValue());
        List<IrrUploadResultEntity> sysUpload = IterableUtil.toList(repository.findAll(uploadResultBuilder));
		if(CollectionUtils.isEmpty(sysUpload)) {
			return IrrUtil.getMap(false, "无需要加载的系统数据！");
		}
        sysUpload = indexDataValidation(formatCollNumberIndexResult(loadSysData(sysUpload)),
                sysUpload.get(0).getTaskBatch());
		for(IrrUploadResultEntity sys : sysUpload) {
			update(sys.getId(),sys);
		}
		/*被指定指标添加备注*/
		getBranchIndexValue(sysUpload.get(0).getTaskId());
        //授权日志
        Task task = flowableTaskService.getTaskService().createTaskQuery().taskId(processTaskId).singleResult();
        irrAuthTaskLogService.saveAuthTaskLog(taskId, task, "加载系统数据");
		return IrrUtil.getMap(true, "加载成功!");
	}

	@Override
	public List<IrrUploadResultEntity> loadSysData(List<IrrUploadResultEntity> list) throws Exception{
		if(CollectionUtils.isEmpty(list) || ObjectUtils.isEmpty(list.get(0))) {
			return list;
		}
		/*查询当前评估期的系统指标值*/
		List<IrrSysIndexValueDTO> sysIndexValue = findIrrSysIndexValueByEvalDate(list.get(0).getTaskBatch());
		if(CollectionUtils.isEmpty(sysIndexValue)) {
			return list;
		}
		Map<String, IrrSysIndexValueDTO> sysValueMap = new HashMap<String, IrrSysIndexValueDTO>();
		for(IrrSysIndexValueDTO sysValue : sysIndexValue) {
			String key = sysValue.getIndexId();
			if(StringUtils.isNotBlank(sysValue.getOrgId())) {
				key += sysValue.getOrgId();
			}
			if(StringUtils.isNotBlank(sysValue.getChannelId())) {
				key += sysValue.getChannelId();
			}
			sysValueMap.put(key, sysValue);
		}
		/*获取所有机构组成Map*/
		List<Org> orgList = orgService.getRepository().findAll();
		Map<String, String> orgMap = new HashMap<String, String>();
		if(CollectionUtil.isNotEmpty(orgList)) {
			for(Org tempOrg : orgList) {
				orgMap.put(tempOrg.getId(), tempOrg.getCode());
			}
		}
		for(IrrUploadResultEntity upload : list) {
			if(upload == null) {
				continue;
			}
			/*查询与系统指标值相对应的指标编码和拆分层级*/
			List<Map<String, Object>> indexInfoList = irrIndexInfoService.findIndexInfoBySplitId(upload.getSplitId());
			if(CollectionUtils.isEmpty(indexInfoList) || MapUtils.isEmpty(indexInfoList.get(0))) {
				continue;
			}
			Object indexLevel = indexInfoList.get(0).get("INDEX_LEVEL");
			Object indexCode = indexInfoList.get(0).get("INDEX_CODE");
			if(IrrUtil.strObjectIsEmpty(indexLevel) || IrrUtil.strObjectIsEmpty(indexCode)) {
				continue;
			}
			Object channelFlag = indexInfoList.get(0).get("CHANNEL_FLAG");
			IrrSysIndexValueDTO value = null;
			if(IrrEnum.INDEX_LEVEL_BRANCH.getValue().equals(indexLevel)) {//分公司
				String branchKey = indexCode.toString();
				if(!IrrUtil.strObjectIsEmpty(upload.getCollOrgId())) {
					if(!IrrUtil.strObjectIsEmpty(orgMap.get(upload.getCollOrgId()))) {
						branchKey += orgMap.get(upload.getCollOrgId());
					}
				}
				if(!IrrUtil.strObjectIsEmpty(channelFlag)) {
					branchKey += channelFlag.toString();
				}
				value = sysValueMap.get(branchKey);
			}else if(IrrEnum.INDEX_LEVEL_HEAD.getValue().equals(indexLevel)){//总部
				if(IrrUtil.strObjectIsEmpty(channelFlag)) {
					value = sysValueMap.get(indexCode.toString()+IrrEnum.ORG_ROOT_CODE.getValue());
				}else {
					value = sysValueMap.get(indexCode.toString()+IrrEnum.ORG_ROOT_CODE.getValue()+channelFlag.toString());
				}
			}
            if (value != null && StringUtils.isNotBlank(value.getIndexValue().toString())) {
                upload.setSplitResultQ2(value.getIndexValue().toString().trim());
            } else {
                upload.setSplitResultQ2(IrrEnum.DEFAULT_NUM.getValue());
			}
		}
		return list;
	}

	@Override
	public List<IrrSysIndexValueDTO> findIrrSysIndexValueByEvalDate(String evalDate) {
        String sql = "SELECT * FROM E_ADS_IRR_INDEX_VALUE WHERE EVAL_DATE='" + evalDate + "' ORDER BY LOAD_DT";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrSysIndexValueDTO>(IrrSysIndexValueDTO.class));
	}

	@Override
    public Map<String, Object> collSubmit(String taskId, String id) throws Exception {
		Map<String, Object> transientVariables = new HashMap<String, Object>();
        Task task = flowableTaskService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        String assignee = task.getAssignee();
        //流程查看页面机构需要固话，以防止人员离职或者岗位变动，造成流程页面获取的机构不是变动之前的，
        //在此将机构名称一并存入到采集意见中，在流程页面中进行处理，使获得的机构是变动之前的。
        //新增格式为：(通过)采集完成*组织机构名称
        
        QueryParameter qu = new QueryParameter();
		qu.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qu.setTextMatchStyle(TextMatchStyle.substring);
        User examUser = new User();
		examUser.setLoginName(assignee);
		List<User> users = userService.fetch(examUser, qu);
		String comms = IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue()+"采集完成";
		if(!CollectionUtils.isEmpty(users) && users.get(0) != null) {
			comms+="*"+users.get(0).getOrgs().get(0).getName();
		}
		System.out.println("~~~~~~comms:"+comms);
		//return null;
        transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), task.getAssignee());
		flowableTaskService.completeTask(taskId, null, transientVariables, null, null, comms);
        //授权日志
        irrAuthTaskLogService.saveAuthTaskLog(id, task, IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue() + "采集任务提交");
		return IrrUtil.getMap(true, IrrPromptInfoEnum.SUBMIT_SUCCESS.getValue());
	}

	@Override
	public List<Map<String, Object>> findUploadResultByCondition(String taskId, String collOrgId,
			String collUserLoginName) throws Exception {
		String sql = "SELECT II.ID INDEX_ID,II.INDEX_CODE,II.INDEX_NAME,SI.ID SPLIT_ID,SI.SPLIT_CODE,SI.SPLIT_NAME,SI.SPLIT_LEVEL,SI.ORG_ID,SI.ORG_CODE,SI.ORG_NAME,SI.CHANNEL_FLAG,UR.TASK_BATCH,UR.TASK_ID,\r\n" + 
				"UR.SPLIT_RESULT_Q1,UR.SPLIT_SCORE_Q1,UR.SPLIT_RESULT_Q2,UR.SPLIT_SCORE_Q2,UR.DATA_VALI,UR.DATA_DESC FROM T_IRR_SPLIT_INDEX SI INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID \r\n" + 
				"INNER JOIN T_IRR_UPLOAD_RESULT UR ON SI.ID=UR.SPLIT_ID WHERE UR.TASK_ID='"+taskId+"' AND UR.COLL_ORG_ID='"+collOrgId+"' AND UR.FD_CREATOR='"+collUserLoginName+"'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
    public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult, String sourceProjId,
            String sourceOrgId, Integer size, Integer page, String processTaskId, String processNode)
            throws Exception {
		/*分三个进行审核查询：
		 * 1、总公司：分公司需总公司采集的指标、总公司部门采集指标；（分配指标所属组织机构字段）
		 * 2、分公司：分公司采集指标；（分配指标所属机构字段为空）
		 * 3、会签：不同部门采集的指标（分配指标有所属机构并且属于会签流程指标）
		 * 以上可以根据当前人机构来进行判断
		 * */
        //查询此任务的真实所属机构
        String trueUserId = flowableTaskService.getTaskTrueAssigneeUserId(processTaskId, SecurityUtil.getLoginName());
        if (StringUtils.isBlank(trueUserId)) {
            return null;
        }
        Org currentUserOrg = irrTaskService.findOrgByUserId(trueUserId);
		if(currentUserOrg == null) {
			return null;
		}
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		StringBuffer sb = new StringBuffer("SELECT UR.* FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN FR_AA_ORG ORG ON UR.COLL_ORG_ID=ORG.FD_ID INNER JOIN T_IRR_INDEX_INFO INDE ON INDE.ID=SI.SOURCE_INDEX_ID ");
        //是否为会签节点标识
        boolean isSignNode = false;
        boolean isExamNode=false;
		if (IrrUtil.strObjectIsNotEmpty(currentUserOrg.getName())
                && currentUserOrg.getName().contains(IrrProcessEnum.IRR_HEAD_SUFFIX.getValue().toString())) {
            String rolePrefix = IrrProcessEnum.IRR_SIGN_PROCESS_EXAM_PREFIX.getValue().toString();
            if (IrrProcessEnum.IRR_PROCESS_NODE_COLL.getValue().equals(processNode)) {
                rolePrefix = IrrProcessEnum.IRR_SIGN_PROCESS_COLL_PREFIX.getValue().toString();
            }
            Map<String, Object> roleMap = irrTaskService
                    .findRoleByCondition(rolePrefix, trueUserId);
			//总公司部门
			if(MapUtils.isNotEmpty(roleMap)) {
				//会签子流程
				if(IrrUtil.strObjectIsEmpty(roleMap.get("FD_CODE"))) {
					return null;
				}
				String roleCode=roleMap.get("FD_CODE").toString();
				if(roleCode.contains(IrrProcessEnum.IRR_SIGN_PROCESS_EXAM_PREFIX.getValue().toString())) {//判断是否是会签审核节点
					isExamNode=true;
				}
                isSignNode = true;
                //2020-10-10 去掉限制,目前只有一个领导，要求看到财务，会计部门的数据
                //会签审核角色能看到2个部门数据
				//sb.append("WHERE SI.ORG_ID IN (SELECT DISTINCT UO.FD_ORG_ID FROM FR_AA_USER_ORG UO INNER JOIN FR_AA_USER_ROLE URO ON UO.FD_USER_ID=URO.FD_USER_ID\r\n" + 
				//		"INNER JOIN FR_AA_ROLE R ON URO.FD_ROLE_ID=R.FD_ID WHERE R.FD_CODE='"+roleMap.get("FD_CODE").toString()+"') ");
			}else {
				//总公司子流程
                sb.append("INNER JOIN FR_AA_USER_ORG UO ON SI.ORG_ID=UO.FD_ORG_ID WHERE UO.FD_USER_ID='" + trueUserId
                        + "' ");
			}
		}else {
			//分公司
			sb.append("WHERE SI.ORG_ID IS NULL AND UR.COLL_ORG_ID='"+currentUserOrg.getId()+"' ");
		}
		if(StringUtils.isNotBlank(sourceProjId)) {
			sb.append("AND SI.SOURCE_PROJ_ID='"+sourceProjId+"' ");
		}
		if(StringUtils.isNotBlank(sourceOrgId)) {
			sb.append("AND ORG.FD_CODE LIKE '%"+sourceOrgId+"%' ");
		}
		if(uploadResult != null) {
			if(StringUtils.isNotBlank(uploadResult.getSplitCode())) {
				sb.append("AND UR.SPLIT_CODE LIKE '%"+uploadResult.getSplitCode()+"%'");
			}
			if(StringUtils.isNotBlank(uploadResult.getTaskId())) {
				sb.append("AND UR.TASK_ID='"+uploadResult.getTaskId()+"' ");
			}
            if (StringUtils.isNotBlank(uploadResult.getCreator())) {
            		if ("null".equals(uploadResult.getCreator()) || isSignNode) {
                        //会签节点，要根据流程ID去查处理人，不能拿流程中的。因为会签节点会覆盖变量
            			if(!isExamNode) {
            				String assignee = flowableTaskService.findTaskAssignee(processTaskId);
                            sb.append("AND UR.FD_CREATOR='" + assignee + "' ");
            			}
                    } else {
                        sb.append("AND UR.FD_CREATOR='" + uploadResult.getCreator() + "' ");
                    }
                
			}
			if(StringUtils.isNotBlank(uploadResult.getResultTypeName())) {
				sb.append("AND UR.RESULT_TYPE_NAME LIKE '%"+resultTypeMap.get(uploadResult.getResultTypeName())+"%' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getSplitName())) {
				sb.append("AND UR.SPLIT_NAME LIKE '%"+uploadResult.getSplitName()+"%' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getCollWay())) {
				sb.append("AND UR.COLL_WAY='"+uploadResult.getCollWay()+"' ");
			}
		}
		sb.append("ORDER BY ORG.FD_CODE,SUBSTR(INDE.INDEX_CODE,1,4),INDE.INDEX_LINE");
		System.out.println("~~~~~~~~~~~~~~~~~~~~查看流程："+sb);
		List<IrrUploadResultEntity> list = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrUploadResultEntity>(IrrUploadResultEntity.class));
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		if(size == null) {
			size = Integer.valueOf(IrrEnum.IRR_DEFAULT_SIZE.getValue());
		}
		if(page == null||page==0) {
			page = Integer.valueOf(IrrEnum.IRR_DEFAULT_PAGE.getValue());
		}
		ResponsePage<IrrUploadResultEntity> reponsePage = new ResponsePage<IrrUploadResultEntity>();
		reponsePage.setAllData(list);
		reponsePage.setNumber(Long.valueOf(page));
		reponsePage.setSize(size);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", reponsePage.getPageData());
		return map;
	}

	@Override
    public Map<String, Object> checkIsData(String id, String processTaskId) throws Exception {
		if(StringUtils.isBlank(id)) {
			IrrUtil.getMap(false, "无法提交:评估计划ID不存在！");
		}
        String loginName = flowableTaskService.findTaskAssignee(processTaskId);
        QIrrUploadResultEntity qUploadResult = QIrrUploadResultEntity.irrUploadResultEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUploadResult.taskId.eq(id));
        builder.and(qUploadResult.creator.eq(loginName));
        List<IrrUploadResultEntity> list = IterableUtil.toList(repository.findAll(builder));
		if(CollectionUtils.isEmpty(list)) {
			return IrrUtil.getMap(false, "无法提交:还没有上传模板数据！");
		}else {
			return IrrUtil.getMap(true);
		}
	}

	@Override
	public Map<String, String> findIndexDesc(String splitId) throws Exception {
		String sql = "SELECT UR.SPLIT_ID,II.INDEX_DESC FROM T_IRR_UPLOAD_RESULT UR \r\n" + 
				"INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID \r\n" + 
				"INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n";
		if(StringUtils.isNotBlank(splitId)) {
			sql += "WHERE UR.SPLIT_ID='"+splitId+"'";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		Map<String, String> returnMap = new HashMap<String, String>();
		if(CollectionUtils.isEmpty(list)) {
			return returnMap;
		}
		Object splitIdObj = null;
		Object indexDescObj = null;
		for(Map<String, Object> map : list) {
			splitIdObj = map.get("SPLIT_ID");
			indexDescObj = map.get("INDEX_DESC");
			if(!IrrUtil.strObjectIsEmpty(splitIdObj) && !IrrUtil.strObjectIsEmpty(indexDescObj)) {
				returnMap.put(splitIdObj.toString(), indexDescObj.toString());
			}
		}
		return returnMap;
	}

	@Override
	public Org findOrgByCondition(String userId) throws Exception {
		String sql = "SELECT ORG.FD_ID ID,ORG.FD_CODE CODE,ORG.FD_NAME NAME FROM FR_AA_USER U INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID \r\n" + 
				"INNER JOIN FR_AA_ORG ORG ON UO.FD_ORG_ID=ORG.FD_ID WHERE U.FD_ID='"+userId+"'";
		List<Org> orgList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Org>(Org.class));
		if(!CollectionUtils.isEmpty(orgList)) {
			return orgList.get(0);
		}
		return null;
	}

	@Override
    public void packDownloadSheet(Workbook wb, Sheet sheet, List<IrrSplitIndexEntity> splitList,
            Map<String, Map<String, Object>> splitSourceIndexInfo, String trueUserId) throws Exception {
		/*表格边框样式*/
		CellStyle dataCellBorderStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
		/*指标结果类型Map*/
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		/*生成title*/
		Row titleRow = sheet.createRow(0);
		String[] titles = IrrTemplateEnum.IRR_TEMPLATE_FILE_TITLE.getValue().split("-");
		int titleCellIndex = 0;
		for(String title : titles) {
			Cell titleCell = titleRow.createCell(titleCellIndex);
			titleCell.setCellStyle(POIUtil.createBorderFontCellStyle(wb, BorderStyle.THIN,IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(),
					Font.COLOR_NORMAL, true));
			titleCell.setCellValue(title);
			titleCellIndex ++;
		}
		/*生成数据*/
		int dataRowIndex = 0;
		for(IrrSplitIndexEntity split : splitList) {
			dataRowIndex ++;
			int dataCellIndex = 0;
			Row dataRow = sheet.createRow(dataRowIndex);
			Map<String, Object> sourceIndexInfo = splitSourceIndexInfo.get(split.getSplitCode());
			/*评估项目名称*/
			Cell projTypeNameCell = dataRow.createCell(dataCellIndex);
			projTypeNameCell.setCellStyle(dataCellBorderStyle);
			projTypeNameCell.setCellValue(sourceIndexInfo.get("PROJ_TYPE_NAME").toString());
			dataCellIndex ++;
			/*行次*/
			Cell indexLineCell = dataRow.createCell(dataCellIndex);
			indexLineCell.setCellStyle(dataCellBorderStyle);
			indexLineCell.setCellValue(sourceIndexInfo.get("INDEX_LINE").toString());
			dataCellIndex ++;
			/*指标编码*/
            String splitCode = split.getSplitCode();
			Cell codeCell = dataRow.createCell(dataCellIndex);
			codeCell.setCellStyle(dataCellBorderStyle);
            codeCell.setCellValue(splitCode);
			dataCellIndex ++;
			/*指标名称*/
			Cell nameCell = dataRow.createCell(dataCellIndex);
			nameCell.setCellStyle(dataCellBorderStyle);
			nameCell.setCellValue(split.getSplitName());
			dataCellIndex ++;
			/*指标结果类型*/
			Cell typeCell = dataRow.createCell(dataCellIndex);
			typeCell.setCellStyle(dataCellBorderStyle);
			String type = split.getResultType();
			typeCell.setCellValue(resultTypeMap.get(type));
			dataCellIndex ++;
			/*指标值*/
			Cell resultCell = dataRow.createCell(dataCellIndex);
			if(IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue().equals(type)) {//结果类型为选项
				resultCell.setCellStyle(dataCellBorderStyle);
				resultCell.setCellValue("请选择");
				String sql = "SELECT OPTION_NAME FROM T_IRR_INDEX_OPTION WHERE INDEX_ID='"+split.getSourceIndexId()+"' ORDER BY OPTION_SORT";
				List<String> options = jdbcTemplate.queryForList(sql, String.class);
				if(CollectionUtils.isNotEmpty(options)) {
				    String[] optionArray = options.toArray(new String[options.size()]);
                    //如果是分支机构隶属保监局拆分指标，则写死
                    if (IrrEnum.SPLIT_BRANCH_MEMB_BUREAU_CODE.getValue().equals(splitCode)) {
                        //获取上传人机构
                        Org userOrg = irrTaskService.findOrgByUserId(trueUserId);
                        if (userOrg == null || StringUtils.isBlank(userOrg.getName()))
                            continue;
				        for(String temp : optionArray) {
                            if (StringUtils.isNotBlank(temp) && userOrg.getName()
                                    .contains(temp.substring(temp.indexOf("|") + 1, temp.indexOf("|") + 3))) {
                                optionArray = new String[] { temp };
                                break;
                            }
				        }
				    }
                    sheet.addValidationData(POIUtil.createExcelSelect(sheet.getDataValidationHelper(), optionArray,
							dataRowIndex, dataRowIndex, dataCellIndex, dataCellIndex));
				}
			}else if(IrrEnum.INDEX_RESULT_TYPE_DATE.getValue().equals(type)) {//结果类型为日期
				resultCell.setCellStyle(dataCellBorderStyle);
				resultCell.setCellType(CellType.NUMERIC);
			}else if(IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue().equals(type)) {//结果类型为数字
				resultCell.setCellType(CellType.STRING);
				resultCell.setCellStyle(POIUtil.createDataFormatBorderCellStyle(wb, IrrTemplateEnum.TEMPLATE_TEXT_FORMAT_DATE_STR.getValue(),BorderStyle.THIN));
			}else {//其他类型为文本,@
				resultCell.setCellType(CellType.STRING);
				resultCell.setCellStyle(POIUtil.createDataFormatBorderCellStyle(wb, IrrTemplateEnum.TEMPLATE_TEXT_FORMAT_DATE_STR.getValue(),BorderStyle.THIN));
			}
		}
	}

	@Override
	public List<IrrUploadResultEntity> packUploadResultData(IrrTaskEntity task,
            Map<String, IrrSplitIndexEntity> splitMap, Sheet sheet, Org org, String assignee) throws Exception {
		List<IrrUploadResultEntity> uploadList = new ArrayList<IrrUploadResultEntity>();
		Integer rows = sheet.getLastRowNum();
		if(rows == null || rows <= 0) {
			LOG.error(sheet.getSheetName()+"文件没有读取的内容!");
		}
		Object cellValue = null;
		for(int i=1;i<=rows;i++) {
			Row row = sheet.getRow(i);
			int cols = row.getLastCellNum();
			if(cols <= 0) {
				LOG.error("第"+i+1+"行没有数据！");
				continue;
			}
			//从指标编码开始读
			int colIndex = 2;
			IrrUploadResultEntity upload = new IrrUploadResultEntity();
			cellValue = POIUtil.getCellValue(row.getCell(colIndex++));
			String splitCodeTemp = cellValue==null?null:cellValue.toString();
			if(StringUtils.isNotBlank(splitCodeTemp)) {//补全数据
				IrrSplitIndexEntity split = splitMap.get(splitCodeTemp);
				upload.setSplitId(split.getId());
				upload.setCollWay(split.getCollWay());
			}
			upload.setSplitCode(splitCodeTemp);
			cellValue = POIUtil.getCellValue(row.getCell(colIndex++));
			upload.setSplitName(cellValue==null?null:cellValue.toString());
			cellValue = POIUtil.getCellValue(row.getCell(colIndex++));
			upload.setResultTypeName(cellValue==null?null:cellValue.toString());
			cellValue = POIUtil.getCellValue(row.getCell(colIndex));
			if(StringUtils.isNotBlank(upload.getResultTypeName()) && upload.getResultTypeName().equals("无")) {
				if(cellValue.toString().startsWith("0")) {
					upload.setSplitResultQ2(cellValue.toString());
				}else {
				try {
					BigDecimal bigDecimal=new BigDecimal(cellValue.toString());
					upload.setSplitResultQ2(bigDecimal.toString());
				} catch (NumberFormatException e) {
					upload.setSplitResultQ2(cellValue.toString());
				}}				
			}else if(StringUtils.isNotBlank(upload.getResultTypeName()) && upload.getResultTypeName().equals("数值")){
                if (IrrUtil.strObjectIsNotEmpty(cellValue)) {
                    BigDecimal bigDecimal = new BigDecimal(cellValue.toString().trim());
                    upload.setSplitResultQ2(bigDecimal.toPlainString());
                }
			}else {
				upload.setSplitResultQ2(cellValue.toString());
			}			
			upload.setCollOrgId(org.getId());
			upload.setCollOrgName(org.getName());
			upload.setIsHandChange(IrrEnum.YESNO_N.getValue());
			upload.setTaskId(task.getId());
			upload.setTaskBatch(task.getTaskBatch());
			upload.setCollUserName(SecurityUtil.getUserName());
			upload.setIsFillReason(IrrEnum.YESNO_N.getValue());
			upload.setIsCommit(IrrEnum.YESNO_N.getValue());
            upload.setCreator(assignee);
			uploadList.add(upload);
		}
		return uploadList;
	}

	@Override
	public List<IndexValueDTO> getIndexResult(String taskId, String orgId, List<IrrIndexInfoEntity> indexInfoList)
			throws Exception {
		if(CollectionUtils.isEmpty(indexInfoList)) {
			throw new IrrProcessException("汇总失败:不存在直接获取的采集数据!");
		}
		List<IrrUploadResultEntity> resultList = null;
		if(StringUtils.isNotBlank(orgId)) {//分公司
			IrrUploadResultEntity uploadResultExample = new IrrUploadResultEntity();
			uploadResultExample.setCollOrgId(orgId);
			if(StringUtils.isNotBlank(taskId)) {
				uploadResultExample.setTaskId(taskId);
			}
			resultList = fetch(uploadResultExample, IrrQueryParameter.getIrrDefaultQP());
		}else {//总公司
			String sql = "SELECT UR.* FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID \r\n" + 
					"INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID WHERE II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.INDEX_LEVEL='"+IrrEnum.INDEX_LEVEL_HEAD.getValue()+"'\r\n" + 
					"AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.INDEX_EVAL_FORM IS NULL AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' AND SI.SPLIT_LEVEL<>'"+IrrEnum.SPLIT_LEVEL_CHANNEL.getValue()+"' AND UR.TASK_ID='"+taskId+"'";
			resultList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrUploadResultEntity>(IrrUploadResultEntity.class));
		}
		if(CollectionUtils.isEmpty(resultList)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for(IrrUploadResultEntity result : resultList) {
			if(result != null && StringUtils.isNotBlank(result.getSplitCode())) {
				map.put(result.getSplitCode().substring(0, 7), result.getSplitResultQ2());
			}
		}
		List<IndexValueDTO> indexValueList = new ArrayList<IndexValueDTO>();
		for(IrrIndexInfoEntity indexInfo : indexInfoList) {
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(indexInfo.getId());
			value.setCode(indexInfo.getIndexCode());
			value.setName(indexInfo.getIndexName());
			value.setLine(indexInfo.getIndexLine());
			value.setValue(map.get(value.getCode()));
			indexValueList.add(value);
		}
		return indexValueList;
	}

	@Override
    public Map<String, Object> searchIndexByPresentLogin(String id, String splitCode, String splitName,
            String collOrgName, String collUserName, String isCommit, String sourceProjId, Integer size, Integer page)
            throws Exception {
        if (StringUtils.isBlank(id)) {
            return IrrUtil.getMap(false);
        }
		String sql="SELECT UR.ID,UR.SPLIT_ID AS splitId,UR.SPLIT_NAME AS splitName ,UR.SPLIT_CODE AS splitCode,UR.TASK_BATCH AS taskBatch,"
				 + "UR.TASK_ID AS taskId,UR.DATA_BODY AS dataBody,UR.SPLIT_RESULT_Q1 AS splitResultQ1,UR.SPLIT_SCORE_Q1 AS splitScoreQ1,"
				 + "UR.SPLIT_RESULT_Q2 AS splitResultQ2, UR.SPLIT_SCORE_Q2 AS splitScoreQ2,UR.COLL_WAY AS collWay,UR.COLL_ORG_ID AS collOrgId,"
				 + "UR.COLL_ORG_NAME AS collOrgName,UR.IS_HAND_CHANGE AS isHandChange,UR.RESULT_TYPE_NAME AS resultTypeName,UR.IS_FILL_REASON "
				 + "AS isFillReason,UR.COLL_USER_NAME AS collUserName,UR.REMARK AS REMARK FROM T_IRR_UPLOAD_RESULT UR INNER JOIN FR_AA_ORG ORG "
                + "ON UR.COLL_ORG_ID=ORG.FD_ID INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON "
				 + "SI.SOURCE_INDEX_ID=II.ID WHERE 1=1 ";
        if (StringUtils.isNotBlank(id)) {
            sql += "AND UR.TASK_ID='" + id + "' ";
		}
		if(StringUtils.isNotBlank(splitCode)) {
			sql+="AND UR.SPLIT_CODE LIKE '%"+splitCode+"%' ";
		}
		if(StringUtils.isNotBlank(splitName)) {
			sql+="AND UR.SPLIT_NAME LIKE '%"+splitName+"%' ";
		}
		if(StringUtils.isNotBlank(collOrgName)) {
            sql += "AND ORG.FD_CODE='" + collOrgName + "' ";
		}
		if(StringUtils.isNotBlank(isCommit)) {
            sql += "AND UR.IS_COMMIT='" + isCommit + "' ";
		}
        if (StringUtils.isNotBlank(sourceProjId)) {
            sql += "AND II.PROJ_TYPE_ID='" + sourceProjId + "' ";
        }
        if (StringUtils.isNotBlank(collUserName)) {
        	String sqlRole="SELECT R.* FROM FR_AA_USER U INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID INNER JOIN FR_AA_ROLE R ON "
        			     + "UR.FD_ROLE_ID=R.FD_ID WHERE U.FD_LOGINNAME='"+collUserName+"' AND R.FD_CODE LIKE '"+IrrProcessEnum.IRR_SIGN_PROCESS.getValue()+"%'\r\n";//判断是否是会签子流程
        	List<Map<String, Object>> listRole=jdbcTemplate.queryForList(sqlRole);
        	if(listRole.size()<1) {
        		sql += " AND UR.FD_CREATOR='" + collUserName + "'";
        	}
        }
		sql+="ORDER BY ORG.FD_CODE,SUBSTR(UR.SPLIT_CODE,1,7),II.INDEX_LINE";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		if(size == null) {
			size = Integer.valueOf(IrrEnum.IRR_DEFAULT_SIZE.getValue());
		}
		if(page == null||page==0) {
			page = Integer.valueOf(IrrEnum.IRR_DEFAULT_PAGE.getValue());
		}
		ResponsePage<Map<String, Object>> response=new ResponsePage<Map<String, Object>>();
		response.setSize(20);	
		response.setNumber(Long.valueOf(page));
		response.setSize(size);
		response.setAllData(list);
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("response", response.getPageData());
		return map;
	}

	@Override
	public Map<String,String> findAllOrgs() {
        Map<String, String> map = new LinkedHashMap<String, String>();
		String sql="SELECT * FROM FR_AA_ORG ";
		List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
        Collections.sort(list, new Comparator<Map<String, Object>>() {

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("FD_NAME").toString().contains(IrrEnum.ORG_ORG_NAME.getValue())
                        && o2.get("FD_NAME").toString().contains(IrrEnum.ORG_DEPT_NAME.getValue())) {
                    return -1;//正序
                } else if (o1.get("FD_NAME").toString().contains(IrrEnum.ORG_DEPT_NAME.getValue())
                        && o2.get("FD_NAME").toString().contains(IrrEnum.ORG_ORG_NAME.getValue())) {
                    return 1;//倒叙
                } else if (o1.get("FD_NAME").toString().contains(IrrEnum.ORG_ORG_NAME.getValue())
                        && o2.get("FD_NAME").toString().contains(IrrEnum.ORG_ORG_NAME.getValue())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
		for (Map<String, Object> mapOrg : list) {
			map.put(mapOrg.get("FD_CODE").toString(), mapOrg.get("FD_NAME").toString());
		}
		return map;
    }

    @Override
    public Map<String, Object> searchCollIndex(String taskId, String splitCode, String splitName, String collectionWay,
            String sourceProjId, String sourceOrgId, String collName, Integer size, Integer page) throws Exception {
        List<Map<String, Object>> list = searchCollIndex(taskId, splitCode, splitName, collectionWay, sourceProjId,
                sourceOrgId, collName);
        if (size == null) {
            size = Integer.valueOf(IrrEnum.IRR_DEFAULT_SIZE.getValue());
        }
        if (page == null || page == 0) {
            page = Integer.valueOf(IrrEnum.IRR_DEFAULT_PAGE.getValue());
        }
        ResponsePage<Map<String, Object>> response = new ResponsePage<Map<String, Object>>();
        response.setNumber(Long.valueOf(page));
        response.setSize(size);
        response.setAllData(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("response", response.getPageData());
        return map;
    }

    @Override
    public List<Map<String, Object>> findUserRole(String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        String sql = "SELECT DISTINCT R.FD_CODE,R.FD_NAME FROM FR_AA_USER U INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID \r\n"
                + "INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID WHERE U.FD_ID='" + userId + "'";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> searchCollIndex(String taskId, String splitCode, String splitName,
            String collectionWay, String sourceProjId, String sourceOrgId, String collName) throws Exception {
        String sql = "SELECT UR.ID,UR.SPLIT_ID AS splitId,UR.SPLIT_NAME AS splitName ,UR.SPLIT_CODE AS splitCode,UR.TASK_BATCH AS taskBatch,"
                //+ "UR.TASK_ID AS taskId,UR.DATA_BODY AS dataBody,UR.SPLIT_RESULT_Q1 AS splitResultQ1,UR.SPLIT_SCORE_Q1 AS splitScoreQ1,"//修改之前
                + "UR.TASK_ID AS taskId,UR.DATA_BODY AS dataBody,UR.SPLIT_RESULT_Q1 AS splitResultQ1,UR.SPLIT_SCORE_Q1 AS splitScoreQ1,UR.DATA_DESC AS dataDesc,"//修改之后
                + "UR.SPLIT_RESULT_Q2 AS splitResultQ2, UR.SPLIT_SCORE_Q2 AS splitScoreQ2,UR.COLL_WAY AS collWay,UR.COLL_ORG_ID AS collOrgId,"
                + "UR.COLL_ORG_NAME AS collOrgName,UR.IS_HAND_CHANGE AS isHandChange,UR.RESULT_TYPE_NAME AS resultTypeName,UR.IS_FILL_REASON "
                + "AS isFillReason,UR.COLL_USER_NAME AS collUserName,UR.REMARK AS REMARK FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID \r\n"
                + "INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID INNER JOIN FR_AA_ORG ORG ON UR.COLL_ORG_ID=ORG.FD_ID WHERE 1=1 ";
        if (StringUtils.isNotBlank(splitCode)) {
            sql += "AND UR.SPLIT_CODE LIKE '%" + splitCode + "%' ";
        }
        if (StringUtils.isNotBlank(splitName)) {
            sql += "AND UR.SPLIT_NAME LIKE '%" + splitName + "%' ";
        }
        if (StringUtils.isNotBlank(collectionWay)) {
            sql += "AND UR.COLL_WAY = '" + collectionWay + "' ";
        }
        if (StringUtils.isNotBlank(taskId)) {
            sql += "AND UR.TASK_ID = '" + taskId + "' ";
        }
        if (StringUtils.isNotBlank(sourceProjId)) {
            sql += "AND II.PROJ_TYPE_ID='" + sourceProjId + "' ";
        }
        if (StringUtils.isNotBlank(sourceOrgId)) {
            sql += "AND ORG.FD_CODE='" + sourceOrgId + "' ";
        }
        if (StringUtils.isNotBlank(collName)) {
            sql += "AND UR.COLL_USER_NAME LIKE '%" + collName + "%' ";
        }
        List<Map<String, Object>> roleList = findUserRole(SecurityUtil.getUserId());
        //业务管理员可以查看全部
        boolean lookAll = false;
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (Map<String, Object> role : roleList) {
                if (role != null && IrrEnum.ROLE_IRR_ADMIN.getValue().equals(role.get("FD_CODE"))) {
                    lookAll = true;
                    break;
                }
            }
        }
        if (!lookAll) {
            sql += " AND UR.FD_CREATOR='" + SecurityUtil.getLoginName() + "'";
        }
        sql += " ORDER BY SUBSTR(UR.SPLIT_CODE,1,7)";
        System.out.println("zhibiaosql~~~~~~~~~~~~~~~~~~~~~~~~~:"+sql);
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Workbook exportCollResult(String param) throws Exception {
        JSONObject json = JSONObject.fromObject(param);
        List<Map<String, Object>> list = searchCollIndex(json.getString("taskId"), json.getString("splitCode"),
                json.getString("splitName"), json.getString("collectionWay"), json.getString("sourceProjId"),
                json.getString("sourceOrgId"), json.getString("collName"));
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(IrrTemplateEnum.TEMPLATE_COLL_RESULT_SHEET_NAME.getValue());
        if (CollectionUtils.isEmpty(list)) {
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(IrrTemplateEnum.TEMPLATE_COLL_RESULT_EMPTY_INFO.getValue());
            return wb;
        }
        CellStyle titleCellStyle = POIUtil.createBorderFontCellStyle(wb, BorderStyle.THIN,
                IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(), Font.COLOR_NORMAL, true);
        CellStyle cellStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
        //设置表头
        Row titleRow = sheet.createRow(0);
        String[] titleArray = IrrTemplateEnum.TEMPLATE_COLL_RESULT_SHEET_TITLE.getValue().split(IrrEnum.SEPARATOR.getValue());
        for (int i = 0; i < titleArray.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellStyle(titleCellStyle);
            cell.setCellValue(titleArray[i]);
        }
        Map<String, String> collWayMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.COLL_WAY_CODE.getValue(),
                Locale.CHINA);
        //设置数据
        Object obj = null;
        Cell cell = null;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (MapUtils.isEmpty(map)) {
                continue;
            }
            int column = 0;
            Row row = sheet.createRow(i + 1);
            //任务期次
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("TASKBATCH")) == null ? null : obj.toString());
            //拆分指标编码
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITCODE")) == null ? null : obj.toString());
            //拆分指标名称
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITNAME")) == null ? null : obj.toString());
            //本期指标结果
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITRESULTQ2")) == null ? null : obj.toString());
            //本期指标得分
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITSCOREQ2")) == null ? null : obj.toString());
            //上期指标结果
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITRESULTQ1")) == null ? null : obj.toString());
            //上期指标得分
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("SPLITSCOREQ1")) == null ? null : obj.toString());
            //采集方式
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("COLLWAY")) == null ? null : collWayMap.get(obj.toString()));
            //采集机构名称
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("COLLORGNAME")) == null ? null : obj.toString());
            //采集人名称
            cell = row.createCell(column++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((obj = map.get("COLLUSERNAME")) == null ? null : obj.toString());
        }
        return wb;
    }

    @Override
    public List<IrrUploadResultEntity> formatCollNumberIndexResult(List<IrrUploadResultEntity> formatList)
            throws Exception {
        if (CollectionUtils.isEmpty(formatList)) {
            throw new IrrProcessException("无法格式化上传数据：采集结果为空！");
        }
        //查询对应的指标小数位数
        String placeSql = "SELECT SI.ID,II.DECIMAL_PLACE FROM T_IRR_SPLIT_INDEX SI\r\n"
                + "INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n"
                + "WHERE SI.IS_USE='" + IrrEnum.YESNO_Y.getValue()
                + "' AND II.INDEX_STATUS='" + IrrEnum.INDEX_STATUS_ENABLE.getValue() + "' AND II.IS_XBRL='"
                + IrrEnum.YESNO_Y.getValue() + "'\r\n"
                + "AND II.IS_APPLICABIE='" + IrrEnum.YESNO_Y.getValue() + "' AND II.INDEX_RESULT_TYPE='"
                + IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue() + "'";
        List<Map<String, Object>> placeList = jdbcTemplate.queryForList(placeSql);
        if (CollectionUtils.isEmpty(placeList)) {
            throw new IrrProcessException("无法格式化上传数据：指标的小数位数不存在！");
        }
        Map<String, Integer> placeMap = new HashMap<String, Integer>();
        for (Map<String, Object> temp : placeList) {
            Object splitId = temp.get("ID");
            Object decimalPlace = temp.get("DECIMAL_PLACE");
            if (IrrUtil.strObjectIsEmpty(splitId) || IrrUtil.strObjectIsEmpty(decimalPlace))
                continue;
            placeMap.put(splitId.toString(), Integer.valueOf(decimalPlace.toString()));
        }
        //格式化数据
        for (IrrUploadResultEntity temp : formatList) {
            Integer scale = placeMap.get(temp.getSplitId());
            if (scale == null)
                continue;
            String value = IrrUtil.formatNumber(scale, temp.getSplitResultQ2(), true);
            if (StringUtils.isBlank(value))
                continue;
            temp.setSplitResultQ2(value);
        }
        return formatList;
    }
}
