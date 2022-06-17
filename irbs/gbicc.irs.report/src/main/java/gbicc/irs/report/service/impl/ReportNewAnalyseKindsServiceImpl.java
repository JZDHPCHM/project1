package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportNewAnalyseKinds;
import gbicc.irs.report.repository.ReportNewAnalyseKindsRepository;
import gbicc.irs.report.service.ReportNewAnalyseKindsService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * ClassName: ReportNewAnalyseKindsServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2019年4月23日 下午2:22:34 <br/>
 *
 * @author xiaoxianlie
 * @version
 * @since JDK 1.8
 */
@Service
public class ReportNewAnalyseKindsServiceImpl
		extends
		DaoServiceImpl<ReportNewAnalyseKinds, String, ReportNewAnalyseKindsRepository>
		implements ReportNewAnalyseKindsService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/newAnalyseKinds";
	}

	protected List<Object> convertExportData(List<ReportNewAnalyseKinds> list){
		List<Object> res = new ArrayList<Object>();
		ReportNewAnalyseKinds tmp = null;
		for(ReportNewAnalyseKinds e : list){
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
