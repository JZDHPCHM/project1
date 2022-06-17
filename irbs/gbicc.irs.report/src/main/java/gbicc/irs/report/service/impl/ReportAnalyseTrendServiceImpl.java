package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportAnalyseTrend;
import gbicc.irs.report.repository.ReportAnalyseTrendRepository;
import gbicc.irs.report.service.ReportAnalyseTrendService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportAnalyseTrendServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月5日 上午10:33:32 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportAnalyseTrendServiceImpl
		extends
		DaoServiceImpl<ReportAnalyseTrend, String, ReportAnalyseTrendRepository>
		implements ReportAnalyseTrendService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/analyseTrend";
	}

	protected List<Object> convertExportData(List<ReportAnalyseTrend> list){
		List<Object> res = new ArrayList<Object>();
		ReportAnalyseTrend tmp = null;
		for(ReportAnalyseTrend e : list){
			if(e.getGuestGr().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
