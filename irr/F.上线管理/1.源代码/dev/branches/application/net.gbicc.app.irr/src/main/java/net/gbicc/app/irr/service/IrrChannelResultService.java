package net.gbicc.app.irr.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrChannelResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrChannelResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;

/**
* 渠道结果
*/
public interface IrrChannelResultService extends DaoService<IrrChannelResultEntity, String, IrrChannelResultRepository> {

	/**
	 * 汇总渠道结果
	 * @param taskId 评估计划ID
	 * @return
	 * @throws Exception
	 */
	public List<IrrChannelResultEntity> summChannelResult(String taskId) throws Exception;

	/**
	 * 计算分公司相同指标渠道相加的结果
	 * @param taskId 评估计划ID
	 * @param orgId 机构ID
	 * @return
	 */
	public List<IndexValueDTO> sumBranchChannelIndexResult(String taskId,String orgId);
}
