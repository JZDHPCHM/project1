package net.gbicc.app.irr.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrAuthEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthRepository;

/**
 * 授权管理
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
public interface IrrAuthService extends DaoService<IrrAuthEntity, String, IrrAuthRepository> {

    /**
     * 保存授权
     * 
     * @param data
     *            授权数据
     * @param userId
     *            授权人ID
     * @return
     */
    public Map<String, Object> saveAuth(String data, String userId) throws Exception;

    /**
     * 查询授权
     * 
     * @param userId
     *            授权人ID
     * @return
     */
    public Map<String, Object> findAuth(String userId) throws Exception;

    /**
     * 是否为admin角色
     *
     * @param userId
     *            用户ID
     * @return
     * @throws Exception
     */
    public Boolean isAdmin(String userId) throws Exception;

}
