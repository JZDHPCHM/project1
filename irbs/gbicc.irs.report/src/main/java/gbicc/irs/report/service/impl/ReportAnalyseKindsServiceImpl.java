package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportAnalyseKinds;
import gbicc.irs.report.repository.ReportAnalyseKindsRepository;
import gbicc.irs.report.service.ReportAnalyseKindsService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportAnalyseKindsServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月23日 下午2:22:34 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportAnalyseKindsServiceImpl
		extends
		DaoServiceImpl<ReportAnalyseKinds, String, ReportAnalyseKindsRepository>
		implements ReportAnalyseKindsService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/analyseKinds";
	}

	protected List<Object> convertExportData(List<ReportAnalyseKinds> list){
		List<Object> res = new ArrayList<Object>();
		ReportAnalyseKinds tmp = null;
		for(ReportAnalyseKinds e : list){
			if(e.getName().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
