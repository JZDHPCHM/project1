package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportProfitBranch;
import gbicc.irs.report.repository.ReportProfitBranchRepository;
import gbicc.irs.report.service.ReportProfitBranchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportProfitBranchServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月29日 上午10:50:13 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportProfitBranchServiceImpl
		extends
		DaoServiceImpl<ReportProfitBranch, String, ReportProfitBranchRepository>
		implements ReportProfitBranchService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/profitBranch";
	}

	protected List<Object> convertExportData(List<ReportProfitBranch> list){
		List<Object> res = new ArrayList<Object>();
		ReportProfitBranch tmp =null;
		for(ReportProfitBranch e : list){
			if(e.getFdArea().equals("全行合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
