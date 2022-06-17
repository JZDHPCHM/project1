package gbicc.irs.report.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.report.entity.ReportPriceLimit;
import gbicc.irs.report.repository.ReportPriceLimitRepository;

/**
 * 
 * ClassName: ReportPriceLimitService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月7日 上午10:06:51 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

public interface ReportPriceLimitService extends
		DaoService<ReportPriceLimit, String,ReportPriceLimitRepository> {
	String  getOrgLst();
}
