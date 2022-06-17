package net.gbicc.app.irr.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemDictionaryServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.flowable.service.FlowableTaskService;

import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrUploadResultRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.IrrSysIndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrPromptInfoEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.gbicc.app.irr.service.IrrUploadResultService;

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
	
	@Override
	public Workbook createIrrCollTemplate() throws Exception {
		Workbook hfwb = new HSSFWorkbook();
		Sheet sheet = hfwb.createSheet(IrrTemplateEnum.IRR_TEMPLATE_SHEET_NAME.getValue());
		List<IrrSplitIndexEntity> list = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(SecurityUtil.getUserId(), IrrEnum.COLL_WAY_HAND.getValue());
		if(list == null || list.size() <= 0) {
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue(IrrEnum.COLL_EMPTY_INDEX_INFO.getValue().toString());
			return hfwb;
		}
		/*生成title*/
		Row titleRow = sheet.createRow(0);
		String[] titles = IrrTemplateEnum.IRR_TEMPLATE_FILE_TITLE.getValue().split("-");
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
		/*指标结果类型Map*/
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		for(IrrSplitIndexEntity split : list) {
			dataRowIndex ++;
			int dataCellIndex = 0;
			Row dataRow = sheet.createRow(dataRowIndex);
			/*指标编码*/
			Cell codeCell = dataRow.createCell(dataCellIndex);
			codeCell.setCellStyle(dataCellBorderStyle);
			codeCell.setCellValue(split.getSplitCode());
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
			resultCell.setCellStyle(dataCellBorderStyle);
			if(IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue().equals(type)) {//查询指标的选项
				resultCell.setCellValue("请选择");
				String sql = "SELECT OPTION_NAME FROM T_IRR_INDEX_OPTION WHERE INDEX_ID='"+split.getSourceIndexId()+"'";
				List<String> options = jdbcTemplate.queryForList(sql, String.class);
				if(CollectionUtils.isNotEmpty(options)) {
					sheet.addValidationData(POIUtil.createExcelSelect(sheet.getDataValidationHelper(), options.toArray(new String[options.size()]), 
							dataRowIndex, dataRowIndex, dataCellIndex, dataCellIndex));
				}
			}
		}
		return hfwb;
	}

	@Override
	@Transactional
	public void readCollUploadFile(Workbook wb,String taskId) throws Exception {
		Sheet sheet = wb.getSheetAt(0);
		Integer rows = sheet.getLastRowNum();
		if(rows == null || rows <= 0) {
			LOG.error("文件没有读取的内容!");
		}
		List<Org> orgs = userService.findById(SecurityUtil.getUserId()).getOrgs();
		Org org = new Org();
		if(CollectionUtils.isNotEmpty(orgs)) {
			org = orgs.get(0);
		}
		List<IrrSplitIndexEntity> splitIndexList = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(SecurityUtil.getUserId(), IrrEnum.COLL_WAY_HAND.getValue());
		Map<String,IrrSplitIndexEntity> splitMap = irrSplitIndexService.getSplitIndexMap(splitIndexList);
		List<IrrUploadResultEntity> uploadList = new ArrayList<IrrUploadResultEntity>();
		Object cellValue = null;
		IrrTaskEntity task = irrTaskService.findById(taskId);
		for(int i=1;i<=rows;i++) {
			Row row = sheet.getRow(i);
			int cols = row.getLastCellNum();
			if(cols <= 0) {
				LOG.error("第"+i+1+"行没有数据！");
				continue;
			}
			int colIndex = 0;
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
			upload.setSplitResultQ2(cellValue==null?null:cellValue.toString());
			upload.setCollOrgId(org.getId());
			upload.setCollOrgName(org.getName());
			upload.setIsHandChange(IrrEnum.YESNO_N.getValue());
			upload.setTaskId(taskId);
			upload.setTaskBatch(task.getTaskBatch());
			upload.setCollUserName(SecurityUtil.getUserName());
			upload.setIsFillReason(IrrEnum.YESNO_N.getValue());
			uploadList.add(upload);
		}
		/*数据验证*/
		uploadList = indexDataValidation(uploadList,task.getTaskBatch());
		/*采集系统数据*/
		List<IrrUploadResultEntity> sysResultList = uploadSysResult(taskId,SecurityUtil.getUserId());
		uploadList.addAll(sysResultList);
		/*删除当前期次当前人的采集数据*/
		getRepository().deleteByTaskIdAndCreator(taskId, SecurityUtil.getLoginName());
		add(uploadList);
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
		Map<String, String> dataValidMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_DATA_VALID_PARAM_CODE.getValue(), Locale.CHINA);
		String maxLimitParam = dataValidMap.get(IrrEnum.INDEX_DATA_VALID_PARAM_MAX.getValue());
		if(StringUtils.isBlank(maxLimitParam)) {
			maxLimitParam = IrrEnum.DEFAULT_MAX_LIMIT_PARAM.getValue();
		}
		maxLimitParam = maxLimitParam.replace(IrrEnum.MAX_LIMIT_PARAM_UNIT_CODE.getValue(), "");
		List<String> orgs = irrTaskService.findOrgIdByCondition(SecurityUtil.getLoginName());
		if(CollectionUtils.isEmpty(orgs) || StringUtils.isBlank(orgs.get(0))) {
			return list;
		}
		Map<String, IrrUploadResultEntity> previousUpload = getUploadMap(findUploadResult(IrrUtil.getPreviousPeriod(taskBatch),orgs.get(0)));
		if(CollectionUtils.isEmpty(list)) {
			return list;
		}
		for(int i=0;i<list.size();i++) {
			IrrUploadResultEntity upload = list.get(i);
			IrrUploadResultEntity previous = previousUpload.get(upload.getSplitCode());
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
		List<IrrSplitIndexEntity> sysSplitList = irrSplitIndexService.findSplitIndexByUserIdAndCollWay(collUserId, IrrEnum.COLL_WAY_SYS.getValue());
		if(CollectionUtils.isEmpty(sysSplitList)) {
			return list;
		}
		User collUser = userService.findById(collUserId);
		Org org = new Org();
		if(CollectionUtils.isNotEmpty(collUser.getOrgs())) {
			org = collUser.getOrgs().get(0);
		}
		IrrTaskEntity task = irrTaskService.findById(taskId);
		/*指标结果类型Map*/
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		for(IrrSplitIndexEntity split : sysSplitList) {
			IrrUploadResultEntity sysUpload = new IrrUploadResultEntity();
			sysUpload.setSplitId(split.getId());
			sysUpload.setSplitName(split.getSplitName());
			sysUpload.setSplitCode(split.getSplitCode());
			sysUpload.setTaskBatch(task.getTaskBatch());
			sysUpload.setTaskId(taskId);
			sysUpload.setCollWay(split.getCollWay());
			sysUpload.setCollOrgId(org.getId());
			sysUpload.setCollOrgName(org.getName());
			sysUpload.setIsHandChange(IrrEnum.YESNO_N.getValue());
			sysUpload.setResultTypeName(resultTypeMap.get(split.getResultType()));
			sysUpload.setCollUserName(collUser.getUserName());
			sysUpload.setIsFillReason(IrrEnum.YESNO_N.getValue());
			list.add(sysUpload);
		}
		/*加载系统数据*/
		list = loadSysData(list);
		/*验证系统数据*/
		list = indexDataValidation(list,task.getTaskBatch());
		return list;
	}
	
	@Override
	public List<IrrUploadResultEntity> findUploadResult(String taskBatch, String collOrgId) throws Exception {
		IrrUploadResultEntity exampleEntity = new IrrUploadResultEntity();
		exampleEntity.setTaskBatch(taskBatch);
		exampleEntity.setCollOrgId(collOrgId);
		return fetch(exampleEntity, IrrQueryParameter.getIrrDefaultQP());
	}

	@Override
	public Map<String, IrrUploadResultEntity> getUploadMap(List<IrrUploadResultEntity> list) {
		Map<String, IrrUploadResultEntity> map = new HashMap<String,IrrUploadResultEntity>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(IrrUploadResultEntity upload : list) {
				map.put(upload.getSplitCode(), upload);
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
			dbEntity.setSplitResultQ2(splitResultQ2);
			dbEntity.setIsHandChange(IrrEnum.YESNO_Y.getValue());
			User user = userService.findById(SecurityUtil.getUserId());
			Org org = new Org();
			if(user != null && !CollectionUtils.isEmpty(user.getOrgs()) && user.getOrgs().get(0) != null) {
				org = user.getOrgs().get(0);
			}
			IrrUploadResultEntity previous = getRepository().findBySplitCodeAndTaskBatchAndCollOrgId(dbEntity.getSplitCode(), IrrUtil.getPreviousPeriod(dbEntity.getTaskBatch()),org.getId());
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
	public Map<String, Object> loadSysIndexDataByTaskId(String taskId) throws Exception {
		IrrUploadResultEntity upload = new IrrUploadResultEntity();
		upload.setTaskId(taskId);
		upload.setCollWay(IrrEnum.COLL_WAY_SYS.getValue());
		upload.setCreator(SecurityUtil.getLoginName());
		upload.setIsHandChange(IrrEnum.YESNO_N.getValue());
		List<IrrUploadResultEntity> sysUpload = fetch(upload, IrrQueryParameter.getIrrDefaultQP());
		if(CollectionUtils.isEmpty(sysUpload)) {
			return IrrUtil.getMap(false, "无需要加载的系统数据！");
		}
		sysUpload = indexDataValidation(loadSysData(sysUpload),sysUpload.get(0).getTaskBatch());
		for(IrrUploadResultEntity sys : sysUpload) {
			update(sys.getId(),sys);
		}
		return IrrUtil.getMap(true, "加载成功!");
	}

	@Override
	public List<IrrUploadResultEntity> loadSysData(List<IrrUploadResultEntity> list) throws Exception{
		if(CollectionUtils.isEmpty(list) || ObjectUtils.isEmpty(list.get(0))) {
			return list;
		}
		/*查询当前人、机构信息*/
		User user = userService.findById(SecurityUtil.getUserId());
		List<Org> orgs = user.getOrgs();
		Org org = new Org();
		if(CollectionUtils.isNotEmpty(orgs)) {
			org = orgs.get(0);
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
				if(!IrrUtil.strObjectIsEmpty(org.getCode())) {
					branchKey += org.getCode();
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
			if(value != null) {
				upload.setSplitResultQ2(value.getIndexValue().toString());
			}
		}
		return list;
	}

	@Override
	public List<IrrSysIndexValueDTO> findIrrSysIndexValueByEvalDate(String evalDate) {
		String sql = "SELECT * FROM E_ADS_IRR_INDEX_VALUE WHERE EVAL_DATE='"+evalDate+"'";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrSysIndexValueDTO>(IrrSysIndexValueDTO.class));
	}

	@Override
	public Map<String, Object> collSubmit(String taskId) throws Exception{
		Map<String, Object> transientVariables = new HashMap<String, Object>();
		transientVariables.put(IrrProcessEnum.IRR_SUB_PROCESS_COLL_FLAG.getValue().toString(), SecurityUtil.getLoginName());
		flowableTaskService.completeTask(taskId, null, transientVariables, null, null, IrrProcessEnum.IRR_COMMENT_SUBMIT_PREFIX.getValue()+"采集完成");
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
	public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult, String sourceProjId,String examLoginName,Integer size,Integer page) throws Exception{
		Map<String, String> resultTypeMap = frSystemDictionaryService.getDictionaryMap(IrrEnum.INDEX_RESULT_TYPE_CODE.getValue(), Locale.CHINA);
		StringBuffer sb = new StringBuffer("SELECT UR.* FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(sourceProjId)) {
			sb.append("AND SI.SOURCE_PROJ_ID='"+sourceProjId+"' ");
		}
		if(StringUtils.isNotBlank(examLoginName)) {
			sb.append("AND SI.EXAM_USER_LOGINNAME='"+examLoginName+"' ");
		}
		if(uploadResult != null) {
			if(StringUtils.isNotBlank(uploadResult.getSplitCode())) {
				sb.append("AND UR.SPLIT_CODE LIKE '%"+uploadResult.getSplitCode()+"%'");
			}
			if(StringUtils.isNotBlank(uploadResult.getTaskId())) {
				sb.append("AND UR.TASK_ID='"+uploadResult.getTaskId()+"' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getCreator())) {
				sb.append("AND UR.FD_CREATOR='"+uploadResult.getCreator()+"' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getResultTypeName())) {
				sb.append("AND UR.RESULT_TYPE_NAME LIKE '%"+resultTypeMap.get(uploadResult.getResultTypeName())+"%' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getSplitName())) {
				sb.append("AND UR.SPLIT_NAME LIKE '%"+uploadResult.getSplitName()+"%' ");
			}
			if(StringUtils.isNotBlank(uploadResult.getCollWay())) {
				sb.append("AND UR.COLL_WAY='"+uploadResult.getCollWay()+"'");
			}
		}
		List<IrrUploadResultEntity> list = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrUploadResultEntity>(IrrUploadResultEntity.class));
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		if(size == null) {
			size = Integer.valueOf(IrrEnum.IRR_DEFAULT_SIZE.getValue());
		}
		if(page == null) {
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
	public Map<String, Object> checkIsData(String id) throws Exception {
		if(StringUtils.isBlank(id)) {
			IrrUtil.getMap(false, "无法提交:评估计划ID不存在！");
		}
		String loginName = SecurityUtil.getLoginName();
		IrrUploadResultEntity uploadEntity = new IrrUploadResultEntity();
		uploadEntity.setTaskId(id);
		uploadEntity.setCreator(loginName);
		List<IrrUploadResultEntity> list = fetch(uploadEntity, IrrQueryParameter.getIrrDefaultQP());
		if(CollectionUtils.isEmpty(list)) {
			return IrrUtil.getMap(false, "无法提交:还没有上传模板数据！");
		}else {
			return IrrUtil.getMap(true);
		}
	}
	
}
