package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportProfitKinds;
import gbicc.irs.report.repository.ReportProfitKindsRepository;
import gbicc.irs.report.service.ReportProfitKindsService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportProfitKindsServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月29日 上午9:33:52 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportProfitKindsServiceImpl
		extends
		DaoServiceImpl<ReportProfitKinds, String, ReportProfitKindsRepository>
		implements ReportProfitKindsService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/profitKinds";
	}

	protected List<Object> convertExportData(List<ReportProfitKinds> list){
		List<Object> res = new ArrayList<Object>();
		ReportProfitKinds tmp =null;
		for(ReportProfitKinds e : list){
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
