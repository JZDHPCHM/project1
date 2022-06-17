package gbicc.irs.main.rating.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.RatingMainStepRepository;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.ScoreDetail;

public interface RatingMainStepService extends DaoService<RatingMainStep, String, RatingMainStepRepository> {

	MainRating nextStep(String stepId) throws Exception;

	MainRating lastStep(String stepId) throws Exception;

	MainRating checkQualitative(String stepId, Map<String, String> paramValue) throws Exception;

	MainRating saveQualitativeAndNextStep(String stepId, Map<String, String> paramValue) throws Exception;

	List<RatingMainStep> getAdditionalStepByRatingId(String ratingId) throws Exception;

	//CustRatingInfo custInfo(String ratingId);

	List<Map<String, Object>> queryByParent(String parentid);

	//List<ScoreDetail> scoreDetail(String Custno);

	List<Map<String, Object>> bigType(String Custno);

	List<Map<String, Object>> findScore(String bpCode) throws Exception;

	String createRating(String bpCode, String tempType, String version) throws Exception;

	CustRatingInfo custInfo(String Custno, String type);

	List<ScoreDetail> scoreDetail(String Custno, String type);



}
