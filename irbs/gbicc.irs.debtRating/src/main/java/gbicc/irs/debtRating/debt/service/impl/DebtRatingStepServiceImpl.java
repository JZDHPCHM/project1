package gbicc.irs.debtRating.debt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jruby.ir.operands.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.engine.model.core.enums.ParameterType;
import org.wsp.engine.model.core.po.Model;
import org.wsp.engine.model.core.po.Parameter;
import org.wsp.engine.model.core.po.ParameterOption;
import org.wsp.framework.core.util.JacksonObjectMapper;
import org.wsp.framework.jdbc.util.SqlBatcher;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.entity.RatingDebtStep;
import gbicc.irs.debtRating.debt.repository.RatingDebtStepRepository;
import gbicc.irs.debtRating.debt.service.DebtIndexService;
import gbicc.irs.debtRating.debt.service.DebtRatingService;
import gbicc.irs.debtRating.debt.service.DebtRatingStepService;
import gbicc.irs.debtRating.debt.service.NsPrjProjectService;
import gbicc.irs.debtRating.debt.support.Dxfx;
import gbicc.irs.debtRating.debt.support.ProjectRatingInfo;
import gbicc.irs.debtRating.debt.support.Zbfx;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.CommonUtils;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.CustRatingInfo;

@Service("DebtRatingStepServiceImpl")
public class DebtRatingStepServiceImpl extends DaoServiceImpl<RatingDebtStep, String, RatingDebtStepRepository>
		implements DebtRatingStepService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	DebtRatingService debtRating;
	@Autowired
	DebtRatingStepService debtRatingStep;
	@Autowired
	private DebtIndexService ratingIndexService;
	@Autowired
	NsPrjProjectService nsPrj;

	
	@Override
	public DebtRating nextStep(String stepId) throws Exception {
		RatingDebtStep step = repository.getOne(stepId);
		DebtRating rating = (DebtRating) step.getRatingMain();
		RatingDebtStep nextStep = step.getNextStep();
		rating.setCurrentStep(nextStep);
		rating.setCurrentStepId(nextStep.getId());
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		// 查询评级建议等级
		return rating;
	}

	@Override
	public DebtRating lastStep(String stepId) throws Exception {
		RatingDebtStep setp = repository.getOne(stepId);
		DebtRating rating = (DebtRating) setp.getRatingMain();
		RatingDebtStep lastSetp = setp.getLastStep();
		rating.setCurrentStep(lastSetp);
		rating.setCurrentStepId(lastSetp.getId());
		// 查询评级建议等级
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		return rating;
	}

	@Override
	public DebtRating checkQualitative(String stepId, Map<String, String> paramValue) throws Exception {
		// 更新定性指标
		RatingDebtStep step = repository.getOne(stepId);
		DebtRating rating = (DebtRating) step.getRatingMain();
		rating.setCurrentStep(step);
		rating.setCurrentStepId(step.getId());
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		return rating;
	}

	public void updateQualitative(String stepId, Map<String, String> paramValue) {
		if (StringUtils.hasText(stepId) && stepId != null && paramValue.size() > 0) {
			SqlBatcher sqlBatcher = new SqlBatcher(
					"UPDATE NS_DEBT_INDEXES I SET I.FD_INDEXVALUE=? WHERE I.FD_INDEXCODE=? AND I.FD_STEPID=?");
			Iterator<String> iter = paramValue.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = paramValue.get(key);
				if (StringUtils.hasText(value)) {
					sqlBatcher.addArg(new Object[] { value, key, stepId });
				}
			}
			sqlBatcher.execute(jdbcTemplate);
		}
	}

	@Override
	public DebtRating saveQualitativeAndNextStep(String stepId, Map<String, String> paramValue) throws Exception {
		// 更新定性指标
		this.updateQualitative(stepId, paramValue);
		return this.nextStep(stepId);
	}

	@Override
	public List<RatingDebtStep> getAdditionalStepByRatingId(String ratingId) throws Exception {
		List<RatingDebtStep> step = repository.findByRatingMain_id(ratingId);
		List<RatingDebtStep> stepResult = new ArrayList<RatingDebtStep>();
		for (RatingDebtStep ratingMainStep : step) {
			if (ratingMainStep.getVaild().equals("1")) {
				stepResult.add(ratingMainStep);
			}
		}
		return stepResult;
	}

	/**
	 * 债项信息查询
	 */
	@Override
	public ProjectRatingInfo projectInfo(String projectNo) {
		String sql = "select levelz.fd_admission_suggest as suggest,debt.fd_pd as levelBs,debt.fd_project_name as proName,prj.document_type as proType, (case when prj.lease_channel='00' then '标准项目' when prj.lease_channel='01' then '小微项目' when prj.lease_channel='02' then '用信项目(非授信)' end) as leaseChannel,'单笔单批' as prjType,\r\n" + 
				"(select distinct(fd_indexvalue)  from ns_debt_indexes where  fd_indextype=fd_pro_id and fd_indexcode='P039' and fd_level='BS001')  as ZlBJ,\r\n" + 
				"(select distinct(fd_indexvalue)  from ns_debt_indexes where  fd_indextype=fd_pro_id and fd_indexcode='P042' and fd_level='BS001') as ZLQX,  \r\n" + 
				"	          (case when debt.fd_final_bs is null then ROUND(debt.fd_sco, 2) else ROUND(debt.fd_final_bs, 2) end)as finalBs,to_char(debt.fd_final_date,'yyyy-MM-dd') as ratingDate, \r\n" + 
				"        case when (select count(*) from ns_debt_rating where  to_number(fd_sco)>to_number(debt.fd_sco) and fd_vaild='1'   and fd_rating_status='020' and fd_sco is not null)=0 then 0   else \r\n" + 
				"         ((select count(*) from ns_debt_rating where   to_number(fd_sco)>=to_number(debt.fd_sco) and fd_vaild='1' and fd_rating_status='020' and fd_sco is not null)/(select count(*) from ns_debt_rating where fd_vaild='1'   and fd_rating_status='020' and fd_sco is not null)*100)end as bsFws, \r\n" + 
				"            ROUND(debt.fd_intern_bs,2) as internBs,debt.fd_final_name as finalName,debt.fd_intern_name as internName,debt.fd_asset_review as assetReview \r\n" + 
				"            from ns_debt_rating debt  \r\n" + 
				"            left join ns_prj_project prj on debt.fd_project_code = prj.project_number \r\n" + 
				"            left join ns_cfg_main_scale levelz on fd_scale_level = debt.fd_pd  \r\n" + 
				"            where debt.fd_id = ?\r\n" + 
				"            \r\n" + 
				"     ";
		List<ProjectRatingInfo> custInfo = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<ProjectRatingInfo>(ProjectRatingInfo.class), projectNo);
		if (custInfo.size() > 0) {
			custInfo.get(0).setZbfx(zbfx(projectNo));
			custInfo.get(0).setDxfx(debtMxb(projectNo));
			return custInfo.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 指标分析
	 */
	@Override
	public List<Map<String, Object>> debtMxb(String Custno) {
		String tj=" fd_status='1' ";
/*		Integer count=jdbcTemplate.queryForObject("select count(*) from ns_debt_temp where fd_debt_id=? and fd_status='2'",Integer.class, Custno);
		if(count>0) {
			 tj=" fd_status='2' ";
		}
*/		
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String sql = "select shot,ID,NAME,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS\r\n" + 
				"  from (select '1' AS shot,\r\n" + 
				"               rating.FD_ID AS ID,\r\n" + 
				"               ' 客户信用保障倍数' AS NAME,\r\n" + 
				"               '客户评级结果' AS FD_INDEXNAME,\r\n" + 
				"               FD_INDEXVALUE,\r\n" + 
				"               '1.0' AS FD_SCORE,\r\n" + 
				"               FD_QUALITATIVE_OPTIONS\r\n" + 
				"          from ns_debt_rating rating\r\n" + 
				"  left join ns_debt_temp temp\r\n" + 
				"    on rating.fd_id = temp.fd_debt_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"  left join ns_debt_indexes indexe\r\n" + 
				"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"         where fd_level = 'BS010'\r\n" + 
				"           and fd_indexCode = 'P036'\r\n" + 
				"        union all\r\n" + 
				"        select '1' AS shot,\r\n" + 
				"               rating.FD_ID AS ID,\r\n" + 
				"               ' 客户信用保障倍数' AS NAME,\r\n" + 
				"               '基准额度' AS FD_INDEXNAME,\r\n" + 
				"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') AS FD_INDEXVALUE,\r\n" + 
				"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') FD_SCORE,\r\n" + 
				"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') AS FD_QUALITATIVE_OPTIONS\r\n" + 
				"          from ns_debt_rating rating\r\n" + 
				"  left join ns_debt_temp temp\r\n" + 
				"    on rating.fd_id = temp.fd_debt_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"  left join ns_debt_indexes indexe\r\n" + 
				"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"         where fd_level = 'BS010'\r\n" + 
				"           and fd_indexCode = 'P036'\r\n" + 
				"        union all\r\n" + 
				"        select '5' AS shot,\r\n" + 
				"               rating.fd_id AS ID,\r\n" + 
				"               ' 客户信用保障倍数' as NAME,\r\n" + 
				"               '客户信用保障倍数' as FD_INDEXNAME,\r\n" + 
				"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
				"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') as FD_SCORE,\r\n" + 
				"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
				"          from ns_debt_rating rating\r\n" + 
				"  left join ns_debt_temp temp\r\n" + 
				"    on rating.fd_id = temp.fd_debt_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"  left join ns_debt_indexes indexe\r\n" + 
				"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
				"   and "+tj+"\r\n" + 
				"         where fd_level = 'BS010'\r\n" + 
				"           and fd_indexCode in ('BS004'))where id=?";
		 list1 = jdbcTemplate.queryForList(sql,Custno);
		 
		 	List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			 sql = " select shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       replace(FD_LEVEL, 'BS006_', '股权') AS NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '1' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               '股权初始价值' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"  left join ns_debt_temp temp\r\n" + 
			 		"    on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"  left join ns_debt_indexes indexe\r\n" + 
			 		"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"         where fd_level like 'BS006%'\r\n" + 
			 		"           and fd_indexCode in ('ZX010')\r\n" + 
			 		"         group by rating.fd_id, FD_LEVEL\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"  left join ns_debt_temp temp\r\n" + 
			 		"    on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"  left join ns_debt_indexes indexe\r\n" + 
			 		"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"         where fd_level like 'BS006%'\r\n" + 
			 		"           and fd_indexCode in ('P032')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_LEVEL,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '股权质押保障倍数' as NAME,\r\n" + 
			 		"               '股权质押保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(FD_Score AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" +
			 		"               '股权质押保障倍数' as FD_LEVEL\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"  left join ns_debt_temp temp\r\n" + 
			 		"    on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"  left join ns_debt_indexes indexe\r\n" + 
			 		"    on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"   and "+tj+"\r\n" + 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX010')\r\n" + 
			 		"         order by FD_LEVEL, shot) where id=?";
			 list2 = jdbcTemplate.queryForList(sql,Custno);
			 
			 List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			 sql = "select shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       replace(FD_LEVEL, 'BS005_', '不动产') AS NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '1' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               '不动产市场价值' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level like 'BS005%'\r\n" + 
			 		"           and fd_indexCode in ('ZX009_INIT')\r\n" + 
			 		"         group by rating.fd_id, FD_LEVEL\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from  ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS005%'\r\n" + 
			 		"           and fd_indexCode in ('P031', 'P030')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_LEVEL,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '不动产抵押保障倍数' as NAME,\r\n" + 
			 		"               '不动产抵押保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_Score, 'fm99999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(FD_Score AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm99999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" +
			 		"               '不动产抵押保障倍数' as FD_LEVEL\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX009')\r\n" + 
			 		"         order by FD_LEVEL, shot) -- \r\n" + 
			 		" where id=?";
			 list3 = jdbcTemplate.queryForList(sql,Custno);
			 
			 List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			 sql = "select shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       replace(FD_LEVEL, 'BS003_', '财务应收账款') AS NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '1' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               '应收账款账面价值' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level like 'BS003%'\r\n" + 
			 		"           and fd_indexCode in ('ZX008_INIT_VALUE')\r\n" + 
			 		"         group by rating.fd_id, FD_LEVEL\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"               FD_LEVEL\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS003%'\r\n" + 
			 		"           and fd_indexCode in ('P026', 'P027', 'P028' ， 'P029')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_LEVEL,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '应收账款质押保障倍数' as NAME,\r\n" + 
			 		"               '应收账款质押保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(FD_Score AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" +
			 		"               '应收账款质押保障倍数' as FD_LEVEL\r\n" + 
			 		"          from  ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX008')\r\n" + 
			 		"         order by FD_LEVEL, shot) where id=?";
			 list4 = jdbcTemplate.queryForList(sql,Custno);
			 
			 List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			 sql = "select *\r\n" + 
			 		"  from (select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '自然人保证' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level like 'BS007%'\r\n" + 
			 		"           and fd_indexCode in ('P024', 'P025_1', 'P025_2')\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '自然人保证保障倍数' as NAME,\r\n" + 
			 		"               '自然人保证保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(FD_SCORE AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX007'))\r\n" + 
			 		" where id=?";
			 list5 = jdbcTemplate.queryForList(sql,Custno);
			 List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
			 sql = "select shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '1' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '法人保证' as NAME,\r\n" + 
			 		"               '保证人经营性现金流净额' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS004%'\r\n" + 
			 		"           and fd_indexCode in ('ZX006_INIT')\r\n" + 
			 		"         group by rating.fd_id\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '法人保证' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS004%'\r\n" + 
			 		"           and fd_indexCode in ('P016', 'P017', 'P018')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select shot,\r\n" + 
			 		"               ID,\r\n" + 
			 		"               NAME,\r\n" + 
			 		"               FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE ||'%',\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from (select shot,\r\n" + 
			 		"                       ID,\r\n" + 
			 		"                       NAME,\r\n" + 
			 		"                       FD_INDEXNAME,\r\n" + 
			 		"                        to_char(CAST(((FD_INDEXVALUE / FXCK) * 100) AS NUMBER(10, 2))) AS FD_INDEXVALUE ,\r\n" + 
			 		"                       FD_SCORE,\r\n" + 
			 		"                       FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                       FXCK\r\n" + 
			 		"                  from (select '3' AS shot,\r\n" + 
			 		"                               rating.fd_id AS ID,\r\n" + 
			 		"                               '法人保证' as NAME,\r\n" + 
			 		"                               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"                               FD_INDEXVALUE,\r\n" + 
			 		"                               FD_SCORE,\r\n" + 
			 		"                               FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                               (select Avg(FD_INDEXVALUE)\r\n" + 
			 		"                                  from ns_debt_indexes\r\n" + 
			 		"                                 where FD_INDEXTYPE = indexe.FD_INDEXTYPE\r\n" + 
			 		"                                   and FD_INDEXCODE in ('P037')\r\n" + 
			 		"                                   and FD_LEVEL like 'BS004%') AS FXCK\r\n" + 
			 		"                          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"                         where fd_level like 'BS004%'\r\n" + 
			 		"                           and fd_indexCode in ('P020', 'P021'))\r\n" + 
			 		"                 group by shot,\r\n" + 
			 		"                          ID,\r\n" + 
			 		"                          NAME,\r\n" + 
			 		"                          FD_INDEXNAME,\r\n" + 
			 		"                          FD_INDEXVALUE,\r\n" + 
			 		"                          FD_SCORE,\r\n" + 
			 		"                          FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                          FXCK)\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '4' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '法人保证' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from     ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level like 'BS004%'\r\n" + 
			 		"           and fd_indexCode in ('P019', 'P022', 'P023')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '法人保证保障倍数' as NAME,\r\n" + 
			 		"               '法人保证保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_Score, 'fm9999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(FD_Score AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm9999990.99'), '.')  as FD_QUALITATIVE_OPTIONS\r\n" +
			 		"          from      ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX006')\r\n" + 
			 		"         order by shot)\r\n" + 
			 		" where id=?";
			 list6 = jdbcTemplate.queryForList(sql,Custno);
			 List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
			 sql = "select shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '1' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '租赁物净值' as NAME,\r\n" + 
			 		"               '租赁物净值' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_SCORE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(CAST(SUM(FD_SCORE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from  ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj + 
			 		"         where fd_level like 'BS009%'\r\n" + 
			 		"           and fd_indexCode in ('BS002_TZXZ')\r\n" + 
			 		"         group by rating.fd_id\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '2' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '可控性调整' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS009%'\r\n" + 
			 		"           and fd_indexCode in ('P007', 'P008', 'P009', 'P010', 'P011')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS \r\n" + 
			 		"        union all\r\n" + 
			 		"        select '3' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '保值性调整' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"         where fd_level like 'BS009%'\r\n" + 
			 		"           and fd_indexCode in ('P012')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '4' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '变现性调整' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level like 'BS009%'\r\n" + 
			 		"           and fd_indexCode in ('P013', 'P014', 'P015')\r\n" + 
			 		"         group by rating.fd_id,\r\n" + 
			 		"                  FD_INDEXNAME,\r\n" + 
			 		"                  FD_INDEXVALUE,\r\n" + 
			 		"                  FD_SCORE,\r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"        union all\r\n" + 
			 		"        select '5' AS shot,\r\n" + 
			 		"               rating.fd_id AS ID,\r\n" + 
			 		"               '租赁物保障倍数' as NAME,\r\n" + 
			 		"               '租赁物保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(FD_SCORE AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('BS002')\r\n" + 
			 		"         ) where id=? order by shot,fd_indexname";
			 list7= jdbcTemplate.queryForList(sql,Custno);
			 List<Map<String, Object>> list8 = new ArrayList<Map<String, Object>>();
			 sql = "select shot, \r\n" + 
			 		"                  ID, \r\n" + 
			 		"                  NAME, \r\n" + 
			 		"                  FD_INDEXNAME, \r\n" + 
			 		"                  FD_INDEXVALUE, \r\n" + 
			 		"                  FD_SCORE, \r\n" + 
			 		"                  FD_QUALITATIVE_OPTIONS \r\n" + 
			 		"             from (select '3' AS shot, \r\n" + 
			 		"                          rating.fd_id AS ID, \r\n" + 
			 		"                          '筹资性现金流保障倍数' as NAME, \r\n" + 
			 		"                          to_char(FD_INDEXNAME) AS FD_INDEXNAME, \r\n" + 
			 		"                          FD_INDEXVALUE, \r\n" + 
			 		"                          FD_SCORE, \r\n" + 
			 		"                          FD_QUALITATIVE_OPTIONS ,\r\n" + 
			 		"                          '' as FD_LEVEL\r\n" + 
			 		"                     from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"                    where fd_level like 'BS008%' \r\n" + 
			 		"                      and fd_indexCode in ('CZ002', 'CZ003')\r\n" + 
			 		"                      group by  rating.fd_id,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS \r\n" + 
			 		"                   union all \r\n" + 
			 		"                   select '2' AS shot, \r\n" + 
			 		"                          rating.fd_id AS ID, \r\n" + 
			 		"                          '筹资性现金流保障倍数' as NAME, \r\n" + 
			 		"                          FD_WEIGHT AS FD_INDEXNAME, \r\n" + 
			 		"                          FD_INDEXVALUE, \r\n" + 
			 		"                          FD_SCORE, \r\n" + 
			 		"                          FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                          FD_LEVEL \r\n" + 
			 		"                     from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+ 
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"                    where fd_level like 'BS008%' \r\n" + 
			 		"                      and FD_INDEXCODE = 'CZ001_2' \r\n" + 
			 		"                   union all \r\n" + 
			 		"                   select '1' AS shot, \r\n" + 
			 		"                          rating.fd_id AS ID, \r\n" + 
			 		"                          '筹资性现金流保障倍数' as NAME, \r\n" + 
			 		"                          '筹资金额' as FD_INDEXNAME, \r\n" + 
			 		"                         to_char(cast(SUM(FD_INDEXVALUE) AS NUMBER(10, 2))) as FD_INDEXVALUE, \r\n" + 
			 		"                  CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE, \r\n" + 
			 		"                  to_char(cast(SUM(FD_INDEXVALUE ) AS NUMBER(10, 2))) as FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                  '' as FD_LEVEL\r\n" + 
			 		"                     from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"                    where fd_level like 'BS008%' \r\n" + 
			 		"                      and fd_indexCode in ('CZ004') \r\n" + 
			 		"                    group by rating.fd_id \r\n" + 
			 		"                   union all \r\n" + 
			 		"                   select '4' AS shot, \r\n" + 
			 		"                          rating.fd_id AS ID, \r\n" + 
			 		"                          '筹资性现金流的保障倍数' as NAME, \r\n" + 
			 		"                          '筹资性现金流的保障倍数' as NAME, \r\n" + 
			 		"                          rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE, \r\n" + 
			 		"                          CAST(FD_SCORE AS NUMBER(10, 2)) as FD_SCORE, \r\n" + 
			 		"                          rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
			 		"                          FD_LEVEL \r\n" + 
			 		"                     from    ns_debt_rating rating \r\n" + 
			 		"			 		  left join ns_debt_temp temp \r\n" + 
			 		"			 		    on rating.fd_id = temp.fd_debt_id \r\n" + 
			 		"			 		   and "+tj+
			 		"			 		  left join ns_debt_indexes indexe \r\n" + 
			 		"			 		    on indexe.fd_indextype = temp.fd_index_id \r\n" + 
			 		"			 		   and "+tj+
			 		"                    where fd_level = 'BS010' \r\n" + 
			 		"                      and fd_indexCode in ('ZX001') \r\n" + 
			 		"                    order by shot,FD_LEVEL,fd_indexname) \r\n" + 
			 		"            where id= ?";
			 list8 = jdbcTemplate.queryForList(sql,Custno);
			 List<Map<String, Object>> list9 = new ArrayList<Map<String, Object>>();
			 sql = "select  shot,\r\n" + 
			 		"       ID,\r\n" + 
			 		"       NAME,\r\n" + 
			 		"       FD_INDEXNAME,\r\n" + 
			 		"       FD_INDEXVALUE,\r\n" + 
			 		"       FD_SCORE,\r\n" + 
			 		"       FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"  from (select '2' as shot, rating.fd_id AS ID,\r\n" + 
			 		"               '经营性现金流保障倍数' as NAME,\r\n" + 
			 		"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
			 		"               to_char(FD_INDEXVALUE) AS FD_INDEXVALUE,\r\n" + 
			 		"               FD_SCORE,\r\n" + 
			 		"               to_char(FD_QUALITATIVE_OPTIONS) AS FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"          left join ns_debt_temp temp\r\n" + 
			 		"            on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"          left join ns_debt_indexes indexe\r\n" + 
			 		"            on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('P001', 'P002', 'P003')\r\n" + 
			 		"        union all\r\n" + 
			 		"        select  '1' as shot, rating.fd_id AS ID,\r\n" + 
			 		"               '经营性现金流保障倍数' as NAME,\r\n" + 
			 		"               '经营性现金流净额' as FD_INDEXNAME,\r\n" + 
			 		"               rtrim(to_char(SUM(FD_INDEXVALUE), 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(to_char(SUM(FD_INDEXVALUE), 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"          left join ns_debt_temp temp\r\n" + 
			 		"            on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"          left join ns_debt_indexes indexe\r\n" + 
			 		"            on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('P041_1', 'P041_3', 'P041_2')\r\n" + 
			 		"         group by rating.fd_id\r\n" + 
			 		"        union all\r\n" + 
			 		"        select  '3' as shot, rating.fd_id AS ID,\r\n" + 
			 		"               '经营性现金流的保障倍数' as NAME,\r\n" + 
			 		"               '经营性现金流的保障倍数' as NAME,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
			 		"               CAST(FD_SCORE AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
			 		"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
			 		"          from ns_debt_rating rating\r\n" + 
			 		"          left join ns_debt_temp temp\r\n" + 
			 		"            on rating.fd_id = temp.fd_debt_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"          left join ns_debt_indexes indexe\r\n" + 
			 		"            on indexe.fd_indextype = temp.fd_index_id\r\n" + 
			 		"           and "+tj+"\r\n" + 
			 		"         where fd_level = 'BS010'\r\n" + 
			 		"           and fd_indexCode in ('ZX002')\r\n" + 
			 		"         order by shot,FD_INDEXNAME)\r\n" + 
			 		" where id = ? ";
			 list9 = jdbcTemplate.queryForList(sql,Custno);
			 
			 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			 addList(list,list9);
			 addList(list,list8);
			 addList(list,list7);
			 addList(list,list6);
			 addList(list,list5);
			 addList(list,list4);
			 addList(list,list3);
			 addList(list,list2);
			 addList(list,list1);
			 List<Map<String, Object>> listInit = new ArrayList<Map<String, Object>>();
			 for(int i=1;i<=9;i++) {
				 addList(listInit,bigType(String.valueOf(i),Custno));
			 }
		for (Map<String, Object> map : listInit) {
			//Map<String, Object> map = listInit.get(i);//复评
			//Map<String, Object> mapInit = list.get(i);//初评
			for (Map<String, Object> map2 : list) {
				if(map2.get("FD_INDEXNAME").equals(map.get("FD_INDEXNAME"))
						&& map2.get("NAME").equals(map.get("NAME"))) {
					String finl = map2.get("FD_INDEXVALUE") == null ? "" : map2.get("FD_INDEXVALUE").toString();
					map.put("finalRating", finl);
				}else {
					continue;
				}
			}
		}
		return listInit;
	}
public static void main(String[] args) {
	String str = "bAa1";
	Pattern p = Pattern.compile("^[A-Za-z]+$");
	Matcher m = p.matcher(str);
	boolean isValid = m.matches();
	
}
	public void addList(List<Map<String, Object>> list, List<Map<String, Object>> listShot) {
		 for (Map<String, Object> map : listShot) {
			 list.add(map);
		}
	}
	/**
	 * 指标明细
	 */
	@Override
	public List<Map<String, Object>> bigType(String type,String Custno) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(type.equals("")) {
			return list;
		}
		if(type.equals("9")) {
			String sql = "  select *\r\n" + 
					"  from (select '1' AS shot,\r\n" + 
					"               rating.FD_ID AS ID,\r\n" + 
					"               ' 客户信用保障倍数' AS NAME,\r\n" + 
					"               '客户评级结果' AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               '1.0' AS FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode = 'P036'\r\n" + 
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.FD_ID AS ID,\r\n" + 
					"               ' 客户信用保障倍数' AS NAME,\r\n" + 
					"               '基准额度' AS FD_INDEXNAME,\r\n" + 
					"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') AS FD_INDEXVALUE,\r\n" + 
					"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') FD_SCORE,\r\n" + 
					"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.') AS FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode = 'P036'\r\n" + 
					"        union all\r\n" + 
					"        select '3' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               ' 客户信用保障倍数' as NAME,\r\n" + 
					"               '客户信用保障倍数' as FD_INDEXNAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               rtrim(CAST(FD_SCORE AS NUMBER(10, 2)), '.')  as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('BS004'))\r\n" + 
					" where id = ?";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		if(type.equals("8")) {
			String sql = "select shot,\r\n" + 
					"       ID,\r\n" + 
					"       replace(FD_LEVEL, 'BS006_', '股权') AS NAME,\r\n" + 
					"       FD_INDEXNAME,\r\n" + 
					"       FD_INDEXVALUE,\r\n" + 
					"       FD_SCORE,\r\n" + 
					"       FD_QUALITATIVE_OPTIONS\r\n" + 
					"  from (select '1' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               '股权初始价值' as FD_INDEXNAME,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS006%'\r\n" + 
					"           and fd_indexCode in ('ZX010')\r\n" + 
					"         group by rating.fd_id, FD_LEVEL\r\n" + 
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS006%'\r\n" + 
					"           and fd_indexCode in ('P032')\r\n" + 
					"         group by rating.fd_id,\r\n" + 
					"                  FD_LEVEL,\r\n" + 
					"                  FD_INDEXNAME,\r\n" + 
					"                  FD_INDEXVALUE,\r\n" + 
					"                  FD_SCORE,\r\n" + 
					"                  FD_QUALITATIVE_OPTIONS\r\n" + 
					"        union all\r\n" + 
					"        select '3' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '股权质押保障倍数' as NAME,\r\n" + 
					"               '股权质押保障倍数' as NAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm9999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST((FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"              '股权质押保障倍数' as FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('ZX010'))--\r\n" + 
					" where id = ?\r\n" + 
					" order by FD_LEVEL, shot";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		
		
		if(type.equals("7")) {
			String sql = "select shot,\r\n" + 
					"       ID,\r\n" + 
					"       replace(FD_LEVEL, 'BS005_', '不动产') AS NAME,\r\n" + 
					"       FD_INDEXNAME,\r\n" + 
					"       FD_INDEXVALUE,\r\n" + 
					"       FD_SCORE,\r\n" + 
					"       FD_QUALITATIVE_OPTIONS\r\n" + 
					"  from (select '1' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               '不动产市场价值' as FD_INDEXNAME,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS005%'\r\n" + 
					"           and fd_indexCode in ('ZX009_INIT')\r\n" + 
					"         group by rating.fd_id, FD_LEVEL\r\n" + 
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS005%'\r\n" + 
					"           and fd_indexCode in ('P031', 'P030')\r\n" + 
					"         group by rating.fd_id,\r\n" + 
					"                  FD_LEVEL,\r\n" + 
					"                  FD_INDEXNAME,\r\n" + 
					"                  FD_INDEXVALUE,\r\n" + 
					"                  FD_SCORE,\r\n" + 
					"                  FD_QUALITATIVE_OPTIONS\r\n" + 
					"        union all\r\n" + 
					"        select '5' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '不动产抵押保障倍数' as NAME,\r\n" + 
					"               '不动产抵押保障倍数' as NAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm9999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST((FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"              '不动产抵押保障倍数' as FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('ZX009'))--\r\n" + 
					" where id = ?\r\n" + 
					" order by FD_LEVEL, shot";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		if(type.equals("6")) {
			String sql = "select shot,\r\n" + 
					"       ID,\r\n" + 
					"       replace(FD_LEVEL, 'BS003_', '财务应收账款') AS NAME,\r\n" + 
					"       FD_INDEXNAME,\r\n" + 
					"       FD_INDEXVALUE,\r\n" + 
					"       FD_SCORE,\r\n" + 
					"       FD_QUALITATIVE_OPTIONS\r\n" + 
					"  from (select '1' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               '应收账款账面价值' as FD_INDEXNAME,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS003%'\r\n" + 
					"           and fd_indexCode in ('ZX008_INIT_VALUE')\r\n" + 
					"         group by rating.fd_id, FD_LEVEL\r\n" + 
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS003%'\r\n" + 
					"           and fd_indexCode in ('P026', 'P027', 'P028' ， 'P029')\r\n" + 
					"         group by rating.fd_id,\r\n" + 
					"                  FD_LEVEL,\r\n" + 
					"                  FD_INDEXNAME,\r\n" + 
					"                  FD_INDEXVALUE,\r\n" + 
					"                  FD_SCORE,\r\n" + 
					"                  FD_QUALITATIVE_OPTIONS\r\n" + 
					"        union all\r\n" + 
					"        select '5' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '应收账款质押保障倍数' as NAME,\r\n" + 
					"               '应收账款质押保障倍数' as NAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm9999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST((FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               '应收账款质押保障倍数' as FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('ZX008'))\r\n" + 
					" where id = ?\r\n" + 
					" order by FD_LEVEL, shot ";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		
		
		
		
		
		if(type.equals("5")) {
			String sql = "select * from (\r\n" + 
					"select   \r\n" + 
					"'2' AS shot, rating.fd_id AS ID, '自然人保证' as NAME,to_char(FD_INDEXNAME) AS FD_INDEXNAME, FD_INDEXVALUE, FD_SCORE, FD_QUALITATIVE_OPTIONS\r\n" + 
					" from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level like 'BS007%' \r\n" + 
					"             and fd_indexCode in ('P024','P025_1','P025_2')\r\n" + 
					"             union all         \r\n" + 
					" select '5' AS shot, rating.fd_id AS ID, '自然人保证保障倍数' as NAME,'自然人保证保障倍数' as NAME,rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.')  as FD_INDEXVALUE,CAST(FD_SCORE AS NUMBER (10, 2) )   as FD_SCORE,rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS from ns_debt_indexes indexe  \r\n" + 
					"inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id  \r\n" + 
					"where fd_level='BS010'  \r\n" + 
					"and fd_indexCode in ('ZX007') )where  id =?";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		if(type.equals("4")) {
			String sql = "select shot,\r\n" + 
					"       ID,\r\n" + 
					"       NAME,\r\n" + 
					"       FD_INDEXNAME,\r\n" + 
					"       FD_INDEXVALUE,\r\n" + 
					"       FD_SCORE,\r\n" + 
					"       FD_QUALITATIVE_OPTIONS\r\n" + 
					"  from (select '1' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '法人保证' as NAME,\r\n" + 
					"               '保证人经营性现金流净额' as FD_INDEXNAME,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS004%'\r\n" + 
					"           and fd_indexCode in ('ZX006_INIT')\r\n" + 
					"         group by rating.fd_id\r\n" + 
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '法人保证' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS004%'\r\n" + 
					"           and fd_indexCode in ('P016', 'P017', 'P018')\r\n" + 
					"         group by rating.fd_id,\r\n" + 
					"                  FD_INDEXNAME,\r\n" + 
					"                  FD_INDEXVALUE,\r\n" + 
					"                  FD_SCORE,\r\n" + 
					"                  FD_QUALITATIVE_OPTIONS\r\n" + 
					"        union all\r\n" + 
					"        select shot,\r\n" + 
					"               ID,\r\n" + 
					"               NAME,\r\n" + 
					"               FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE ||'%',\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from (select shot,\r\n" + 
					"                       ID,\r\n" + 
					"                       NAME,\r\n" + 
					"                       FD_INDEXNAME,\r\n" + 
					"                        to_char(CAST(((FD_INDEXVALUE / FXCK) * 100) AS NUMBER(10, 2))) AS FD_INDEXVALUE,\r\n" + 
					"                       FD_SCORE,\r\n" + 
					"                       FD_QUALITATIVE_OPTIONS,\r\n" + 
					"                       FXCK\r\n" + 
					"                  from (select '3' AS shot,\r\n" + 
					"                               rating.fd_id AS ID,\r\n" + 
					"                               '法人保证' as NAME,\r\n" + 
					"                               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"                               FD_INDEXVALUE,\r\n" + 
					"                               FD_SCORE,\r\n" + 
					"                               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"                               (select Avg(FD_INDEXVALUE)\r\n" + 
					"                                  from ns_debt_indexes\r\n" + 
					"                                 where FD_INDEXTYPE = indexe.FD_INDEXTYPE\r\n" + 
					"                                   and FD_INDEXCODE in ('P037')\r\n" + 
					"                                   and FD_LEVEL like 'BS004%') AS FXCK\r\n" + 
					"                          from ns_debt_indexes indexe\r\n" + 
					"                         inner join ns_debt_rating rating\r\n" + 
					"                            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"                         where fd_level like 'BS004%'\r\n" + 
					"                           and fd_indexCode in ('P020', 'P021'))\r\n" + 
					"                 group by shot,\r\n" + 
					"                          ID,\r\n" + 
					"                          NAME,\r\n" + 
					"                          FD_INDEXNAME,\r\n" + 
					"                          FD_INDEXVALUE,\r\n" + 
					"                          FD_SCORE,\r\n" + 
					"                          FD_QUALITATIVE_OPTIONS,\r\n" + 
					"                          FXCK)\r\n" + 
					"        union all\r\n" + 
					"        select '4' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '法人保证' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS004%'\r\n" + 
					"           and fd_indexCode in ('P019', 'P022', 'P023')\r\n" + 
					"         group by rating.fd_id,\r\n" + 
					"                  FD_INDEXNAME,\r\n" + 
					"                  FD_INDEXVALUE,\r\n" + 
					"                  FD_SCORE,\r\n" + 
					"                  FD_QUALITATIVE_OPTIONS\r\n" + 
					"        union all\r\n" + 
					"        select '5' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '法人保证保障倍数' as NAME,\r\n" + 
					"               '法人保证保障倍数' as NAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm9999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST((FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_Score, 'fm999999999990.99'), '.')  as FD_QUALITATIVE_OPTIONS\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('ZX006'))\r\n" + 
					" where ID = ? \r\n" + 
					" order by shot\r\n" + 
					"";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		if(type.equals("3")) {
			String sql = " select shot,ID,NAME,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS from (\r\n" + 
					" select   \r\n" + 
					"'1' AS shot, rating.fd_id AS ID, '租赁物净值' as NAME,'租赁物净值' as FD_INDEXNAME,\r\n" + 
					"rtrim(CAST(SUM(FD_SCORE) AS NUMBER (10, 2)), '.')   as FD_INDEXVALUE, \r\n" + 
					"CAST(SUM(FD_SCORE) AS NUMBER (10, 2)) as FD_SCORE,\r\n" + 
					"rtrim(CAST(SUM(FD_SCORE) AS NUMBER (10, 2)), '.')  as FD_QUALITATIVE_OPTIONS\r\n" + 
					" from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level like 'BS009%' \r\n" + 
					"             and fd_indexCode in ('BS002_TZXZ') \r\n" + 
					"             group by  rating.fd_id\r\n" + 
					"union all\r\n" + 
					"              select   \r\n" + 
					"'2' AS shot, rating.fd_id AS ID, '可控性调整' as NAME,to_char(FD_INDEXNAME) AS FD_INDEXNAME, FD_INDEXVALUE, FD_SCORE, FD_QUALITATIVE_OPTIONS\r\n" + 
					" from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level like 'BS009%' \r\n" + 
					"             and fd_indexCode in ('P007','P008','P009','P010','P011') \r\n" + 
					"             group by  rating.fd_id,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS\r\n" + 
					"union all  \r\n" + 
					"                      select   \r\n" + 
					"'3' AS shot, rating.fd_id AS ID, '保值性调整' as NAME,to_char(FD_INDEXNAME) AS FD_INDEXNAME, FD_INDEXVALUE, FD_SCORE, FD_QUALITATIVE_OPTIONS\r\n" + 
					" from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level like 'BS009%' \r\n" + 
					"             and fd_indexCode in ('P012') \r\n" + 
					"             group by  rating.fd_id,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS\r\n" + 
					"union all\r\n" + 
					"                      select   \r\n" + 
					"'4' AS shot, rating.fd_id AS ID, '变现性调整' as NAME,to_char(FD_INDEXNAME) AS FD_INDEXNAME, FD_INDEXVALUE, FD_SCORE, FD_QUALITATIVE_OPTIONS\r\n" + 
					" from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level like 'BS009%' \r\n" + 
					"             and fd_indexCode in ('P013','P014','P015') \r\n" + 
					"             group by  rating.fd_id,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS\r\n" + 
					"             union all\r\n" + 
					"select '5' AS shot, rating.fd_id AS ID, '租赁物保障倍数' as NAME,'租赁物保障倍数' as NAME,rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.')  as FD_INDEXVALUE,CAST(FD_SCORE AS NUMBER (10, 2) )   as FD_SCORE,rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS from ns_debt_indexes indexe \r\n" + 
					"            inner join ns_debt_rating rating on indexe.fd_indextype = rating.fd_pro_id \r\n" + 
					"            where fd_level='BS010' \r\n" + 
					"             and fd_indexCode in ('BS002')\r\n" + 
					" ) where  ID = ?   order by shot,fd_indexname ";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		
		
		
		if(type.equals("2")) {
			String sql = "select shot,\r\n" + 
					"       ID,\r\n" + 
					"       NAME,\r\n" + 
					"       FD_INDEXNAME,\r\n" + 
					"       FD_INDEXVALUE,\r\n" + 
					"       FD_SCORE,\r\n" + 
					"       FD_QUALITATIVE_OPTIONS\r\n" + 
					"  from (select '3' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '筹资性现金流保障倍数' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               '' as FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS008%'\r\n" + 
					"           and fd_indexCode in ('CZ002', 'CZ003')\r\n" + 
					"     group by  rating.fd_id,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS "+
					"        union all\r\n" + 
					"        select '2' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '筹资性现金流保障倍数' as NAME,\r\n" + 
					"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
					"               FD_INDEXVALUE,\r\n" + 
					"               FD_SCORE,\r\n" + 
					"               FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS008%'\r\n" + 
					"           and FD_INDEXCODE = 'CZ001_2'\r\n" + 
					"        union all\r\n" + 
					"        select '1' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '筹资性现金流保障倍数' as NAME,\r\n" + 
					"               '筹资金额' as FD_INDEXNAME,\r\n" + 
					"              to_char(cast(SUM(FD_INDEXVALUE) AS NUMBER(10, 2))) as FD_INDEXVALUE,\r\n" + 
					"        CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"        to_char(cast(SUM(FD_INDEXVALUE ) AS NUMBER(10, 2))) as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"        '' as FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level like 'BS008%'\r\n" + 
					"           and fd_indexCode in ('CZ004')\r\n" + 
					"         group by rating.fd_id\r\n" + 
					"        union all\r\n" + 
					"        select '4' AS shot,\r\n" + 
					"               rating.fd_id AS ID,\r\n" + 
					"               '筹资性现金流的保障倍数' as NAME,\r\n" + 
					"               '筹资性现金流的保障倍数' as NAME,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
					"               CAST((FD_SCORE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
					"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS,\r\n" + 
					"               FD_LEVEL\r\n" + 
					"          from ns_debt_indexes indexe\r\n" + 
					"         inner join ns_debt_rating rating\r\n" + 
					"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
					"         where fd_level = 'BS010'\r\n" + 
					"           and fd_indexCode in ('ZX001'))\r\n" + 
					" where ID = ?\r\n" + 
					" order by shot,FD_LEVEL,fd_indexname";
			 list = jdbcTemplate.queryForList(sql,Custno);
		}
		
		if(type.equals("1")) {
				String sql = "select shot,\r\n" + 
						"       ID,\r\n" + 
						"       NAME,\r\n" + 
						"       FD_INDEXNAME,\r\n" + 
						"       FD_INDEXVALUE,\r\n" + 
						"       FD_SCORE,\r\n" + 
						"       FD_QUALITATIVE_OPTIONS\r\n" + 
						"  from (select '2' as shot,\r\n" + 
						"               rating.fd_id AS ID,\r\n" + 
						"               '经营性现金流保障倍数' as NAME,\r\n" + 
						"               to_char(FD_INDEXNAME) AS FD_INDEXNAME,\r\n" + 
						"               to_char(FD_INDEXVALUE) AS FD_INDEXVALUE,\r\n" + 
						"               FD_SCORE,\r\n" + 
						"               to_char(FD_QUALITATIVE_OPTIONS) AS FD_QUALITATIVE_OPTIONS\r\n" + 
						"          from ns_debt_indexes indexe\r\n" + 
						"         inner join ns_debt_rating rating\r\n" + 
						"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
						"         where fd_level = 'BS010'\r\n" + 
						"           and fd_indexCode in ('P001', 'P002', 'P003')\r\n" + 
						"        union all\r\n" + 
						"        select '1' as shot,\r\n" + 
						"               rating.fd_id AS ID,\r\n" + 
						"               '经营性现金流保障倍数' as NAME,\r\n" + 
						"               '经营性现金流净额' as FD_INDEXNAME,\r\n" + 
						"               rtrim(to_char(SUM(FD_INDEXVALUE), 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
						"               CAST(SUM(FD_INDEXVALUE) AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
						"               rtrim(to_char(SUM(FD_INDEXVALUE), 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
						"          from ns_debt_indexes indexe\r\n" + 
						"         inner join ns_debt_rating rating\r\n" + 
						"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
						"         where fd_level = 'BS010'\r\n" + 
						"           and fd_indexCode in ('P041_1', 'P041_3', 'P041_2')\r\n" + 
						"         group by rating.fd_id\r\n" + 
						"        union all\r\n" + 
						"        select '3' as shot,\r\n" + 
						"               rating.fd_id AS ID,\r\n" + 
						"               '经营性现金流的保障倍数' as NAME,\r\n" + 
						"               '经营性现金流的保障倍数' as NAME,\r\n" + 
						"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_INDEXVALUE,\r\n" + 
						"               CAST(FD_SCORE AS NUMBER(10, 2)) as FD_SCORE,\r\n" + 
						"               rtrim(to_char(FD_SCORE, 'fm999999999990.99'), '.') as FD_QUALITATIVE_OPTIONS\r\n" + 
						"          from ns_debt_indexes indexe\r\n" + 
						"         inner join ns_debt_rating rating\r\n" + 
						"            on indexe.fd_indextype = rating.fd_pro_id\r\n" + 
						"         where fd_level = 'BS010'\r\n" + 
						"           and fd_indexCode in ('ZX002')\r\n" + 
						"         order by shot,FD_INDEXNAME)\r\n" + 
						" where  ID = ?    ";
				list = jdbcTemplate.queryForList(sql,Custno);
			}
		for(Map<String,Object> map : list) {
			String option = map.get("FD_QUALITATIVE_OPTIONS")==null?"":map.get("FD_QUALITATIVE_OPTIONS").toString();
			Pattern pattern = Pattern.compile("[0-9]{1,}");
			Matcher matcher = pattern.matcher((CharSequence)option);
			boolean result=matcher.matches();
			if(result) {
				map.put("FD_QUALITATIVE_OPTIONS",option+".00");
			}else {
				map.put("FD_QUALITATIVE_OPTIONS",option);
			}
			 option = map.get("FD_INDEXVALUE")==null?"":map.get("FD_INDEXVALUE").toString();
			 pattern = Pattern.compile("[0-9]{1,}");
			 matcher = pattern.matcher((CharSequence)option);
			 result=matcher.matches();
			if(result) {
				map.put("FD_INDEXVALUE",option+".00");
			}else {
				map.put("FD_INDEXVALUE",option);
			}
			 option = map.get("FD_SCORE")==null?"":map.get("FD_SCORE").toString();
			 pattern = Pattern.compile("[0-9]{1,}");
			 matcher = pattern.matcher((CharSequence)option);
			 result=matcher.matches();
			if(result) {
				map.put("FD_SCORE",option+".00");
			}else {
				map.put("FD_SCORE",option);
			}
			
			
		}
		
		
		return list;
	}

	@Override
	public List<Map<String, Object>> zbfx(String projectNo) {
		String sql = "select * from (\r\n" + 
				"select FD_id as id,A,B,C,D,E,(F/L) AS F,GH,I,'现金流'J,K,L from (\r\n" + 
				"select debt.fd_id,'1'as  A, '现金流保障倍数' as B, FD_XJL_SCO as C,\r\n" + 
				"((select count(*)-1 from ns_debt_rating where  to_number(fd_xjl_sco)>=to_number(debt.fd_xjl_sco) and fd_vaild='1' and fd_sco is not null)/(select count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'经营性现金流' as E,\r\n" + 
				"to_char((select SUM(fd_score)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P042') and FD_LEVEL = 'BS010')) as F,\r\n" + 
				"(select exp(sum(ln(abs(fd_score))))*power(-1,sum(decode(sign(fd_score),-1,1,0)))  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P001','P002','P003')) as GH,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX002') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"'1' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037_1') and FD_LEVEL = 'BS010') as L\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode = 'BS001'\r\n" + 
				")XJL\r\n" + 
				"union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,(F/L) AS F,I/(F/L) as GH,I,'筹资性'J, K,L from (\r\n" + 
				"select debt.fd_id,'2'as  A, '现金流保障倍数' as B, FD_XJL_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(fd_xjl_sco)>= to_number(debt.fd_xjl_sco) and fd_vaild='1' and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'筹资性现金流' as E,\r\n" + 
				"to_char((select SUM(FD_INDEXVALUE)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('CZ004') and FD_LEVEL like 'BS008%')) as F,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('CZX001') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"'2' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037_1') and FD_LEVEL = 'BS010') as L\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode = 'CZX001'\r\n" + 
				")CZX\r\n" + 
				"union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,(F/L) AS F,GH ,I,'租赁物'J, K,L from (\r\n" + 
				"select debt.fd_id,'2'as  A, '租赁物保障倍数' as B, FD_ZLW_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(fd_zlw_sco)>=to_number(debt.fd_zlw_sco )and fd_vaild='1' and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1' and fd_sco is not null)*100) as D,\r\n" + 
				"'租赁物' as E,\r\n" + 
				"(select SUM(fd_score)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('BS002_TZXZ') and FD_LEVEL like 'BS009%') as F,\r\n" + 
				"(select exp(sum(ln(abs(fd_score))))*power(-1,sum(decode(sign(fd_score),-1,1,0)))  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and \r\n" + 
				"FD_INDEXCODE in ('ZX003','ZX004','ZX005') and fd_level='BS009_1' and fd_score>0)*(select (case when sum(fd_score) is null then 1 else 0 end) from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX003','ZX004','ZX005') and fd_score=0 and fd_level='BS009_1') as GH,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('BS002') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"'3' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode = 'BS002'\r\n" + 
				")ZLW\r\n" + 
				"union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,(F/L) As F,GH,I,'增信措施'J, K,L  from (\r\n" + 
				"select debt.fd_id,'4'as  A, '增信措施保障倍数' as B, FD_ZXCS_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(FD_ZXCS_SCO)>=to_number(debt.FD_ZXCS_SCO) and fd_vaild='1' and fd_sco is not null)/(select count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'法人保证'as E,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE in ('ZX006_INIT') and FD_LEVEL like 'BS004%') as F,\r\n" + 
				"(select exp(sum(ln(abs(fd_score))))*power(-1,sum(decode(sign(fd_score),-1,1,0)))   from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE like 'P%'  and FD_LEVEL like 'BS004%' and   fd_score<>0\r\n" + 
				")*(select (case when sum(fd_score) is null then 1 else 0 end)   from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE like 'P%'  and FD_LEVEL like 'BS004%' and   fd_score=0) as GH,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX006') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"'4' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode in('ZX006')\r\n" + 
				")frbz\r\n" + 
				"union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,F,1 as GH,I,'增信措施保障倍数'J, K,L from (\r\n" + 
				"select debt.fd_id,'5'as  A, '增信措施保障倍数' as B, FD_ZXCS_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where  to_number(FD_ZXCS_SCO)>=to_number(debt.FD_ZXCS_SCO) and fd_vaild='1'  and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'自然人保证'as E,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE in ('ZX007') and FD_LEVEL like 'BS007%') as F,\r\n" +
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX007') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"'5' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode in('ZX007')\r\n" + 
				")zrrbz\r\n" + 
				"union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,(F/L) AS F,I/(F/L) AS GH,I,'增信措施'J, K,L from (\r\n" + 
				"select debt.fd_id,'6'as  A, '增信措施保障倍数' as B, FD_ZXCS_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(FD_ZXCS_SCO)>=to_number(debt.FD_ZXCS_SCO) and fd_vaild='1' and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'应收账款质押'as E,\r\n" + 
				"(select sum(FD_INDEXVALUE)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE in ('ZX008_INIT_VALUE') and FD_LEVEL like 'BS003%') as F,\r\n" + 
				"'6' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX008') and FD_LEVEL = 'BS010') as I,\r\n" + 
				"FD_LEVEL,FD_INDEXCODE,FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode in('ZX008')\r\n" + 
				" order by indexs.FD_CREATE_DATE)yszk\r\n" + 
				" union all\r\n" + 
				"  select FD_id as id,A,B,C,D,E,(F/L) AS F,(case when  F/L=0 then 0 else  (I/(F/L)) end) AS GH,I,'增信措施'J, K,L from (\r\n" + 
				"select debt.fd_id,'7'as  A, '增信措施保障倍数' as B, FD_ZXCS_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(FD_ZXCS_SCO)>=to_number(debt.FD_ZXCS_SCO) and fd_vaild='1'  and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'不动产抵押'as E,\r\n" + 
				"(select sum(FD_INDEXVALUE)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE in ('ZX009_INIT') and FD_LEVEL like 'BS005%') as F,\r\n" + 
				"'7' K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX009') and FD_LEVEL = 'BS010') as I\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode in('ZX009')\r\n" + 
				" order by indexs.FD_CREATE_DATE)bdc\r\n" + 
				" union all\r\n" + 
				"  select FD_id as id,A,B,C,D,E,(F/L) AS F,I/(F/L) AS GH,I,'增信措施'J, K ,L from (\r\n" + 
				"select debt.fd_id,'8'as  A, '增信措施保障倍数' as B, FD_ZXCS_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where   to_number(FD_ZXCS_SCO)>=to_number(debt.FD_ZXCS_SCO) and fd_vaild='1'  and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'股权质押'as E,\r\n" + 
				"(select sum(FD_INDEXVALUE)  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and  FD_INDEXCODE in ('ZX010') and FD_LEVEL like 'BS006%') as F,\r\n" + 
				"'8' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX010') and FD_LEVEL = 'BS010') as I\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" FD_LEVEL = 'BS010' and fd_indexCode in('ZX010')\r\n" + 
				" order by indexs.FD_CREATE_DATE)gq\r\n" + 
				" \r\n" + 
				" union all\r\n" + 
				"select FD_id as id,A,B,C,D,E,F,0 AS GH,0 as I,'客户信用保障倍数'J, K ,L from (\r\n" + 
				"select debt.fd_id,'9'as  A, '客户信用保障倍数' as B, FD_KHXY_SCO as C,\r\n" + 
				"((select  count(*)-1 from ns_debt_rating where  to_number(FD_KHXY_SCO)>=to_number(debt.FD_KHXY_SCO) and fd_vaild='1'  and fd_sco is not null)/(select  count(*)-1 from ns_debt_rating where fd_vaild='1'  and fd_sco is not null)*100) as D,\r\n" + 
				"'客户评级结果'as E,\r\n" + 
				"0 as F,\r\n" + 
				"'9' as K,\r\n" + 
				"(select fd_score  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('P037') and FD_LEVEL = 'BS010') as L,\r\n" + 
				"(select fd_INDEXVALUE  from ns_debt_indexes where FD_INDEXTYPE = indexs.FD_INDEXTYPE and FD_INDEXCODE in ('ZX010') and FD_LEVEL = 'BS010') as I\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where FD_LEVEL = 'BS010' and fd_indexCode in('BS004') order by indexs.FD_CREATE_DATE) KHXY)\r\n " +
				"where id = ?" ; 
		

		List<Map<String, Object>> custInfo = jdbcTemplate.queryForList(sql, projectNo);

		return custInfo;
	}

	// select * from ns_debt_indexes where fd_dx_text is not null
	@Override
	public List<List<Dxfx>> dxfx(String projectNo) {
		List<List<Dxfx>> result = new ArrayList<List<Dxfx>>();
		List<Dxfx> resultDx = new ArrayList<Dxfx>();
		String sql ="select * from (select debt.FD_ID,'经营性现金流' as bsName,FD_INDEXNAME as zlName,FD_INDEXVALUE as dwOption,FD_QUALITATIVE_OPTIONS as dwDescribe,\r\n" + 
				"FD_INDEXVALUE as initRating,FD_INDEXVALUE as finalRating\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" fd_INDEXCODE in ('P001','P002','P042') and fd_LEVEL='BS010'\r\n" + 
				" order by indexs.FD_CREATE_DATE)jyx\r\n" + 
				" union all\r\n" + 
				"select * from (select debt.FD_ID,'筹资性现金流' as bsName,FD_INDEXNAME as zlName,FD_INDEXVALUE as dwOption,FD_QUALITATIVE_OPTIONS as dwDescribe,\r\n" + 
				"FD_INDEXVALUE as initRating,FD_INDEXVALUE as finalRating\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where fd_INDEXCODE in ('CZ001_1','P002') or (fd_LEVEL='BS010'and fd_INDEXCODE= 'P042')\r\n" + 
				" order by indexs.FD_CREATE_DATE)czx\r\n" + 
				"where  czx.fd_id = ?";
		resultDx = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Dxfx>(Dxfx.class),projectNo);
		result.add(resultDx);
		sql ="select * from (select debt.FD_ID,'经营性现金流' as bsName,FD_INDEXNAME as zlName,FD_INDEXVALUE as dwOption,FD_QUALITATIVE_OPTIONS as dwDescribe,\r\n" + 
				"FD_INDEXVALUE as initRating,FD_INDEXVALUE as finalRating\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where\r\n" + 
				" fd_INDEXCODE in ('P007','P008','P009','P010','P011','P012','P013','P014','P015') \r\n" + 
				" order by indexs.FD_CREATE_DATE)zlw\r\n" + 
				"where  fd_id = ?";
		resultDx = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Dxfx>(Dxfx.class),projectNo);
		result.add(resultDx);
		
		
		sql ="select * from (select debt.FD_ID,(case fd_level when 'BS003' then '应收账款' when 'BS004' then '法人保证' when 'BS005' then '不动产' when 'BS006' then '股权'  when 'BS007' then '自然人' end)  as bsName,FD_INDEXNAME as zlName,FD_INDEXVALUE as dwOption,FD_QUALITATIVE_OPTIONS as dwDescribe,\r\n" + 
				"				FD_INDEXVALUE as initRating,FD_INDEXVALUE as finalRating\r\n" + 
				"				from ns_debt_indexes indexs\r\n" + 
				"				inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"				where\r\n" + 
				"				 fd_INDEXCODE like 'P%'  and fd_level in ('BS003','BS004','BS005','BS006','BS007')\r\n" + 
				"				 order by indexs.FD_CREATE_DATE)zlw\r\n" + 
				"				where  fd_id = ?";
		resultDx = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Dxfx>(Dxfx.class),projectNo);
		result.add(resultDx);
		

		sql ="select * from (\r\n" + 
				"select * from (\r\n" + 
				"select debt.FD_ID, '' as bsName,FD_INDEXNAME as zlName,FD_INDEXVALUE as dwOption,FD_QUALITATIVE_OPTIONS as dwDescribe,\r\n" + 
				"FD_INDEXVALUE as initRating,FD_INDEXVALUE as finalRating\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where  fd_level in ('BS010') and fd_indexCode in('P036','P037'))\r\n" + 
				"union all\r\n" + 
				"select * from (\r\n" + 
				"select  debt.FD_ID,'' as bsName,FD_INDEXNAME as zlName,to_char(FD_SCORE) as dwOption,'' as dwDescribe,\r\n" + 
				"to_char(FD_SCORE) as initRating,to_char(FD_SCORE) as finalRating\r\n" + 
				"from ns_debt_indexes indexs\r\n" + 
				"inner join ns_debt_rating debt on fd_pro_id = fd_indexType\r\n" + 
				"where  fd_level in ('BS010') and fd_indexCode in('P036')))khxy\r\n" + 
				"where  fd_id = ?";
		resultDx = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Dxfx>(Dxfx.class),projectNo);
		result.add(resultDx);
		
		return result;
	}

	public Sheet excelParsing(String path, Integer index) throws Exception {
		InputStream is = new FileInputStream(new File(path));
		Workbook hssfWorkbook = null;
		if (path.endsWith("xlsx")) {
			hssfWorkbook = new XSSFWorkbook(is);
		} else if (path.endsWith("xls")) {
			hssfWorkbook = new HSSFWorkbook(is);
		}
		Sheet hssfSheet = hssfWorkbook.getSheetAt(index);// 获取下标为0的sheet也，该页面为项目基础信息
		return hssfSheet;
	}

	/**
	 * @throws Exception
	 * @债项评级基础信息 @返回基础信息的id值--0
	 */
	@Override
	public String prjInfo(String path,String proCode,String ratingLevel) throws Exception {
		Sheet hssfSheet = excelParsing(path, 0);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			if(row!=null) {
				String cell0 = proCode;// 项目编号
				String cell1 = row.getCell(0) == null ? "" : row.getCell(0).toString();// 租赁余额
				String cell2 = row.getCell(1) == null ? "" : row.getCell(1).toString();// 租赁剩余期限
				String cell3 = row.getCell(2) == null ? "" : row.getCell(2).toString();// 保证金
				String cell4 = row.getCell(3) == null ? "" : row.getCell(3).toString();// 基准利率
				String cell6 = row.getCell(4) == null ? "" : row.getCell(4).toString();// 资产总计
				String cell5  = row.getCell(5) == null ? "" : row.getCell(5).toString();// 营业收入
				String cell7 = ratingLevel;// 主体评级
				String cell8 = excelParsing(path, 1).getRow(2).getCell(1) == null ? ""
						: excelParsing(path, 1).getRow(2).getCell(1).toString();// 主体评级
				uuid = UUID.randomUUID().toString();
				String sql = "insert into ns_prj_Basic_info values(?,?,?,?,?,?,?,?,?,?)";

				try {
					if (jdbcTemplate.update(sql, uuid, cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8) < 1)
						uuid = "";
				} catch (Exception e) {
					uuid = "";
					e.printStackTrace();
				}
			}
			
		}
		return uuid;
	}

	/**
	 * @throws Exception
	 * @债项评级经营性现金流
	 */
	@Override
	public boolean prjOperatingFlow(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 1);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			
			Cell cell2 = row.getCell(1);
			String cell2Str="0";
			if(cell2!=null&&!cell2.toString().equals("")) {
				cell2Str=cell2.toString();
			}
			//String cell3 = row.getCell(2) == null ? "0" : row.getCell(2).toString();
			
			Cell cell3 = row.getCell(2);
			String cell3Str="0";
			if(cell3!=null&&!cell3.toString().equals("")) {
				cell3Str=cell3.toString();
			}
			
			//String cell4 = row.getCell(3) == null ? "0" : row.getCell(3).toString();
			Cell cell4 = row.getCell(3);
			String cell4Str="0";
			if(cell4!=null&&!cell4.toString().equals("")) {
				cell4Str=cell4.toString();
			}
			String cell5 = row.getCell(4) == null ? "" : row.getCell(4).toString();
			uuid = UUID.randomUUID().toString();
			String sql = "insert into ns_prj_Operating_Flow values(?,?,?,?,?,?)";
			try {
				if (jdbcTemplate.update(sql, uuid, prjId, cell2Str, cell3Str, cell4Str, cell5) > 0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @债项评级筹资性现金流
	 */
	@Override
	public boolean prjRaiseSexual(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 2);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell2 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell3 = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String cell4 = row.getCell(3) == null ? "" : row.getCell(3).toString();
			String cell5 = excelParsing(path, 0).getRow(2).getCell(2) == null ? ""
					: excelParsing(path, 0).getRow(2).getCell(2).toString();// 主体评级
			uuid = UUID.randomUUID().toString();
			
			if (org.apache.commons.lang3.StringUtils.isNotBlank(cell2)
					&& org.apache.commons.lang3.StringUtils.isNotBlank(cell3)
					&& org.apache.commons.lang3.StringUtils.isNotBlank(cell4)
					&& org.apache.commons.lang3.StringUtils.isNotBlank(cell5)) {
				String sql = "insert into ns_prj_raise values(?,?,?,?,?,?)";
				try {
					if (jdbcTemplate.update(sql, uuid, prjId, cell2, cell3, cell4, cell5) > 0) {
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @租赁物定性指标
	 */
	@Override
	public boolean prjLease(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 3);
		String uuid = "";
		String cell1 = hssfSheet.getRow(1).getCell(2) == null ? "" : hssfSheet.getRow(1).getCell(2).toString();
		String cell2 = hssfSheet.getRow(2).getCell(2) == null ? "" : hssfSheet.getRow(2).getCell(2).toString();
		String cell3 = hssfSheet.getRow(3).getCell(2) == null ? "" : hssfSheet.getRow(3).getCell(2).toString();
		String cell4 = hssfSheet.getRow(4).getCell(2) == null ? "" : hssfSheet.getRow(4).getCell(2).toString();
		String cell5 = hssfSheet.getRow(5).getCell(2) == null ? "" : hssfSheet.getRow(5).getCell(2).toString();
		String cell6 = hssfSheet.getRow(6).getCell(2) == null ? "" : hssfSheet.getRow(6).getCell(2).toString();
		String cell7 = hssfSheet.getRow(7).getCell(2) == null ? "" : hssfSheet.getRow(7).getCell(2).toString();
		String cell8 = hssfSheet.getRow(8).getCell(2) == null ? "" : hssfSheet.getRow(8).getCell(2).toString();
		String cell9 = hssfSheet.getRow(9).getCell(2) == null ? "" : hssfSheet.getRow(9).getCell(2).toString();
		uuid = UUID.randomUUID().toString();
		String sql = "insert into NS_lease_qualitative values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			if (jdbcTemplate.update(sql, uuid, prjId, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8,
					cell9) > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param debtId
	 * @租赁物数据解析入库
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean leaseItem(String path, String debtId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 3);
		for (int i = 14; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String leaseType = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String leaseYsy = row.getCell(4) == null ? "" : row.getCell(4).toString();
			
			String isNot = row.getCell(6) == null ? "" : row.getCell(6).toString();
			String leaseInit = row.getCell(7) == null ? "" : row.getCell(7).toString();
			String uuid = UUID.randomUUID().toString();
			if (org.apache.commons.lang3.StringUtils.isNotBlank(leaseType)&&!isNot.equals("否")) {
				if (!org.apache.commons.lang3.StringUtils.isNotBlank(leaseYsy)) {
					leaseYsy = "0";
				}
				String sql = "insert into NS_lease_item values(?,?,?,?,?)";
				try {
					if (jdbcTemplate.update(sql, uuid, debtId, leaseType, leaseYsy, leaseInit) > 0) {
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @不动产信息
	 */
	@Override
	public boolean prjRealEstate(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 4);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell1 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell2 = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String cell3 = row.getCell(3) == null ? "" : row.getCell(3).toString();
			uuid = UUID.randomUUID().toString();
			String sql = "insert into ns_prj_real_estate values(?,?,?,?,?)";
			try {
				if (jdbcTemplate.update(sql, uuid, prjId, cell1, cell2, cell3) > 0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @法人保障价值
	 */
	@Override
	public boolean prjLegalPerson(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 5);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell1 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell2 = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String cell3 = row.getCell(3) == null ? "" : row.getCell(3).toString();
			String cell4 = row.getCell(4) == null ? "" : row.getCell(4).toString();
			String cell5 = row.getCell(5) == null ? "" : row.getCell(5).toString();// 定性
			String cell6 = row.getCell(6) == null ? "" : row.getCell(6).toString();
			String cell7 = row.getCell(7) == null ? "" : row.getCell(7).toString();
			String cell8 = row.getCell(8) == null ? "" : row.getCell(8).toString();
			String cell9 = row.getCell(9) == null ? "" : row.getCell(9).toString();
			if(!cell4.equals("")) {
				uuid = UUID.randomUUID().toString();
				String sql = "insert into NS_prj_legal_person values(?,?,?,?,?,?,?,?,?,?,?)";
				try {
					if (jdbcTemplate.update(sql, uuid, prjId, cell7, cell8, cell9, cell4, cell1,cell6,cell5, cell2, cell3) > 0) {
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
			
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @股权保障价值
	 */
	@Override
	public boolean prjEquity(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 6);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell1 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell2 = row.getCell(2) == null ? "" : row.getCell(2).toString();

			if (org.apache.commons.lang3.StringUtils.isNotBlank(cell1)
					&& org.apache.commons.lang3.StringUtils.isNotBlank(cell2)) {
				uuid = UUID.randomUUID().toString();
				String sql = "insert into ns_prj_Equity values(?,?,?,?)";
				try {
					if (jdbcTemplate.update(sql, uuid, prjId, cell1, cell2) > 0) {
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return flag;
	}

	/**
	 * @throws Exception
	 * @应收账款
	 */
	@Override
	public boolean prjAccount(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 7);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell1 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell2 = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String cell3 = row.getCell(3) == null ? "" : row.getCell(3).toString();
			String cell4 = row.getCell(4) == null ? "" : row.getCell(4).toString();
			String cell5 = row.getCell(5) == null ? "" : row.getCell(5).toString();
			uuid = UUID.randomUUID().toString();
			if(!cell1.equals("")) {
				String sql = "insert into ns_prj_Account values(?,?,?,?,?,?,?)";
				try {
					if (jdbcTemplate.update(sql, uuid, prjId, cell1, cell2, cell3, cell4, cell5) > 0) {
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		return flag;
	}

	/**
	 * @throws Exception
	 * @自然人
	 */
	@Override
	public boolean prjNaturalPerson(String path, String prjId) throws Exception {
		boolean flag = false;
		Sheet hssfSheet = excelParsing(path, 8);
		String uuid = "";
		// hssfSheet.getLastRowNum()
		for (int i = 2; i < hssfSheet.getLastRowNum() + 1; i++) {
			Row row = hssfSheet.getRow(i);
			String cell1 = row.getCell(1) == null ? "" : row.getCell(1).toString();
			String cell2 = row.getCell(2) == null ? "" : row.getCell(2).toString();
			String cell3 = row.getCell(3) == null ? "" : row.getCell(3).toString();
			uuid = UUID.randomUUID().toString();
			String sql = "insert into ns_prj_natural_person values(?,?,?,?,?)";
			try {
				if (jdbcTemplate.update(sql, uuid, prjId, cell1, cell2, cell3) > 0) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean isNumeric(String str) {
		// Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
			return true;
		} catch (Exception e) {
			return false;// 异常 说明包含非数字。
		}
	}

	/**
	 * @针对一条数据信息的指标查询
	 * @param arr
	 * @param mapAll
	 * @return
	 */
	public Map<String, String> assemblyMap(List<Map<String, Object>> arr, Map<String, String> mapAll) {
		for (Map<String, Object> mapList : arr) {
			for (String key : mapList.keySet()) {
				if (mapList.get(key) != null && mapList.get(key).toString().length() > 2
						&& mapList.get(key).toString().substring(1, 2).equals(".")
						&& !isNumeric(mapList.get(key).toString())) {
					mapAll.put(key, mapList.get(key).toString().substring(0, 1));
				} else {
					mapAll.put(key, mapList.get(key) == null ? "" : mapList.get(key).toString());
				}
			}
		}
		return mapAll;
	}

	/**
	 * @针对多条不确定的数据信息的指标查询
	 * @param arr
	 * @param mapAll
	 * @return
	 */
	public Map<String, String> assemblyArrMap(String modelCode, String zlCode, List<Map<String, Object>> arr,
			Map<String, String> mapAll, String prjId, String bsOrder) {
		Map<String, String> yszkMap = new HashMap<String, String>();

		BigDecimal yszkJh = new BigDecimal("0");
		for (int i=0;i<arr.size();i++) {
			Map<String, Object>  mapList =arr.get(i);
			for (String key : mapList.keySet()) {
				if (mapList.get(key) != null && mapList.get(key).toString().length() > 2
						&& mapList.get(key).toString().substring(1, 2).equals(".")
						&& !isNumeric(mapList.get(key).toString())) {
					yszkMap.put(key, mapList.get(key).toString().substring(0, 1));
				} else {
					yszkMap.put(key, mapList.get(key) == null ? "0" : mapList.get(key).toString());
				}
			}
			if (modelCode.equals("ZXPJ04")) {
				yszkMap.put("P037", mapAll.get("P037"));
			}
			/*if (modelCode.equals("ZXPJ03")) {
				yszkMap.put("BS002_INIT", mapAll.get("BS002_INIT"));
			}*/


			if (modelCode.equals("ZXPJ01")) {
				yszkMap.put("CZ001_1", mapAll.get("P036"));
				yszkMap.put("CZ002", mapAll.get("CZ002"));
				yszkMap.put("CZ003", mapAll.get("P042"));
				yszkMap.put("P043", mapAll.get("P043"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date CZ005_2 = new Date();
				try {
					CZ005_2 = sdf.parse(yszkMap.get("CZ005_2"));
				} catch (ParseException e) {
					e.printStackTrace();
					yszkMap.put("CZ005", "0");
				}
				Date CZ005_1 = new Date();
				try {
					CZ005_1 = sdf.parse(yszkMap.get("CZ005_1"));
				} catch (ParseException e) {
					e.printStackTrace();
					yszkMap.put("CZ005", "0");
				}
				Double CZ005 = 0.0;
				try {
					try {
						Long l = 0L;
						long ts = CZ005_2.getTime();
						long ts1 = CZ005_1.getTime();
						l = (ts - ts1) / (1000 * 60 * 60 * 24);
						CZ005 = ((double) l / 365);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				yszkMap.put("CZ005", CZ005.toString());
			}
			String dbzk = modelResult(modelCode, yszkMap, prjId, bsOrder+"_"+(i+1)).get(modelCode + "_RESULT");
			
			yszkJh = yszkJh.add(new BigDecimal(dbzk));
		}
		mapAll.put(zlCode, yszkJh.toString());
		return mapAll;
	}

	@Override
	public Map<String,String> assemblyData(String prjId,DebtRating debt) {
		String sql = "select * from ns_prj_Basic_info info left join "
				+ " ns_prj_Operating_Flow a on info.fd_id=a.fd_prj_id " + " where info.fd_id=?";
		Map<String, String> mapAll = new HashMap<String, String>();
		List<Map<String, Object>> arr = jdbcTemplate.queryForList(sql, prjId);
		assemblyMap(arr, mapAll);

		// 短期现金流
		String dqxjl = modelResult("ZXPJ08", mapAll, prjId, "BS001").get("ZXPJ08_RESULT");
		mapAll.put("P002", dqxjl);
		// 风险敞口
		//String fxck = modelResult("ZXPJ07", mapAll, prjId, "BS002");
		Map<String,String> rsult =  modelResult("ZXPJ07", mapAll, prjId, "BS002");
		mapAll.put("P037", rsult.get("ZXPJ07_RESULT"));
		mapAll.put("P037_1", rsult.get("P037_1_RESULT"));
		// 应收账款--接入数据需遍历此处为数组
		// 应收账款为多笔此处需要循环计算加和
		String yszksql = "select * from ns_prj_Account where fd_prj_id = ?";
		List<Map<String, Object>> yszkMap = jdbcTemplate.queryForList(yszksql, prjId);
		assemblyArrMap("ZXPJ02", "ZX008", yszkMap, mapAll, prjId, "BS003");
		// 法人保障价值
		String frbzjzsql = "select * from NS_prj_legal_person where fd_prj_id = ?";
		List<Map<String, Object>> frbzMap = jdbcTemplate.queryForList(frbzjzsql, prjId);
		assemblyArrMap("ZXPJ04", "ZX006", frbzMap, mapAll, prjId, "BS004");
		// 不动产保障价值
		String bdcsql = "select * from ns_prj_real_estate where fd_prj_id = ?";
		List<Map<String, Object>> bdcMap = jdbcTemplate.queryForList(bdcsql, prjId);
		assemblyArrMap("ZXPJ05", "ZX009", bdcMap, mapAll, prjId, "BS005");
		//
		// 股权调整前保障价值
		String gqsql = "select * from ns_prj_Equity where fd_prj_id = ?";
		List<Map<String, Object>> gqMap = jdbcTemplate.queryForList(gqsql, prjId);
		assemblyArrMap("ZXPJ06", "ZX010", gqMap, mapAll, prjId, "BS006");
		// 自然人
		String zrrsql = "select * from ns_prj_natural_person where fd_prj_id = ?";
		List<Map<String, Object>> zrrMap = jdbcTemplate.queryForList(zrrsql, prjId);
		assemblyArrMap("ZXPJ09", "ZX007", zrrMap, mapAll, prjId, "BS007");

		// 筹资性现金流计算
		String czxxjlSql = "select to_date(CZ005_2,'yyyy/MM/dd') as CZ005_2,CZ001_2,to_date(to_char(sysdate,'yyyy/MM/dd'),'yyyy/MM/dd') as CZ005_1,CZ003,CZ004 from ns_prj_raise where fd_prj_id = ?";
		List<Map<String, Object>> czxxjlMap = jdbcTemplate.queryForList(czxxjlSql, prjId);
		mapAll.put("P041_0", dateCalculate(czxxjlMap,mapAll).toString());
		String czxdq = modelResult("ZXPJ11", mapAll, prjId, "BS011").get("ZXPJ11_RESULT");
		mapAll.put("CZ002", czxdq);
		assemblyArrMap("ZXPJ01", "CZX001", czxxjlMap, mapAll, prjId, "BS008");

		// 租赁物保障倍数
		String zlwSql = "select * from Ns_Lease_Item item left join NS_lease_qualitative lea on item.fd_prj_id = lea.fd_prj_id where  item.fd_prj_id = ?";
		List<Map<String, Object>> zlwMap = jdbcTemplate.queryForList(zlwSql, prjId);
		assemblyArrMap("ZXPJ03", "BS002", zlwMap, mapAll, prjId, "BS009");
		mapAll.put("P003", mapAll.get("P042"));
		Map<String,String> result = modelResult("ZXPJ", mapAll, prjId, "BS010");
		try {
			debt.setType("ZXPJ");
			debt.setInternName(SecurityUtil.getUserName());
			debt.setFinalName(SecurityUtil.getUserName());
			debt.setVaild(true);
			debt.setRatingStatus("000");
			debt.setFdVersion("1.0");
			debt.setFinalDate(new Date());
			debt.setProId(prjId);
			DebtRating debtEntity = debtRating.getRepository().save(debt);
			debtRating.startTrial("ZXPJ", debtEntity, result, "000");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	 * 汉德数据解析入库
	 */
	@Override
	@Transactional
	public Map<String, Object> interPa(String json) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "评级成功");
		result.put("flag", "true");
		Map<String, Object> map = (Map) com.alibaba.fastjson.JSONObject.parseObject(json);
		NsPrjProject prj = parseBean(json, NsPrjProject.class);
		nsPrj.getRepository().save(prj);
		
		Map<String, String> zb = new HashMap<String, String>();
		String uuid = UUID.randomUUID().toString();
		String sql = "";
		for (String key : map.keySet()) {
			try {
				List<Map<String, String>> score_target_datas = (List<Map<String, String>>) map.get(key);
				if (key.equals("ZXPJ05")) {
					for (int i = 0; i < score_target_datas.size(); i++) {
						Map<String, String> map2 = score_target_datas.get(i);
						sql = "INSERT INTO ns_prj_real_estate(FD_ID,FD_PRJ_ID,ZX009,P031,P030) VALUES(?,?,?,?,?)";
						jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, map2.get("P045_2"),
								map2.get("P031"), map2.get("P030"));
					}
				} else if (key.equals("ZXPJ02")) {
					for (int i = 0; i < score_target_datas.size(); i++) {
						Map<String, String> map2 = score_target_datas.get(i);
						sql = "INSERT INTO ns_prj_Account(FD_ID,FD_PRJ_ID,ZX008_VALUE,P026,P027,P028,P029) VALUES(?,?,?,?,?,?,?)";
						jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, map2.get("P045_1"),
								map2.get("P026"), map2.get("P027"), map2.get("P028"), map2.get("P029"));
					}
				} else if (key.equals("ZXPJ06")) {
					for (int i = 0; i < score_target_datas.size(); i++) {
						Map<String, String> map2 = score_target_datas.get(i);
						sql = "INSERT INTO ns_prj_Equity(FD_ID,FD_PRJ_ID,ZX010,P032) VALUES(?,?,?,?)";
						jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid,
								map2.get("P045_3") == null ? "" : map2.get("P045_3"),
								map2.get("P032") == null ? "" : map2.get("P032"));
					}
				} else if (key.equals("ZXPJ01")) {
					for (int i = 0; i < score_target_datas.size(); i++) {
						Map<String, String> map2 = score_target_datas.get(i);
						if(map2.get("CZ001_2")!=null) {
							sql = "INSERT INTO ns_prj_raise(FD_ID,FD_PRJ_ID,CZ004,CZ005_2,CZ001_2,CZ003) VALUES(?,?,?,?,?,?)";
							jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid,
									map2.get("CZ004") == null ? "" : map2.get("CZ004"),
									map2.get("CZ005") == null ? "" : map2.get("CZ005"),
									map2.get("CZ001_2") == null ? "" : map2.get("CZ001_2"),
									map2.get("CZ003") == null ? "" : map2.get("CZ003"));
						}
					}
				} else {
					for (int i = 0; i < score_target_datas.size(); i++) {
						Map<String, String> map2 = score_target_datas.get(i);
						zb.put(map2.get("code"), map2.get("value"));
					}
				}

			} catch (Exception e) {
				System.out.println(key + "不是数组类型！");
			}
		}
		String zbLevel = "";
		sql = "select count(1) from ns_main_rating where fd_cust_code = ? and FD_RATING_VAILD='1'";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, map.get("fdCustCode"));

		if (count > 0) {
			try {
				sql = "select FD_FINAL_LEVEL from ns_main_rating where fd_cust_code = ? and FD_RATING_VAILD='1'";
				zbLevel = jdbcTemplate.queryForObject(sql, String.class, map.get("fdCustCode"));
			} catch (Exception e) {
				zbLevel = "";
			}
			
		}
		if (!org.apache.commons.lang3.StringUtils.isNotBlank(zbLevel)) {
			try {
				sql = "select FD_INTERN_LEVEL from ns_main_rating where fd_cust_code = ? and FD_RATING_VAILD='1'";
				zbLevel = jdbcTemplate.queryForObject(sql, String.class, map.get("fdCustCode"));
			} catch (Exception e) {
				zbLevel = "";
			}
		}
		if (!org.apache.commons.lang3.StringUtils.isNotBlank(zbLevel)) {
			result.put("msg", "评级失败，该客户没有主体评级！");
			result.put("flag", "false");
			return result;
		}
		

		sql = "insert into ns_prj_Basic_info values(?,?,?,?,?,?,?,?,?,?)";
		// 插入ns_prj_Basic_info评级主表中
		jdbcTemplate.update(sql, uuid, zb.get("projectNumber"), zb.get("P039"), zb.get("P042"), zb.get("P038"),
				zb.get("P043"), zb.get("P034"), zb.get("P035"), zbLevel, zb.get("P041_1"));

		// 经营性现金流
		sql = "INSERT INTO ns_prj_Operating_Flow (FD_ID,FD_PRJ_ID,P041_1,P041_2,P041_3,P001) VALUES(?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, zb.get("P041_1"), zb.get("P041_2"),
				zb.get("P041_3"), zb.get("P001"));
		// 法人保证倍数
		sql = "INSERT INTO NS_prj_legal_person(FD_ID,FD_PRJ_ID,P016,P017,P018,ZX006,P019,P020,P021,P022,P023) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, zb.get("P016"), zb.get("P017"), zb.get("P018"),
				zb.get("P044"), zb.get("P019"), zb.get("P020"), zb.get("P021"), zb.get("P022"), zb.get("P023"));
		// 自然人
		sql = "INSERT INTO ns_prj_natural_person(FD_ID,FD_PRJ_ID,P024,P025_1,P025_2) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, zb.get("P024"), zb.get("P025_1"),
				zb.get("P025_2"));

		// 租赁物
		sql = "INSERT INTO NS_lease_qualitative(FD_ID,FD_PRJ_ID,P007,P008,P009,P010,P011,P012,P013,P014,P015) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, zb.get("P007"), zb.get("P008"), zb.get("P009"),
				zb.get("P010"), zb.get("P011"), zb.get("P012"), zb.get("P013"), zb.get("P014"), zb.get("P015"));
		// 调整型现值
		sql = "INSERT INTO ns_zl_inter(FD_ID,FD_PRJ_ID,BS002_TZXZ) VALUES(?,?,?)";
		jdbcTemplate.update(sql, UUID.randomUUID().toString(), uuid, zb.get("BS002_TZXZ"));
		map.put("uuid", uuid);

		return map;

	}

	/**
	 * 数据解析传入模型
	 */
	@Override
	@Transactional
	public Map<String, String> assemblyDataInter(Map<String, Object> result2) {
		String indexID = result2.get("uuid").toString();
		//String debtID = result2.get("uuid").toString();
		Map<String, String> result = new HashMap<String, String>();
		String sql = "select * from ns_prj_Basic_info info left join "
				+ " ns_prj_Operating_Flow a on info.fd_id=a.fd_prj_id " + " where info.fd_id=?";
		Map<String, String> mapAll = new HashMap<String, String>();
		List<Map<String, Object>> arr = jdbcTemplate.queryForList(sql, indexID);
		assemblyMap(arr, mapAll);
		// 短期现金流
		String dqxjl = modelResult("ZXPJ08", mapAll, indexID, "BS001").get("ZXPJ08_RESULT");
		mapAll.put("CZ002", dqxjl);
		mapAll.put("P002", dqxjl);
		// 风险敞口
		//String fxck = modelResult("ZXPJ07", mapAll, indexID, "BS002").get("ZXPJ07_RESULT");
		Map<String,String> rsult =  modelResult("ZXPJ07", mapAll, indexID, "BS002");
		mapAll.put("P037", rsult.get("ZXPJ07_RESULT"));
		mapAll.put("P037_1", rsult.get("P037_1_RESULT"));
		// 应收账款--接入数据需遍历此处为数组
		// 应收账款为多笔此处需要循环计算加和
		
		try {
			String yszksql = "select * from ns_prj_Account where fd_prj_id = ?";
			List<Map<String, Object>> yszkMap = jdbcTemplate.queryForList(yszksql, indexID);
			assemblyArrMap("ZXPJ02", "ZX008", yszkMap, mapAll, indexID, "BS003");
		} catch (Exception e) {
			result.put("data", "应收账款计算失败，请检查数据正确性！");
			result.put("flag", "false");
			e.printStackTrace();
			return result;
		}
		
	
		// 法人保障价值
		String frbzjzsql = "select * from NS_prj_legal_person where fd_prj_id = ?";
		List<Map<String, Object>> frbzMap = jdbcTemplate.queryForList(frbzjzsql, indexID);
		assemblyArrMap("ZXPJ04", "ZX006", frbzMap, mapAll, indexID, "BS004");
		// 不动产保障价值
		String bdcsql = "select * from ns_prj_real_estate where fd_prj_id = ?";
		List<Map<String, Object>> bdcMap = jdbcTemplate.queryForList(bdcsql, indexID);
		assemblyArrMap("ZXPJ05", "ZX009", bdcMap, mapAll, indexID, "BS005");
		//
		// 股权调整前保障价值
		String gqsql = "select * from ns_prj_Equity where fd_prj_id = ?";
		List<Map<String, Object>> gqMap = jdbcTemplate.queryForList(gqsql, indexID);
		assemblyArrMap("ZXPJ06", "ZX010", gqMap, mapAll, indexID, "BS006");
		// 自然人
		String zrrsql = "select * from ns_prj_natural_person where fd_prj_id = ?";
		List<Map<String, Object>> zrrMap = jdbcTemplate.queryForList(zrrsql, indexID);
		assemblyArrMap("ZXPJ09", "ZX007", zrrMap, mapAll, indexID, "BS007");

		// 筹资性现金流计算
		String czxxjlSql = "select to_date(CZ005_2,'yyyy/MM/dd') as CZ005_2,CZ001_2,to_date(to_char(sysdate,'yyyy/MM/dd'),'yyyy/MM/dd') as CZ005_1,CZ003,CZ004 from ns_prj_raise where fd_prj_id = ?";
		List<Map<String, Object>> czxxjlMap = jdbcTemplate.queryForList(czxxjlSql, indexID);
		mapAll.put("P041_0", dateCalculate(czxxjlMap,mapAll).toString());
		String czxdq = modelResult("ZXPJ11", mapAll, indexID, "BS001").get("ZXPJ11_RESULT");
		mapAll.put("CZ002", czxdq);
		assemblyArrMap("ZXPJ01", "CZX001", czxxjlMap, mapAll, indexID, "BS008");

		// 租赁物保障倍数BS002_TZXZ
		String zlwSql = "select * from ns_zl_inter item left join NS_lease_qualitative lea on item.fd_prj_id = lea.fd_prj_id where  item.fd_prj_id = ?";
		List<Map<String, Object>> zlwMap = jdbcTemplate.queryForList(zlwSql, indexID);
		assemblyArrMap("ZXPJ10", "BS002", zlwMap, mapAll, indexID, "BS009");
		mapAll.put("P003", mapAll.get("P042"));
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			resultMap=modelResult("ZXPJ", mapAll, indexID, "BS010");
			if(resultMap.size()==0) {
				result.put("data", "计算失败，请检查数据正确性！");
				result.put("flag", "false");
				return result;
				
			}
		} catch (Exception e) {
			result.put("data", "计算失败，请检查数据正确性！");
			result.put("flag", "false");
			e.printStackTrace();
			return result;
		}
		DebtRating debt = new DebtRating();
		// debtRating = debt.startTrial("ZXPJ", debtId, jsons, "000");
		if(result2.get("fdRatingStatus").toString().equals("020")) {
			String id="";
			try {
				id=jdbcTemplate.queryForObject("select FD_ID from ns_debt_rating where FD_PROJECT_CODE=? and FD_RATING_VAILD ='1' --and fd_rating_status='010'",String.class,result2.get("fdProjectCode"));
			if(id.equals("")) {
				result.put("data", "计算失败，该项目无初评数据无法复评");
				result.put("flag", "false");
				return result;
			}
			} catch (Exception e) {
				result.put("data", "计算失败，该项目无初评数据无法复评");
				result.put("flag", "false");
				e.printStackTrace();
				return result;
			}
		debt=debtRating.getRepository().findOne(id);
		}
		try {
			debt.setCustCode(result2.get("fdCustCode") == null ? "" : result2.get("fdCustCode").toString());
			debt.setCustName(result2.get("fdCustName") == null ? "" : result2.get("fdCustName").toString());
			debt.setProjectCode(result2.get("fdProjectCode") == null ? "" : result2.get("fdProjectCode").toString());
			debt.setProjectName(result2.get("projectName") == null ? "" : result2.get("projectName").toString());
			debt.setType("ZXPJ");
			if (result2.get("fdRatingStatus").toString().equals("010")) {
				debt.setInternName(result2.get("fdInternName") == null ? "" : result2.get("fdInternName").toString());
				debt.setInternCode(result2.get("fdInternCode") == null ? "" : result2.get("fdInternCode").toString());
				debt.setInternDate(new Date());
			} else if (result2.get("fdRatingStatus").toString().equals("020")){//assistingPmA
				debt.setFinalName(result2.get("fdFinalName") == null ? "" : result2.get("fdFinalName").toString());
				debt.setFinalDate(new Date());
				debt.setFinalCode(result2.get("fdFinalCode") == null ? "" : result2.get("fdFinalCode").toString());
				debt.setFinalAdvice(
						result2.get("fdFinalAdvice") == null ? "" : result2.get("fdFinalAdvice").toString());
				debt.setAssetReview(result2.get("riskManagerName")== null ? "" : result2.get("riskManagerName").toString());
				
			}
			
			//riskManagerName
			
			debt.setVaild(true);
			debt.setRatingStatus(result2.get("fdRatingStatus").toString());
			debt.setFdVersion("1.0");
			debt.setFinalDate(new Date());
			debt.setProId(indexID);
			DebtRating debtEntity = debtRating.getRepository().save(debt);
			debtRating.startTrial("ZXPJ", debtEntity, resultMap, result2.get("fdRatingStatus").toString());
			result.put("REPORTURL", "/irs/DebtRatingResults/enterReport?projectNo="+debtEntity.getId());
			if (result2.get("fdRatingStatus").toString().equals("010")) {
				result.put("GRADE_LEVEL", debtEntity.getInternLevel());
			} else {
				result.put("GRADE_LEVEL", debtEntity.getFinalLevel());
			}
			result.put("CLIENT_NAME", debtEntity.getCustName());
			result.put("CLIENT_NO", debtEntity.getCustCode());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		String zxpj = resultMap.get("ZXPJ_RESULT");
		result.put("data", zxpj);
		result.put("flag", "true");
		return result;
	}

	/**
	 * @计算两个时间相差天数，以年为单位。
	 * @param czxxjlMap
	 * @return
	 */
	public Double dateCalculate(List<Map<String, Object>> czxxjlMap, Map<String, String> mapAll) {
		Double yearXjl = 0.0;
		for (Map<String, Object> map : czxxjlMap) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date CZ005_2 = new Date();
			try {
				CZ005_2 = sdf.parse(map.get("CZ005_2").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date CZ005_1 = new Date();
			try {
				CZ005_1 = sdf.parse(map.get("CZ005_1").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Double CZ005 = 0.0;
			try {
				Long l = 0L;
				long ts = CZ005_2.getTime();
				long ts1 = CZ005_1.getTime();
				l = (ts - ts1) / (1000 * 60 * 60 * 24);
				CZ005 = ((double) l / 365);
				if (CZ005 < 1) {
					mapAll.put("P041_99",CZ005.toString());
					mapAll.put("CZ005",CZ005.toString());
					yearXjl = yearXjl + Double.parseDouble(map.get("CZ004").toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return yearXjl;
	}


	/**
	 * @param modelCode
	 * @param zlw
	 * @param sql
	 * @风险敞口
	 * @return
	 */
	public Map<String, String> modelResult(String modelCode, Map<String, String> fxck, String prjId, String bsOrder) {
		Map<String, String> result = debtRating.ratingResults(modelCode, fxck);
		try {
			Model model = debtRating.getModel(modelCode);
			initRatingIndex(model.getSubModels(), fxck, result, prjId, bsOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void initRatingIndex(List<Model> models, Map<String, String> map, Map<String, String> paramValue,
			String prjId, String bsOrder) throws Exception {
		RatingDebtIndex index;
		if (!CollectionUtils.isEmpty(models)) {
			// 模型下的定性和定量
			for (Model m1 : models) {
				// 定量或者定性下的指标大类
				index = this.saveRatingIndex(m1, map, paramValue, prjId, bsOrder);
				for (Model m2 : m1.getSubModels()) {
					index = this.saveRatingIndex(m2, map, paramValue, prjId, bsOrder);
					// 大类下的每个指标
					for (Model m3 : m2.getSubModels()) {
						this.saveRatingIndex(m3, map, paramValue, prjId, bsOrder);
					}
				}
			}
		}
	}

	private RatingDebtIndex saveRatingIndex(Model model, Map<String, String> map, Map<String, String> paramValue,
			String prjId, String bsOrder) throws Exception {
		RatingDebtIndex index = new RatingDebtIndex();
		// 指标ID
		index.setIndexId(model.getId());
		index.setIndexType(prjId);
		// 指标名称
		index.setLevel(bsOrder);

		index.setIndexName(model.getName());
		// 评级步骤
		// 指标类型
		// 指标分类

		for (int i = 0; i < model.getParameters().size(); i++) {
			Parameter parameter = model.getParameters().get(i);
			if (parameter.getCode().contains("RESULT") && !parameter.getCode().contains("TEMP")) {
				if (org.apache.commons.lang3.StringUtils.isNotBlank(paramValue.get(parameter.getCode()))) {
					BigDecimal bigdecimal = new BigDecimal(paramValue.get(parameter.getCode()));
					index.setIndexScore(bigdecimal);
				}

			}

			if (!StringUtils.isEmpty(parameter.getName()) && (parameter.getType().equals(ParameterType.IN)
					|| parameter.getType().equals(ParameterType.IN_OPTION))) {
				// 指标code
				index.setIndexCode(parameter.getCode());

				index.setIndexValue(map.get(parameter.getCode()));
			} else {
				index.setIndexCode(parameter.getCode().replace("_RESULT", ""));
			}
			
			// 中间值
			if (!StringUtils.isEmpty(parameter.getCode()) && parameter.getCode().contains("_TEMP_RESULT")) {
				index.setIndexWeight(paramValue.get(parameter.getCode()));
			}
			
			// 权重
			if (!StringUtils.isEmpty(parameter.getCode()) && parameter.getCode().contains("WEIGHT")) {
				index.setIndexWeight(map.get(parameter.getCode().replace("_WEIGHT", "")));
			}
			if (!StringUtils.isEmpty(parameter.getCode()) && parameter.getType().toString().equals("IN_OPTION")) {
				StringBuffer dataStr = new StringBuffer("{");
				for (ParameterOption po : parameter.getOptions()) {
					if (map.get(parameter.getCode()).equals(po.getInputValue())) {
						index.setQuOptions(po.getInputValue() + "." + po.getTitle());
					}
					dataStr.append("\"" + po.getInputValue() + "\"");
					dataStr.append(":");
					dataStr.append("\"" + po.getTitle() + "\"");
					dataStr.append(",");
				}
				String data = dataStr.toString();
				data = data.substring(0, data.length() - 1) + "}";
				// 定性文本
				index.setDxText(data.replace("\n", ""));
			}
		}
		ratingIndexService.add(index);
		return index;
	}
}
