package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrInfosysResultRepository;

/**
* 信息系统结果
*/
public interface IrrInfosysResultService extends DaoService<IrrInfosysResultEntity, String, IrrInfosysResultRepository> {

    /**
     * 根据条件查询指标值
     *
     * @param taskBatch
     *            任务期次
     * @param indexCode
     *            指标编码
     * @return
     * @throws Exception
     */
    public String findIndexValueByCondition(String taskBatch, String indexCode) throws Exception;
}
