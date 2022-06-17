package net.gbicc.app.irr.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrChannelResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrChannelResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;

/**
* 渠道结果
*/
public interface IrrChannelResultService extends DaoService<IrrChannelResultEntity, String, IrrChannelResultRepository> {

	/**
	 * 保存总公司渠道结果
	 * @param taskId 评估计划ID
	 * @param org    渠道所属组织机构
	 * @return
	 * @throws Exception
	 */
	public List<IrrChannelResultEntity> saveHeadChannelResult(String taskId,Org org) throws Exception;

	/**
	 * 计算分公司相同指标渠道相加的结果
	 * @param taskId 评估计划ID
	 * @param orgId 机构ID
	 * @return
	 */
	public List<IndexValueDTO> sumBranchChannelIndexResult(String taskId,String orgId);
	
	/**
	 * 计算渠道得分指标
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrChannelResultEntity> calcChannelScoreIndex(IrrTaskEntity task) throws Exception;
	
	/**
	 * 导出渠道数据
	 * @param task 评估计划
	 * @param wb poi文档操作对象
	 * @return
	 * @throws Exception
	 */
	public Workbook exportChannelIndexResult(IrrTaskEntity task,Workbook wb) throws Exception;
	
	/**
	 * 查询渠道数据
	 * @param taskId 评估计划ID
	 * @param projId 评估项目ID
	 * @param channelFlag 渠道标识
	 * @return
	 * @throws Exception
	 */
	public List<IrrChannelResultEntity> findByCondition(String taskId,String projId,String channelFlag) throws Exception;
	
}
