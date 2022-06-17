package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportSingleOver;
import gbicc.irs.report.repository.ReportSingleOverRepository;
import gbicc.irs.report.service.ReportSingleOverService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportSingleOverServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午11:52:39 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportSingleOverServiceImpl
		extends
		DaoServiceImpl<ReportSingleOver, String, ReportSingleOverRepository>
		implements ReportSingleOverService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/singleOver";
	}

	protected List<Object> convertExportData(List<ReportSingleOver> list){
		List<Object> res = new ArrayList<Object>();
		ReportSingleOver tmp = null;
		for(ReportSingleOver e : list){
			if(e.getArea().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
