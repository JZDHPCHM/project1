package gbicc.irs.report.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportRatingKinds;
import gbicc.irs.report.repository.ReportRatingKindsRepository;
import gbicc.irs.report.service.ReportRatingKindsService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: ReportRatingKindsServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月19日 下午2:16:54 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

@Service
public class ReportRatingKindsServiceImpl
		extends
		DaoServiceImpl<ReportRatingKinds, String, ReportRatingKindsRepository>
		implements ReportRatingKindsService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/ratingKinds";
	}

	protected List<Object> convertExportData(List<ReportRatingKinds> list){
		List<Object> res = new ArrayList<Object>();
		ReportRatingKinds tmp = null;
		for(ReportRatingKinds e : list){
			int type = e.getType();
			e.setTypeName(type==1?"客户数目":type==2?"授信金额":"业务余额");
			if(e.getKindName().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
