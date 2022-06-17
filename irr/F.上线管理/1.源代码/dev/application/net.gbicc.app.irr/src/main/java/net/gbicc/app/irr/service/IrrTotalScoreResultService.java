package net.gbicc.app.irr.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrTotalScoreResultRepository;

/**
* 总分多维度结果
*/
public interface IrrTotalScoreResultService extends DaoService<IrrTotalScoreResultEntity, String, IrrTotalScoreResultRepository> {

	/**
	 * 评估项目多维度汇总
	 * @param task		评价计划
	 * @param xbrlProj  上报的评估项目
	 * @param otherProj 其他的评估项目
	 * @return
	 * @throws Exception
	 */
	public List<IrrTotalScoreResultEntity> totalScoreSumm(IrrTaskEntity task) throws Exception;
	
	/**
	 * 机构多维度汇总
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param org  机构
	 * @return
	 * @throws Exception
	 */
	public List<IrrTotalScoreResultEntity> summOrgTotalScore(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) throws Exception;
	
	/**
	 * 查询多维度的评估项目
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findDistinctTotalScoreProjByTask(IrrTaskEntity task) throws Exception;
	
	/**
	 * 计算权重
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param dim  维度
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcWeight(IrrTaskEntity task,IrrProjTypeEntity proj,String dim) throws Exception;
	
	/**
     * 计算五大类权重
     * 
     * @param task
     *            评估计划
     * @param typeNameTable
     *            大类名称和对应的结果表(中间用-隔开)
     * @param dim
     *            权重维度
     * @param typeCode
     *            评估项目编码
     * @return
     * @throws Exception
     */
    public BigDecimal calcFiveProj(IrrTaskEntity task, String typeNameTable, String dim, String typeCode)
            throws Exception;

	/**
	 * 计算直接扣分需要特殊处理的权重<br/>
	 * =直接评分指标值之和-直接评分总分
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcDirectPointSpecialHand(IrrTaskEntity task,IrrProjTypeEntity proj) throws Exception;
	
	/**
	 * 查询多维度结果
	 * @param taskId 评估计划ID
	 * @param dim    多维度
	 * @param projCode 评估项目编码
	 * @param orgId  机构ID
	 * @return
	 * @throws Exception
	 */
	public String findProjResultByCondition(String taskId,String dim,String projCode,String orgId) throws Exception;
	
}
