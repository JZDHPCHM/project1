package net.gbicc.app.irr.service;

import org.flowable.task.api.Task;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthTaskLogRepository;

/**
 * 授权日志
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
public interface IrrAuthTaskLogService extends DaoService<IrrAuthTaskLogEntity, String, IrrAuthTaskLogRepository> {

    /**
     * 保存权限任务处理日志
     *
     * @param planId
     *            计划ID
     * @param task
     *            流程任务
     * @param comment
     *            处理结果
     * @return
     * @throws Exception
     */
    public IrrAuthTaskLogEntity saveAuthTaskLog(String planId, Task task, String comment) throws Exception;

    /**
     * 查询授权日志
     * 
     * @return
     */
    public ResponseWrapper<IrrAuthTaskLogEntity> findAuthTaskLog(String info, Long page, Integer size) throws Exception;

}