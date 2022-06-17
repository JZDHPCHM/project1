package gbicc.irs.report.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.report.entity.ReportApplicationTrend;
import gbicc.irs.report.repository.ReportApplicationTrendRepository;
/**
 * 
 * ClassName: ReportApplicationTrendService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月26日 上午9:18:27 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
public interface ReportApplicationTrendService extends
		DaoService<ReportApplicationTrend, String,ReportApplicationTrendRepository> {

	public Page<ReportApplicationTrend> list(ReportApplicationTrend queryExampleEntity, Pageable pageable) throws Exception;

}
