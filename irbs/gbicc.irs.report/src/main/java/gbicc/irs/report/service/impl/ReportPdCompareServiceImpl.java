package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportPdCompare;
import gbicc.irs.report.repository.ReportPdCompareRepository;
import gbicc.irs.report.service.ReportPdCompareService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportPdCompareServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午9:38:41 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportPdCompareServiceImpl
		extends
		DaoServiceImpl<ReportPdCompare, String, ReportPdCompareRepository>
		implements ReportPdCompareService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/pdCompare";
	}

	protected List<Object> convertExportData(List<ReportPdCompare> list){
		List<Object> res = new ArrayList<Object>();
		ReportPdCompare tmp = null;
		for(ReportPdCompare e : list){
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
