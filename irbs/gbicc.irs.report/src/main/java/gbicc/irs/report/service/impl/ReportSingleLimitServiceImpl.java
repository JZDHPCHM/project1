package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportSingleLimit;
import gbicc.irs.report.repository.ReportSingleLimitRepository;
import gbicc.irs.report.service.ReportSingleLimitService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportSingleLimitServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午11:19:36 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

@Service
public class ReportSingleLimitServiceImpl
		extends
		DaoServiceImpl<ReportSingleLimit, String, ReportSingleLimitRepository>
		implements ReportSingleLimitService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/singleLimit";
	}

	protected List<Object> convertExportData(List<ReportSingleLimit> list){
		List<Object> res = new ArrayList<Object>();
		ReportSingleLimit tmp = null;
		for(ReportSingleLimit e : list){
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
