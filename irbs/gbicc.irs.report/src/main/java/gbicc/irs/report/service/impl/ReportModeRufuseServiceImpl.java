package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportModeRufuse;
import gbicc.irs.report.repository.ReportModeRufuseRepository;
import gbicc.irs.report.service.ReportModeRufuseService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportModeRufuseServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月7日 下午5:19:52 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportModeRufuseServiceImpl
		extends
		DaoServiceImpl<ReportModeRufuse, String, ReportModeRufuseRepository>
		implements ReportModeRufuseService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/modeRufuse";
	}

	protected List<Object> convertExportData(List<ReportModeRufuse> list){
		List<Object> res = new ArrayList<Object>();
		ReportModeRufuse tmp = null;
		for(ReportModeRufuse e : list){
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
