package gbicc.irs.report.service.impl;

import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.core.util.FileUtil;
import org.wsp.framework.core.util.IterableUtil;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.report.entity.ReportRatingDistribution;
import gbicc.irs.report.repository.ReportRatingDistributionRepository;
import gbicc.irs.report.service.ReportRatingDistributionService;
import org.wsp.framework.jpa.service.support.protocol.ExportFileInfo;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class ReportRatingDistributionServiceImpl
		extends
		DaoServiceImpl<ReportRatingDistribution, String, ReportRatingDistributionRepository>
		implements ReportRatingDistributionService {
	
	@Override
	protected String getXlsxTemplate() {
		return "classpath:/gbicc/irs/report/excel/rating/ratingDistribution";
	}

	protected List<Object> convertExportData(List<ReportRatingDistribution> list){
		List<Object> res = new ArrayList<Object>();
		ReportRatingDistribution tmp = null;
		for(ReportRatingDistribution e : list){
			int type = e.getType();
			e.setTypeName(type==1?"客户数目":type==2?"授信金额":"业务余额");
			if(e.getGuestGr().equals("合计")){
				tmp = e;
				continue;
			}
			res.add(e);
		}
		res.add(tmp);
		return res;
	}
}
