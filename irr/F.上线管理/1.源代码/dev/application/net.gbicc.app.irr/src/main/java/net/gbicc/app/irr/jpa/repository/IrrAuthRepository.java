package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrAuthEntity;

/**
 * 授权管理dao接口
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
public interface IrrAuthRepository extends DaoRepository<IrrAuthEntity, String> {

    /**
     * 根据用户ID查询权限
     *
     * @param userId
     *            用户ID
     * @return
     */
    List<IrrAuthEntity> findByUserId(String userId);

}
