package gbicc.irs.main.rating.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.jdbc.util.SqlBatcher;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.customer.entity.Customer;
import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.RatingMainStepRepository;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.GenerateModel;
import gbicc.irs.main.rating.vo.ScoreDetail;


/**
 * @主体评级相关操作服务类
 * @author wsh
 *
 */
@Service
public class RatingMainStepServiceImpl extends 
DaoServiceImpl<RatingMainStep, String, RatingMainStepRepository> 
implements RatingMainStepService {
	
	@Autowired
	private RatingIndexService ratingIndexService;
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MainRatingServiceImpl mainRatingServiceImpl;
	@Autowired
	private RatingMainStepService ratingMainStepService;
	/**
	 * @下一步
	 */
	@Override
	public MainRating nextStep(String stepId) throws Exception {
		RatingMainStep step = repository.getOne(stepId);
		MainRating rating = (MainRating) step.getRatingMain();
		RatingMainStep nextStep = step.getNextStep();
	/*	if(nextStep != null){
			// 定量
			if(nextStep.getStepType() != null && nextStep.getStepType() == RatingStepType.QUANTITATIVE){ //定量分析
				rating = this.updateDlRatingIndex(nextStep);
			}
			// 定性
			if(nextStep.getStepType() != null && nextStep.getStepType() == RatingStepType.QUALITATIVE_SHOW){
				rating = this.updateDxRatingIndex(step);
			}
		}*/
		rating.setCurrentStep(nextStep);
		rating.setCurrentStepId(nextStep.getId());
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		//查询评级建议等级
		//mainRatingServiceImpl.update(rating.getId(),rating);
		return rating;
	}
	
	/**
	 * @上一步
	 */
	@Override
	public MainRating lastStep(String stepId) throws Exception {
		RatingMainStep setp = repository.getOne(stepId);
		MainRating rating = (MainRating) setp.getRatingMain();
		RatingMainStep lastSetp = setp.getLastStep();
		/*if(lastSetp.getStepType() != null && (lastSetp.getStepType() == RatingStepType.ADDITIONAL || lastSetp.getStepType() == RatingStepType.CUST_INFO)) {//补录步骤
			this.packagingAdditionalStep(lastSetp);
		}*/
		rating.setCurrentStep(lastSetp);
		rating.setCurrentStepId(lastSetp.getId());
		//查询评级建议等级
		//rating.setSuggestLevel(ratingOverturnService.findOverturnMapByRatingId(rating.getId()));
		//mainRatingServiceImpl.update(rating.getId(),rating)
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		return rating;
	}

	@Override
	public MainRating checkQualitative(String stepId, Map<String, String> paramValue) throws Exception {
		// 更新定性指标
		//this.updateQualitative(stepId, paramValue);
		RatingMainStep step = repository.getOne(stepId);
		MainRating rating = (MainRating) step.getRatingMain();
		if(step != null){
			//rating = this.updateDxRatingIndex(step);
		}
		rating.setCurrentStep(step);
		rating.setCurrentStepId(step.getId());
		//return mainRatingServiceImpl.update(rating.getId(),rating);
		rating.setSteps(this.getAdditionalStepByRatingId(rating.getId()));
		return rating;
	}
	/**
	 * @更新指标
	 * @param stepId
	 * @param paramValue
	 */
	public void updateQualitative(String stepId, Map<String, String> paramValue) {
		if(StringUtils.hasText(stepId) && stepId!=null && paramValue.size() > 0){
			SqlBatcher sqlBatcher =new SqlBatcher("UPDATE NS_RATING_INDEXES I SET I.FD_INDEXVALUE=? WHERE I.FD_INDEXCODE=? AND I.FD_STEPID=?");
			Iterator<String> iter = paramValue.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = paramValue.get(key);
				if(StringUtils.hasText(value)) {
					sqlBatcher.addArg(new Object[]{value,key,stepId});
				}
			}
			sqlBatcher.execute(jdbcTemplate);
		}
	}
	@Override
	public MainRating saveQualitativeAndNextStep(String stepId, Map<String, String> paramValue) throws Exception {
		// 更新定性指标
		this.updateQualitative(stepId, paramValue);
		return this.nextStep(stepId);
	}

	@Override
	public List<RatingMainStep> getAdditionalStepByRatingId(String ratingId) throws Exception{
		List<RatingMainStep> step=repository.findByRatingMain_id(ratingId);
		List<RatingMainStep> stepResult = new ArrayList<RatingMainStep>();
		for (RatingMainStep ratingMainStep : step) {
			if(ratingMainStep.getVaild().equals("1")) {
				stepResult.add(ratingMainStep);
			}
		}
		return stepResult;
	}
	/**
	 * @客户基础信息
	 * @Custno 
	 */
	@Override
	public CustRatingInfo custInfo(String Custno,String type) {
		Integer num =jdbcTemplate.queryForObject("select count(*) from ns_main_rating ra where  ra.FD_ID = ? and ra.fd_final_level is not null", Integer.class,Custno);
		String inner ="";
		if(num>0) {
			inner=" inner join ns_cfg_main_scale scale\r\n" + 
					"    on scale.FD_SCALE_LEVEL = rating.FD_FINAL_LEVEL and scale.fd_version=rating.fd_version "; 
		}else {
			inner=" inner join ns_cfg_main_scale scale\r\n" + 
					"    on scale.FD_SCALE_LEVEL = rating.fd_intern_level and scale.fd_version=rating.fd_version  "; 
		}
		String sql ="";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
			 sql ="select rating.fd_cust_code        as fdcustcode,\r\n" + 
					"       rating.fd_cust_name        as custName,\r\n" + 
					"       ''      as finalLevel,\r\n" + 
					"       scale.FD_ADMISSION_SUGGEST as admissionSuggest,\r\n" + 
					"		mas.FD_PARK_ADDRESS as park,"+
					"       rating.FD_INTERN_NAME      as internName,\r\n" + 
					"       rating.FD_INTERN_LEVEL     as internLevel,\r\n" + 
					"       ''       as finalName,\r\n" + 
					"       rating.FD_SCO       as sco,\r\n" + 
					"       abs(case when  rating.FD_TYPE='UNIVERSAL_S_SODEL2' or  rating.FD_TYPE='UNIVERSAL_S_SODEL' then (FD_QUAN_SCO/40)- (FD_QUAL_SCO/60) \r\n" + 
					"       when rating.FD_TYPE='NEW_BUILD' then (FD_QUAN_SCO/30)- (FD_QUAL_SCO/70) end) as bl,"+
					"  rating.FD_PD as pd,\r\n" + 
					"   (case when (select count(1)+1 from ns_main_rating where FD_SCO> rating.FD_SCO and fd_vaild='1')=0 then 100 else (select count(1)+1 from ns_main_rating where FD_SCO> rating.FD_SCO and fd_vaild='1')/(select count(1)+1 from ns_main_rating where fd_vaild='1') end) as ranking,"+
					"		rating.FD_FIR_REP||'年报'          as firRep,"+
					"		rating.Fd_Rating_Type as ratingType,"+
					"       rating.FD_TYPE             as type,\r\n" + 
					"        to_char(rating.FD_FINAL_DATE,'yyyy-MM-dd')      as internDate,\r\n" + 
					"      ''     as finalAdvice\r\n" + 
					"  from \r\n" + 
					" ns_main_rating rating\r\n" +
					"inner join ns_bp_master mas on mas.FD_BP_CODE = rating.FD_CUST_CODE "+inner+
					" where rating.FD_ID = ? ";
			
		}else {
			 sql ="select rating.fd_cust_code        as fdcustcode,\r\n" + 
					"       rating.fd_cust_name        as custName,\r\n" + 
					"       rating.FD_FINAL_LEVEL      as finalLevel,\r\n" + 
					"       scale.FD_ADMISSION_SUGGEST as admissionSuggest,\r\n" + 
					"		mas.FD_PARK_ADDRESS as park,"+
					"       rating.FD_INTERN_NAME      as internName,\r\n" + 
					"       rating.FD_INTERN_LEVEL     as internLevel,\r\n" + 
					"       rating.FD_FINAL_NAME       as finalName,\r\n" + 
					"       rating.FD_SCO       as sco,\r\n" + 
					"       abs(case when  rating.FD_TYPE='UNIVERSAL_S_SODEL2' or  rating.FD_TYPE='UNIVERSAL_S_SODEL' then (FD_QUAN_SCO/40)- (FD_QUAL_SCO/60) \r\n" + 
					"       when rating.FD_TYPE='NEW_BUILD' then (FD_QUAN_SCO/30)- (FD_QUAL_SCO/70) end) as bl,"+
					"  rating.FD_PD as pd,\r\n" + 
					"  ((select count(1)+1 from ns_main_rating where FD_SCO> rating.FD_SCO)/(select count(1)+1 from ns_main_rating)) as ranking,"+
					"		rating.FD_FIR_REP||'年报'          as firRep,"+
					"		rating.Fd_Rating_Type as ratingType,"+
					"       rating.FD_TYPE             as type,\r\n" + 
					"        to_char(rating.FD_FINAL_DATE,'yyyy-MM-dd')      as internDate,\r\n" + 
					"       rating.FD_FINAL_ADVICE     as finalAdvice\r\n" + 
					"  from \r\n" + 
					" ns_main_rating rating\r\n" +
					"inner join ns_bp_master mas on mas.FD_BP_CODE = rating.FD_CUST_CODE "+inner+
					" where rating.FD_ID = ? ";
		}
		
		
		/*" and scale.fd_TYPE=(\r\n" + 
		"  select case when FD_TYPE='UNIVERSAL_S_SODEL2' \r\n" + 
		" or \r\n" + 
		" FD_TYPE='UNIVERSAL_S_SODEL' then '010' when FD_TYPE='NEW_BUILD' then '020' end  from \r\n" + 
		" ns_main_rating where  fd_id = ?\r\n" + 
		")
*/		List<CustRatingInfo> custInfo=jdbcTemplate.query(sql, new BeanPropertyRowMapper<CustRatingInfo>(CustRatingInfo.class),Custno);
		if(custInfo.size()>0) {
			custInfo.get(0).setGenerate(scoreDivergence(Custno,type));
			custInfo.get(0).setScoreDetail(scoreDetail(Custno,type));
			return custInfo.get(0);
		}else {
			return null;
		}
		
		
	}
	
	/**
	 * @该客户评级生效的评级数据的大类指标
	 * @Custno 
	 */
	public List<GenerateModel> scoreDivergence(String Custno,String type) {
		String sql ="";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
			sql ="select a.*,\r\n" + 
					"       (select avg(score/value)\r\n" + 
					"          from (\r\n" + 
					"                select  \r\n" + 
					"                        indexe.fd_indexcode as code,\r\n" + 
					"                        indexe.fd_score as score,\r\n" + 
					"                        stan.fd_value as value,\r\n"+
					"                        bp.FD_SEGMENT_INDUSTRY,\r\n" +
					"				rating.fd_type "+
					"                  from ns_rating_indexes indexe\r\n" + 
					"                 inner join ns_rating_step step\r\n" + 
					"                    on indexe.fd_stepid = step.fd_id\r\n" + 
					"                 inner join ns_main_rating rating\r\n" + 
					"                    on step.fd_rateid = rating.fd_id\r\n" + 
					"                 inner join NS_BP_MASTER bp\r\n" + 
					"                    on bp.FD_BP_CODE = rating.FD_CUST_CODE\r\n" + 
					"                 inner join NS_STANDARD_SCORE stan\r\n" + 
					"                    on stan.fd_vaild = rating.fd_version and stan.fd_code = indexe.fd_indexcode\r\n" + 
					"                 where indexe.fd_level = '2'\r\n" + 
					"                   and indexe.fd_indexcode not like 'YRZB%'\r\n" + 
					"                   and step.fd_vaild = '2'\r\n" + 
					"                   and rating.fd_vaild = '1'\r\n" + 
					"                   and rating.fd_rating_status='020'\r\n"+
					"                \r\n" + 
					"                ) s\r\n" + 
					"         where s.code = a.code\r\n" + 
					"           and s.fd_type = a.fd_type) as scoreAvg\r\n" + 
					"  from (select (case indexe.fd_indextype\r\n" + 
					"                 when 'QUANTITATIVE' then\r\n" + 
					"                  '定量指标'\r\n" + 
					"                 else\r\n" + 
					"                  '定性指标'\r\n" + 
					"               end) as indextype,\r\n" + 
					"               indexe.fd_indexcode as code,\r\n" + 
					"               indexe.fd_indexname as name,\r\n" + 
					" indexe.FD_ID as id,"+
					"               indexe.fd_score as score,\r\n" + 
					"               stan.FD_VALUE as standardScore,\r\n" + 
					"               bp.FD_SEGMENT_INDUSTRY,\r\n" + 
					"				rating.fd_type "+
					"          from ns_rating_indexes indexe\r\n" + 
					"         inner join ns_rating_step step\r\n" + 
					"            on indexe.fd_stepid = step.fd_id\r\n" + 
					"         inner join ns_main_rating rating\r\n" + 
					"            on step.fd_rateid = rating.fd_id\r\n" + 
					"         left join ns_main_level scale\r\n" + 
					"            on scale.FD_CODE = rating.FD_FINAL_LEVEL and rating.fd_version=scale.fd_rating_vaild and decode(rating.fd_type,'UNIVERSAL_S_SODEL','010','NEW_BUILD','020','UNIVERSAL_S_SODEL2','010')=scale.fd_type " + 
					"         inner join NS_BP_MASTER bp\r\n" + 
					"            on bp.FD_BP_CODE = rating.FD_CUST_CODE\r\n" + 
					"         left join  NS_STANDARD_SCORE stan   \r\n" + 
					"            on stan.FD_code =  indexe.fd_indexcode and stan.fd_vaild = rating.fd_version\r\n" + 
					"         where rating.fd_ID = ?\r\n" + 
					"           and indexe.fd_level = '2'\r\n" + 
					"           and indexe.fd_indexcode not like 'YRZB%'\r\n" + 
					"           and step.fd_vaild='2'\r\n" + 
					"           ) a order by indextype,\r\n" + 
					"           decode(code,'TYZB003',1),\r\n" + 
					"           decode(code,'TYZB004',2),\r\n" + 
					"           decode(code,'TYZB005',3),\r\n" + 
					"			decode(code, 'XJZT003', 3),\r\n" + 
					"          decode(code, 'XJZT002', 1),\r\n" + 
					"          decode(code, 'XJZT001', 2),\r\n" + 
					"          decode(code, 'XJZT004', 4),\r\n" + 
					"          decode(code, 'XJZT006', 5),\r\n" + 
					"          decode(code, 'XJZT007', 6),\r\n" + 
					"          decode(code, 'XJZT008', 7),\r\n" + 
					"          decode(code, 'XJZT009', 8),\r\n" + 
					"          decode(code, 'XJZT0010', 9),\r\n" + 
					"          decode(code, 'XJZT005', 10)," + 
					"           decode(code,'TYZB006',4),\r\n" + 
					"           decode(code,'TYZB002',5),\r\n" + 
					"           decode(code,'TYZB001',6),\r\n" + 
					"           decode(code,'TYZB007',7),\r\n" + 
					"           decode(code,'TYZB009',8),\r\n" + 
					"           decode(code,'TYZB010',9),\r\n" + 
					"           decode(code,'TYZB011',10),\r\n" + 
					"           decode(code,'TYZB012',11),\r\n" + 
					"           decode(code,'TYZB013',12),\r\n" + 
					"           decode(code,'TYZB008',13)";
		}else {
			sql ="select a.*,\r\n" + 
					"       (select avg(score/value)\r\n" + 
					"          from (\r\n" + 
					"                select  \r\n" + 
					"                        indexe.fd_indexcode as code,\r\n" + 
					"                        indexe.fd_score as score,\r\n" + 
					"                        stan.fd_value as value,\r\n"+
					"                        bp.FD_SEGMENT_INDUSTRY,\r\n" +
					"				rating.fd_type "+
					"                  from ns_rating_indexes indexe\r\n" + 
					"                 inner join ns_rating_step step\r\n" + 
					"                    on indexe.fd_stepid = step.fd_id\r\n" + 
					"                 inner join ns_main_rating rating\r\n" + 
					"                    on step.fd_rateid = rating.fd_id\r\n" + 
					"                 inner join NS_BP_MASTER bp\r\n" + 
					"                    on bp.FD_BP_CODE = rating.FD_CUST_CODE\r\n" + 
					"                 inner join NS_STANDARD_SCORE stan\r\n" + 
					"                    on stan.fd_vaild = rating.fd_version and stan.fd_code = indexe.fd_indexcode\r\n" + 
					"                 where indexe.fd_level = '2'\r\n" + 
					"                   and indexe.fd_indexcode not like 'YRZB%'\r\n" + 
					"                   and step.fd_vaild = '1'\r\n" + 
					"                   and rating.fd_vaild = '1'\r\n" + 
					"                   and rating.fd_rating_status='020'\r\n"+
					"                \r\n" + 
					"                ) s\r\n" + 
					"         where s.code = a.code\r\n" + 
					"           and s.fd_type = a.fd_type) as scoreAvg\r\n" + 
					"  from (select (case indexe.fd_indextype\r\n" + 
					"                 when 'QUANTITATIVE' then\r\n" + 
					"                  '定量指标'\r\n" + 
					"                 else\r\n" + 
					"                  '定性指标'\r\n" + 
					"               end) as indextype,\r\n" + 
					"               indexe.fd_indexcode as code,\r\n" + 
					"               indexe.fd_indexname as name,\r\n" + 
					" indexe.FD_ID as id,"+
					"               indexe.fd_score as score,\r\n" + 
					"               stan.FD_VALUE as standardScore,\r\n" + 
					"               bp.FD_SEGMENT_INDUSTRY,\r\n" + 
					"				rating.fd_type "+
					"          from ns_rating_indexes indexe\r\n" + 
					"         inner join ns_rating_step step\r\n" + 
					"            on indexe.fd_stepid = step.fd_id\r\n" + 
					"         inner join ns_main_rating rating\r\n" + 
					"            on step.fd_rateid = rating.fd_id\r\n" + 
					"         left join ns_main_level scale\r\n" + 
					"            on scale.FD_CODE = rating.FD_FINAL_LEVEL  and rating.fd_version=scale.fd_rating_vaild and decode(rating.fd_type,'UNIVERSAL_S_SODEL','010','NEW_BUILD','020','UNIVERSAL_S_SODEL2','010')=scale.fd_type " + 
					"         inner join NS_BP_MASTER bp\r\n" + 
					"            on bp.FD_BP_CODE = rating.FD_CUST_CODE\r\n" + 
					"         left join  NS_STANDARD_SCORE stan   \r\n" + 
					"            on stan.FD_code =  indexe.fd_indexcode and stan.fd_vaild = rating.fd_version\r\n" + 
					"         where rating.fd_ID = ?\r\n" + 
					"           and indexe.fd_level = '2'\r\n" + 
					"           and indexe.fd_indexcode not like 'YRZB%'\r\n" + 
					"           and step.fd_vaild='1'\r\n" + 
					"           ) a order by indextype,\r\n" + 
					"           decode(code,'TYZB003',1),\r\n" + 
					"           decode(code,'TYZB004',2),\r\n" + 
					"           decode(code,'TYZB005',3),\r\n" + 
					"			decode(code, 'XJZT003', 3),\r\n" + 
					"          decode(code, 'XJZT002', 1),\r\n" + 
					"          decode(code, 'XJZT001', 2),\r\n" + 
					"          decode(code, 'XJZT004', 4),\r\n" + 
					"          decode(code, 'XJZT006', 5),\r\n" + 
					"          decode(code, 'XJZT007', 6),\r\n" + 
					"          decode(code, 'XJZT008', 7),\r\n" + 
					"          decode(code, 'XJZT009', 8),\r\n" + 
					"          decode(code, 'XJZT0010', 9),\r\n" + 
					"          decode(code, 'XJZT005', 10)," + 
					"           decode(code,'TYZB006',4),\r\n" + 
					"           decode(code,'TYZB002',5),\r\n" + 
					"           decode(code,'TYZB001',6),\r\n" + 
					"           decode(code,'TYZB007',7),\r\n" + 
					"           decode(code,'TYZB009',8),\r\n" + 
					"           decode(code,'TYZB010',9),\r\n" + 
					"           decode(code,'TYZB011',10),\r\n" + 
					"           decode(code,'TYZB012',11),\r\n" + 
					"           decode(code,'TYZB013',12),\r\n" + 
					"           decode(code,'TYZB008',13)";
		}
		
		List<GenerateModel> generte=jdbcTemplate.query(sql, new BeanPropertyRowMapper<GenerateModel>(GenerateModel.class),Custno);
		return generte;
	}
	
	/**
	 * @param type 
	 * @该客户评级生效的评级数据的定性指标
	 * @Custno 
	 */
	@Override
	public List<ScoreDetail> scoreDetail(String Custno, String type) {
		//获取复核过的指标值UNI_S_QL_1_2_B02
		String sqlInit ="";
		String sqlReview="";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
			 sqlReview =" select  step.fd_vaild, '定性指标' as indexType,--一级指标\r\n" + 
						" (case when "
						+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B01' or "
						+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B02' or "
						+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B03' or "
						+ "indexe.fd_indexcode =  'UNI_M_QL_1_1' or "
						+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B01' or "
						+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B02' or "
						+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B03' or "
						+ "indexe.fd_indexcode =  'UNI_S_QL_1_1' "
						+ "then '企业外部环境' else '企业自身情况' end) as inOutType ,--二级指标\r\n" + 
						" parent.fd_indexName as IndexParentName,--三级指标\r\n" + 
						" indexe.fd_indexcode as IndexCode, --四级指标编码\r\n" + 
						" indexe.fd_indexName as IndexName,--四级指标\r\n" + 
						" indexe.fd_indexvalue as IndexValue," + 
						" indexe.fd_qualitative_options as indexOption\r\n" + 
						" from \r\n" + 
						" ns_rating_indexes indexe \r\n" + 
						" inner join ns_rating_indexes parent on parent.FD_id=indexe.fd_parent_id\r\n" + 
						" inner join ns_rating_step step on step.fd_id=indexe.fd_stepid\r\n" + 
						" inner join ns_main_rating rating on rating.fd_id = step.fd_rateid\r\n" + 
						" where indexe.FD_level = '3' and indexe.fd_indexType = 'QUALITATIVE_EDIT' \r\n" + 
						" and step.fd_vaild in('1') and rating.fd_ID=? order by inOutType,indexparentname ";
				

				//获取复核过的指标值
				sqlInit =" select indexe.FD_INDEXTYPE as indexType,--一级指标\r\n" + 
						" (case when indexe.fd_indexcode =  'UNI_M_QL_1_2_B01' or indexe.fd_indexcode =  'UNI_M_QL_1_2_B02' or indexe.fd_indexcode =  'UNI_M_QL_1_2_B03' or indexe.fd_indexcode =  'UNI_M_QL_1_1' then '企业外部环境' else '企业自身情况' end) as inOutType ,--二级指标\r\n" + 
						" parent.fd_indexName as IndexParentName,--三级指标\r\n" + 
						" indexe.fd_indexcode as IndexCode, --四级指标编码\r\n" + 
						" indexe.fd_indexName as IndexName,--四级指标\r\n" + 
						" indexe.fd_indexvalue as IndexValue\r\n" + 
						" from \r\n" + 
						" ns_rating_indexes indexe \r\n" + 
						" inner join ns_rating_indexes parent on parent.FD_id=indexe.fd_parent_id\r\n" + 
						" inner join ns_rating_step step on step.fd_id=indexe.fd_stepid\r\n" + 
						" inner join ns_main_rating rating on rating.fd_id = step.fd_rateid\r\n" + 
						" where indexe.FD_level = '3' and indexe.fd_indexType = 'QUALITATIVE_EDIT' \r\n" + 
						" and step.fd_vaild in('not') and rating.fd_ID=?";
		}else {
			 sqlReview =" select  step.fd_vaild, '定性指标' as indexType,--一级指标\r\n" + 
					" (case when "
					+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B01' or "
					+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B02' or "
					+ "indexe.fd_indexcode =  'UNI_M_QL_1_2_B03' or "
					+ "indexe.fd_indexcode =  'UNI_M_QL_1_1' or "
					+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B01' or "
					+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B02' or "
					+ "indexe.fd_indexcode =  'UNI_S_QL_1_2_B03' or "
					+ "indexe.fd_indexcode =  'UNI_S_QL_1_1' "
					+ "then '企业外部环境' else '企业自身情况' end) as inOutType ,--二级指标\r\n" + 
					" parent.fd_indexName as IndexParentName,--三级指标\r\n" + 
					" indexe.fd_indexcode as IndexCode, --四级指标编码\r\n" + 
					" indexe.fd_indexName as IndexName,--四级指标\r\n" + 
					" indexe.fd_indexvalue as IndexValue," + 
					" indexe.fd_qualitative_options as indexOption\r\n" + 
					" from \r\n" + 
					" ns_rating_indexes indexe \r\n" + 
					" inner join ns_rating_indexes parent on parent.FD_id=indexe.fd_parent_id\r\n" + 
					" inner join ns_rating_step step on step.fd_id=indexe.fd_stepid\r\n" + 
					" inner join ns_main_rating rating on rating.fd_id = step.fd_rateid\r\n" + 
					" where indexe.FD_level = '3' and indexe.fd_indexType = 'QUALITATIVE_EDIT' \r\n" + 
					" and step.fd_vaild in('1') and rating.fd_ID=? order by inOutType,indexparentname ";
			

			//获取复核过的指标值
			sqlInit =" select indexe.FD_INDEXTYPE as indexType,--一级指标\r\n" + 
					" (case when indexe.fd_indexcode =  'UNI_M_QL_1_2_B01' or indexe.fd_indexcode =  'UNI_M_QL_1_2_B02' or indexe.fd_indexcode =  'UNI_M_QL_1_2_B03' or indexe.fd_indexcode =  'UNI_M_QL_1_1' then '企业外部环境' else '企业自身情况' end) as inOutType ,--二级指标\r\n" + 
					" parent.fd_indexName as IndexParentName,--三级指标\r\n" + 
					" indexe.fd_indexcode as IndexCode, --四级指标编码\r\n" + 
					" indexe.fd_indexName as IndexName,--四级指标\r\n" + 
					" indexe.fd_indexvalue as IndexValue\r\n" + 
					" from \r\n" + 
					" ns_rating_indexes indexe \r\n" + 
					" inner join ns_rating_indexes parent on parent.FD_id=indexe.fd_parent_id\r\n" + 
					" inner join ns_rating_step step on step.fd_id=indexe.fd_stepid\r\n" + 
					" inner join ns_main_rating rating on rating.fd_id = step.fd_rateid\r\n" + 
					" where indexe.FD_level = '3' and indexe.fd_indexType = 'QUALITATIVE_EDIT' \r\n" + 
					" and step.fd_vaild in('2') and rating.fd_ID=?";
		}
		List<ScoreDetail> generteReview=jdbcTemplate.query(sqlReview, new BeanPropertyRowMapper<ScoreDetail>(ScoreDetail.class),Custno);
		List<ScoreDetail> generteInit=jdbcTemplate.query(sqlInit, new BeanPropertyRowMapper<ScoreDetail>(ScoreDetail.class),Custno);
		
		for (ScoreDetail scoreDetail : generteReview) {
			if(generteInit.size()>0) {
				for (ScoreDetail scoreDetail2 : generteInit) {
					if(scoreDetail.getIndexCode().equals(scoreDetail2.getIndexCode())) {
						scoreDetail.setOldValue(scoreDetail2.getIndexValue());
					}
				}	
			}else {
				scoreDetail.setOldValue(scoreDetail.getIndexValue());
				scoreDetail.setIndexValue("");
			}
			
		}
		return generteReview;
	}
	/**
	 * 
	 * @param Custno
	 * @return 获取二级指标类别
	 */
	@Override
	public List<Map<String,Object>> bigType(String Custno) {
		String sql ="select  indexe.fd_id,indexe.FD_INDEXTYPE as indexType,--一级指标\r\n" + 
				"	 indexe.fd_indexcode as IndexCode, --四级指标编码\r\n" + 
				"	 indexe.fd_indexName as IndexName--四级指标\r\n" + 
				"	 from \r\n" + 
				"	 ns_rating_indexes indexe \r\n" + 
				"	 inner join ns_rating_step step on step.fd_id=indexe.fd_stepid\r\n" + 
				"	 inner join ns_main_rating rating on rating.fd_id = step.fd_rateid\r\n" + 
				"	 where  \r\n" + 
				"	  step.fd_vaild in('1') \r\n" + 
				"	 and rating.fd_ID=?\r\n" + 
				"	 and indexe.FD_level = '2' and fd_indexcode not like 'YRZB%'\r\n" + 
				"	 order by fd_indexcode asc";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,Custno);
		return list;
	}
	/**
	 * 
	 * @param Custno
	 * @return 获取三级指标
	 */
	@Override
	public List<Map<String,Object>> queryByParent(String parentid) {
		String sql ="select FD_INDEXNAME,FD_INDEXVALUE,FD_SCORE,FD_QUALITATIVE_OPTIONS from ns_rating_indexes indexe where indexe.fd_parent_id=?";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,parentid);
		return list;
	}
	
	/**
	 * @throws Exception 
	 * @若ratingId为空，生成一条评级数据
	 *
	 */
	@Override
	@Transactional
	public String createRating(String bpCode,String tempType,String version) throws Exception {
		String sqlCust = "select fd_bp_code,fd_bp_name,FD_SCORE_TEMPLATE_ID from NS_BP_MASTER where fd_bp_code=?";
		List<Map<String,Object>> map = jdbcTemplate.queryForList(sqlCust,bpCode);
		String code = map.get(0).get("FD_BP_CODE").toString();
		String name = map.get(0).get("FD_BP_NAME").toString();
		String TemplateId="";
		if(map.get(0).get("FD_SCORE_TEMPLATE_ID")!=null) {
			if(!tempType.equals(map.get(0).get("FD_SCORE_TEMPLATE_ID").toString())) {
				TemplateId=tempType;
			}else {
				TemplateId=map.get(0).get("FD_SCORE_TEMPLATE_ID").toString();
			}
		}else {
			TemplateId=tempType;
		}
		String sql="insert into ns_main_rating (FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_TYPE,fd_version)values(?,?,?,?,?)";
		String uuid = UUID.randomUUID().toString();
		jdbcTemplate.update(sql,uuid,code,name,TemplateId,version);
		List<RatingMainStep> ratingSteps = new ArrayList<RatingMainStep>();
		MainRating mainRating = new MainRating();
		mainRating=mainRatingServiceImpl.findById(uuid);
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from  NS_R_CFG_STEPS  where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
		for(int i=0;i<list.size();i++) {
			//如果获取的 评级步骤是 “定量分析-QUANTITATIVE” 或者评级步骤是 “定性指标录入-QUALITATIVE_EDIT”	
			if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
					|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(mainRating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");
				// 持久化步骤实例
				step = ratingMainStepService.add(step);
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			} else {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(mainRating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");
				// 持久化步骤实例
				step = ratingMainStepService.add(step);
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			}
		}
		mainRating.setCurrentStepId(ratingSteps.get(0).getId());
		mainRating.setCurrentStep(ratingSteps.get(0));
//		if(version.equals("2.0")) {
//			tempType=tempType+"_2";
//		}
		for (RatingMainStep RatingMainStep : ratingSteps) {
			/*这块只会保存定性、定量的数据，因为在fd_step_type表中有三种模板类型分别是（REPORT_INFO、QUANTITATIVE、QUALITATIVE_EDIT）
			  但是在ns_main_report中并没有 “REPORT_INFO” 的模板类型，所以只会保存前两步的数据，第三步的数据将无法保存
			 * */
			RatingMainStep.getStepType();
			String sqlIndex = "update ns_main_report set FD_STEPID=? where fd_INDEXTYPE=? and FD_MODEL=?";
			jdbcTemplate.update(sqlIndex,RatingMainStep.getId(),RatingMainStep.getStepType().toString(),tempType);
		}
		
		List<Map<String,Object>> listMap =jdbcTemplate.queryForList("select * from ns_main_report where FD_MODEL=?",tempType);
		for (Map<String, Object> map2 : listMap) {
			String sqlIndex = "update ns_main_report set FD_ID=? where fd_ID=? and FD_MODEL=?";
			jdbcTemplate.update(sqlIndex,UUID.randomUUID().toString(),map2.get("FD_ID").toString(),tempType);
		}
		repository.flush();
		String insertIndex = "insert into ns_rating_indexes value select * from ns_main_report where FD_MODEL=?";
		jdbcTemplate.update(insertIndex,tempType);
		
		return mainRating.getId();
	}

	@Override
	public List<Map<String,Object>> findScore(String bpCode) throws Exception {
		String insertIndex = "select * from ns_rating_indexes where FD_PARENT_ID=?";
		List<Map<String,Object>> map = jdbcTemplate.queryForList(insertIndex,bpCode);
		return map;
	}
	
	
	//偿债能力
	
	
}
