package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportAnalyseBranch;
import gbicc.irs.report.repository.ReportAnalyseBranchRepository;
import gbicc.irs.report.service.ReportAnalyseBranchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportAnalyseBranchServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月24日 上午9:03:58 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportAnalyseBranchServiceImpl
		extends
		DaoServiceImpl<ReportAnalyseBranch, String, ReportAnalyseBranchRepository>
		implements ReportAnalyseBranchService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/analyseBranch";
	}

	protected List<Object> convertExportData(List<ReportAnalyseBranch> list){
		List<Object> res = new ArrayList<Object>();
		ReportAnalyseBranch tmp = null;
		for(ReportAnalyseBranch e : list){
			if(e.getArea().equals("全行合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
