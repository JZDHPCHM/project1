package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportSingleManage;
import gbicc.irs.report.repository.ReportSingleManageRepository;
import gbicc.irs.report.service.ReportSingleManageService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportSingleManageServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 下午2:51:01 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

@Service
public class ReportSingleManageServiceImpl
		extends
		DaoServiceImpl<ReportSingleManage, String, ReportSingleManageRepository>
		implements ReportSingleManageService {
	

	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/singleManage";
	}

	protected List<Object> convertExportData(List<ReportSingleManage> list){
		List<Object> res = new ArrayList<Object>();
		ReportSingleManage tmp = null;
		for(ReportSingleManage e : list){
			if(e.getFdKindName().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
