package gbicc.irs.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jdbc.sql.dialect.Dialect;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportApplicationTrend;
import gbicc.irs.report.repository.ReportApplicationTrendRepository;
import gbicc.irs.report.service.ReportApplicationTrendService;
/**
 * 
 * ClassName: ReportApplicationTrendServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月26日 上午9:18:34 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportApplicationTrendServiceImpl
		extends
		DaoServiceImpl<ReportApplicationTrend, String, ReportApplicationTrendRepository>
		implements ReportApplicationTrendService {
	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	@Autowired 
	private Dialect dialect;
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/applicationTrend";
	}

	@Override
	public Page<ReportApplicationTrend> list(ReportApplicationTrend queryExampleEntity, Pageable pageable) throws Exception {
		  
		StringBuilder RatingSql = new StringBuilder("SELECT  id as id, ")
				.append("fd_name as name, ")
				.append("fd_range_name as rangeName, ")
				.append("fd_range_order as rangeOrder, ")
				.append("fd_local_rufuse_times as localRufuseTimes, ")
				.append("fd_local_rufuse_per as localRufusePer, ")
				.append("fd_local_rufuse_comp_q as localRufuseCompQ, ")
				.append("fd_local_rufuse_comp_y as localRufuseCompY, ")
				.append("fd_upper_rufuse_times as upperRufuseTimes, ")
				.append("fd_upper_rufuse_per as upperRufusePer, ")
				.append("fd_upper_rufuse_comp_q as upperRufuseCompQ, ")
				.append("fd_upper_rufuse_comp_y as upperRufuseCompY, ")
				.append("fd_sum_refuse_per as sumRufusePer, ")
				.append("fd_rep_time as repTime, ")
				.append("fd_corporation as corporation, ")
				.append("fd_creator as creator, ")
				.append("fd_create_date as createDate, ")
				.append("fd_last_modifier as lastModifier, ")
				.append("fd_last_modifydate as lastModifydate, ")
				.append("fd_year as year, ")
				.append("fd_quarter as quarter ")
				.append(" FROM  irs_report_refuse_trend t ")
				.append("where 1 =1  ");
		RatingSql.append(this.buildCustomerRatingQueryCondition(queryExampleEntity,true));
//		String sql =dialect.limit(RatingSql.toString(), pageable.getPageNumber()+1, pageable.getPageSize());
		List<ReportApplicationTrend> ratingTask = jdbcTemplate.query(RatingSql.toString(), new BeanPropertyRowMapper<ReportApplicationTrend>(ReportApplicationTrend.class));
/*		String countSql= "select count(1) "
				+"   from irs_report_refuse_trend t where 1=1 ";
		countSql += this.buildCustomerRatingQueryCondition(queryExampleEntity,false);
		Long size=jdbcTemplate.queryForObject(countSql, Long.class);*/

		return new PageImpl<ReportApplicationTrend>(ratingTask,pageable,0);
	}
	
	private String buildCustomerRatingQueryCondition(ReportApplicationTrend queryExampleEntity,boolean order) throws Exception {
		String condSql = "";
		if(!StringUtils.isEmpty(queryExampleEntity.getYear())) {
			condSql += " AND  t.fd_year = "+queryExampleEntity.getYear()+"";
		}
		if(!StringUtils.isEmpty(queryExampleEntity.getQuarter())) {
			condSql += " AND  t.fd_quarter = "+queryExampleEntity.getQuarter()+"";
		}
		if(order) condSql +=" order by t.fd_name,t.fd_range_order ";
		return condSql;
	}

}
