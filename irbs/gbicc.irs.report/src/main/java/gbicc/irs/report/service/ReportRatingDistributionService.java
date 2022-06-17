package gbicc.irs.report.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.report.entity.ReportRatingDistribution;
import gbicc.irs.report.repository.ReportRatingDistributionRepository;

public interface ReportRatingDistributionService extends
		DaoService<ReportRatingDistribution, String,ReportRatingDistributionRepository> {

}
