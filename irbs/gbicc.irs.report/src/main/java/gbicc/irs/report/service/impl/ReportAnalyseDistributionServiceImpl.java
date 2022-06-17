package gbicc.irs.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportAnalyseDistribution;
import gbicc.irs.report.entity.ReportRatingDistribution;
import gbicc.irs.report.repository.ReportAnalyseDistributionRepository;
import gbicc.irs.report.service.ReportAnalyseDistributionService;
/**
 * 
 * ClassName: ReportAnalyseDistributionServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月23日 上午9:27:31 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportAnalyseDistributionServiceImpl
		extends
		DaoServiceImpl<ReportAnalyseDistribution, String, ReportAnalyseDistributionRepository>
		implements ReportAnalyseDistributionService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/analyseDistribution";
	}

	protected List<Object> convertExportData(List<ReportAnalyseDistribution> list){
		List<Object> res = new ArrayList<Object>();
		ReportAnalyseDistribution tmp = null;
		for(ReportAnalyseDistribution e : list){
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
