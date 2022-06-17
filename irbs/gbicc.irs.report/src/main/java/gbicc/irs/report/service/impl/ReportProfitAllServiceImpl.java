package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportProfitAll;
import gbicc.irs.report.repository.ReportProfitAllRepository;
import gbicc.irs.report.service.ReportProfitAllService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportProfitAllServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月24日 上午9:03:58 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportProfitAllServiceImpl
		extends
		DaoServiceImpl<ReportProfitAll, String, ReportProfitAllRepository>
		implements ReportProfitAllService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/profitAll";
	}
	
	protected List<Object> convertExportData(List<ReportProfitAll> list){
		List<Object> res = new ArrayList<Object>();
		ReportProfitAll tmp =null;
		for(ReportProfitAll e : list){
			if(e.getGuestGr().equals("平均收益率")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
