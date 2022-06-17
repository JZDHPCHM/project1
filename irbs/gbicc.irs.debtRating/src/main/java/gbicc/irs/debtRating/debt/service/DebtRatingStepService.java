package gbicc.irs.debtRating.debt.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.entity.RatingDebtStep;
import gbicc.irs.debtRating.debt.repository.DebtRatingRepository;
import gbicc.irs.debtRating.debt.repository.RatingDebtStepRepository;
import gbicc.irs.debtRating.debt.support.Dxfx;
import gbicc.irs.debtRating.debt.support.ProjectRatingInfo;
import gbicc.irs.debtRating.debt.support.Zbfx;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.RatingMainStepRepository;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.ScoreDetail;

public interface DebtRatingStepService extends DaoService<RatingDebtStep, String, RatingDebtStepRepository> {

	DebtRating nextStep(String stepId) throws Exception;

	DebtRating lastStep(String stepId) throws Exception;

	DebtRating checkQualitative(String stepId, Map<String, String> paramValue) throws Exception;

	DebtRating saveQualitativeAndNextStep(String stepId, Map<String, String> paramValue) throws Exception;

	List<RatingDebtStep> getAdditionalStepByRatingId(String ratingId) throws Exception;

	ProjectRatingInfo projectInfo(String projectNo);

	List<Map<String, Object>> zbfx(String projectNo);

	List<List<Dxfx>> dxfx(String projectNo);

	boolean leaseItem(String path, String debtId) throws Exception;

	boolean prjOperatingFlow(String path, String prjId) throws Exception;

	boolean prjRealEstate(String path, String prjId) throws Exception;

	boolean prjRaiseSexual(String path, String prjId) throws Exception;

	boolean prjLease(String path, String prjId) throws Exception;

	boolean prjLegalPerson(String path, String prjId) throws Exception;

	boolean prjEquity(String path, String prjId) throws Exception;

	boolean prjAccount(String path, String prjId) throws Exception;

	boolean prjNaturalPerson(String path, String prjId) throws Exception;

	Map<String, Object> interPa(String json) throws Exception;



	Map<String, String> assemblyDataInter(Map<String, Object> result2);

	List<Map<String, Object>> bigType(String Custno, String Type);


	Map<String, String> assemblyData(String prjId, DebtRating rating);

	String prjInfo(String path, String proCode, String ratingLevel) throws Exception;


	List<Map<String, Object>> debtMxb(String Custno);






}
