package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportNewAnalyseBranch;
import gbicc.irs.report.repository.ReportNewAnalyseBranchRepository;
import gbicc.irs.report.service.ReportNewAnalyseBranchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportNewAnalyseBranchServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月23日 下午2:22:34 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportNewAnalyseBranchServiceImpl
		extends
		DaoServiceImpl<ReportNewAnalyseBranch, String, ReportNewAnalyseBranchRepository>
		implements ReportNewAnalyseBranchService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/newAnalyseBranch";
	}

	protected List<Object> convertExportData(List<ReportNewAnalyseBranch> list){
		List<Object> res = new ArrayList<Object>();
		ReportNewAnalyseBranch tmp = null;
		for(ReportNewAnalyseBranch e : list){
			if(e.getArea().equals("全行")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
