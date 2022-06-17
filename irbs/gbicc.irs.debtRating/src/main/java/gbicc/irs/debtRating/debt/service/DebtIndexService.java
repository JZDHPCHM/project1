package gbicc.irs.debtRating.debt.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.repository.DebtIndexRepository;
import gbicc.irs.main.rating.support.RatingStepType;

public interface DebtIndexService extends DaoService<RatingDebtIndex, String, DebtIndexRepository> {
	
	/**
	 * 根据步骤ID查询指标
	 * @param stepId
	 * @return
	 * @throws Exception
	 */
	 List<RatingDebtIndex> getRatingIndexsByStepId(String stepId) throws Exception;
	 
	 /**
	  * 根据步骤ID查询指标和指标值
	  * @param stepId
	  * @return
	  * @throws Exception
	  */
	 Map<String,String> getRatingIndexsValueByStepId(String stepId) throws Exception;
 
	 /**
	 * 根据指标类型查询指标
	 * @param stepType
	 * @return
	 * @throws Exception
	 */
	 List<RatingDebtIndex> getRatingIndexsByIndexType(RatingStepType stepType) throws Exception;
	 
}
