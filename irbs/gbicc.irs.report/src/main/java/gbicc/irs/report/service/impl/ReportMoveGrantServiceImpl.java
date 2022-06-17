package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportMoveGrant;
import gbicc.irs.report.repository.ReportMoveGrantRepository;
import gbicc.irs.report.service.ReportMoveGrantService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportMoveGrantServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月5日 下午5:09:34 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportMoveGrantServiceImpl
		extends
		DaoServiceImpl<ReportMoveGrant, String, ReportMoveGrantRepository>
		implements ReportMoveGrantService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/moveGrant";
	}

	protected List<Object> convertExportData(List<ReportMoveGrant> list){
		List<Object> res = new ArrayList<Object>();
		ReportMoveGrant tmp = null;
		for(ReportMoveGrant e : list){
			if(e.getFdKindName().equals("本期合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
