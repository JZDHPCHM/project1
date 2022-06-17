package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportLoanFive;
import gbicc.irs.report.repository.ReportLoanFiveRepository;
import gbicc.irs.report.service.ReportLoanFiveService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportLoanFiveServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月29日 上午10:50:13 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportLoanFiveServiceImpl
		extends
		DaoServiceImpl<ReportLoanFive, String, ReportLoanFiveRepository>
		implements ReportLoanFiveService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/loanFive";
	}

	protected List<Object> convertExportData(List<ReportLoanFive> list){
		List<Object> res = new ArrayList<Object>();
		ReportLoanFive tmp =null;
		for(ReportLoanFive e : list){
			if(e.getFdKindName().equals("平均收益率")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
