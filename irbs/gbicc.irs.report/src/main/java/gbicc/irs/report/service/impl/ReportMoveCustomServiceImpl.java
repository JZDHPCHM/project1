package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportMoveCustom;
import gbicc.irs.report.repository.ReportMoveCustomRepository;
import gbicc.irs.report.service.ReportMoveCustomService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportMoveCustomServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月5日 下午2:56:38 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class ReportMoveCustomServiceImpl
		extends
		DaoServiceImpl<ReportMoveCustom, String, ReportMoveCustomRepository>
		implements ReportMoveCustomService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/moveCustom";
	}

	protected List<Object> convertExportData(List<ReportMoveCustom> list){
		List<Object> res = new ArrayList<Object>();
		ReportMoveCustom tmp = null;
		for(ReportMoveCustom e : list){
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
