package gbicc.irs.report.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.report.entity.ReportPriceLimit;
import gbicc.irs.report.repository.ReportPriceLimitRepository;
import gbicc.irs.report.service.ReportPriceLimitService;

/**
 * 
 * ClassName: ReportPriceLimitServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月7日 上午10:06:28 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service("ReportPriceLimitService")
public class ReportPriceLimitServiceImpl
		extends
		DaoServiceImpl<ReportPriceLimit, String, ReportPriceLimitRepository>
		implements ReportPriceLimitService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/priceLimit";
	}


	@Override
	public String getOrgLst() {
		List<String> result = jdbcTemplate.query("select distinct(t1.fd_type) as org from irs_report_price_limit t1 order by t1.fd_type asc",new Object[] {  }, 
		new RowMapper<String>()
		{
			@Override
		     public String mapRow(ResultSet rs, int rowNum) throws SQLException 
			{
				
				return rs.getString("org");
			}
		});

		return JSONObject.toJSONString(result);
	}

	protected List<Object> convertExportData(List<ReportPriceLimit> list){
		List<Object> res = new ArrayList<Object>();
		ReportPriceLimit tmp = null;
		for(ReportPriceLimit e : list){
			if(e.getFdGuestGr().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
