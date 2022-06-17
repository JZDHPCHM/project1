package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportProfitImportant;
import gbicc.irs.report.repository.ReportProfitImportantRepository;
import gbicc.irs.report.service.ReportProfitImportantService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportProfitImportantServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月28日 下午3:50:08 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportProfitImportantServiceImpl
		extends
		DaoServiceImpl<ReportProfitImportant, String, ReportProfitImportantRepository>
		implements ReportProfitImportantService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/profitImportant";
	}

	protected List<Object> convertExportData(List<ReportProfitImportant> list){
		List<Object> res = new ArrayList<Object>();
		ReportProfitImportant tmp =null;
		for(ReportProfitImportant e : list){
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
