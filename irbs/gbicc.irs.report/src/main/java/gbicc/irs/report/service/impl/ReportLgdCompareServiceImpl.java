package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportLgdCompare;
import gbicc.irs.report.repository.ReportLgdCompareRepository;
import gbicc.irs.report.service.ReportLgdCompareService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportLgdCompareServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午10:11:11 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportLgdCompareServiceImpl
		extends
		DaoServiceImpl<ReportLgdCompare, String, ReportLgdCompareRepository>
		implements ReportLgdCompareService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/lgdCompare";
	}

	protected List<Object> convertExportData(List<ReportLgdCompare> list){
		List<Object> res = new ArrayList<Object>();
		ReportLgdCompare tmp = null;
		for(ReportLgdCompare e : list){
			if(e.getFdGuestGr().equals("全行")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
