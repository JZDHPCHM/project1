package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportRatingBranch;
import gbicc.irs.report.repository.ReportRatingBranchRepository;
import gbicc.irs.report.service.ReportRatingBranchService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportRatingBranchServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月22日 下午3:58:50 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

@Service
public class ReportRatingBranchServiceImpl
		extends
		DaoServiceImpl<ReportRatingBranch, String, ReportRatingBranchRepository>
		implements ReportRatingBranchService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/ratingBranch";
	}

	protected List<Object> convertExportData(List<ReportRatingBranch> list){
		List<Object> res = new ArrayList<Object>();
		ReportRatingBranch tmp =null;
		for(ReportRatingBranch e : list){
			int type = e.getType();
			e.setTypeName(type==1?"客户数目":type==2?"授信金额":"业务余额");
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
