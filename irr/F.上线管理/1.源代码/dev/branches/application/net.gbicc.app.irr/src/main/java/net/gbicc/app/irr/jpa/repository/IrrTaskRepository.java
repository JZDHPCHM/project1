package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;

/**
* 任务
*/
public interface IrrTaskRepository extends DaoRepository<IrrTaskEntity, String> {

	/**
	 * 根据任务期次查询任务
	 * @param taskBatch 任务期次
	 * @return
	 */
	public List<IrrTaskEntity> findByTaskBatch(String taskBatch);
}
