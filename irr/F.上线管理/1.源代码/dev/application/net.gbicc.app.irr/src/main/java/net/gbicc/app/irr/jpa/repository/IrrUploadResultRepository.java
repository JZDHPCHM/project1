package net.gbicc.app.irr.jpa.repository;

import javax.jdo.annotations.Transactional;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;

/**
* 上传结果
*/
public interface IrrUploadResultRepository extends DaoRepository<IrrUploadResultEntity, String> {

	/**
	 * 根据期次和采集人删除采集数据
	 * @param taskId 任务ID
	 * @param creator 采集人账号
	 */
	@Transactional
	public void deleteByTaskIdAndCreator(String taskId,String creator);
	
    /**
     * 根据任务、采集人、采集方式删除数据
     *
     * @param taskId
     *            任务ID
     * @param creator
     *            采集人
     * @param collWay
     *            采集方式
     */
    public void deleteByTaskIdAndCreatorAndCollWay(String taskId, String creator, String collWay);

	/**
	 * 根据拆分指标编码和期次查询上传结果
	 * @param splitCode 拆分指标编码
	 * @param taskBatch 期次
	 * @param collOrgId 采集机构
	 * @return
	 */
	public IrrUploadResultEntity findBySplitCodeAndTaskBatchAndCollOrgId(String splitCode,String taskBatch,String collOrgId);
}
