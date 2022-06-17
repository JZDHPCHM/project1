package net.gbicc.app.irr.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;

/**
 * 授权任务日志表dao接口
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
public interface IrrAuthTaskLogRepository extends DaoRepository<IrrAuthTaskLogEntity, String> {

    /**
     * 根据流程任务ID查询日志
     *
     * @param taskId
     *            流程任务ID
     * @return
     */
    public IrrAuthTaskLogEntity findByTaskId(String taskId);
}
