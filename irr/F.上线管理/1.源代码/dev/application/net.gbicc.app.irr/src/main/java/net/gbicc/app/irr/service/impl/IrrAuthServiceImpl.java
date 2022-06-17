package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrAuthEntity;
import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.entity.QIrrAuthEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrAuthService;
import net.gbicc.app.irr.service.IrrAuthTaskLogService;

/**
 * 授权管理
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Service
public class IrrAuthServiceImpl extends DaoServiceImpl<IrrAuthEntity, String, IrrAuthRepository>
        implements IrrAuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private IrrAuthTaskLogService irrAuthTaskLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> saveAuth(String data, String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("无法授权：传入的授权人ID为空！");
        }
        //全部用户
        Map<String, User> userMap = new HashMap<String, User>();
        List<User> userList = userService.getRepository().findAll();
        if (CollectionUtils.isEmpty(userList)) {
            return IrrUtil.getMap(false, "系统没有用户 ！");
        }
        for (User temp : userList) {
            userMap.put(temp.getId(), temp);
        }
        //查询已经授权
        List<IrrAuthEntity> userAuthList = repository.findByUserId(userId);
        //全部取消
        if (StringUtils.isBlank(data) && CollectionUtils.isNotEmpty(userAuthList)) {
            for (IrrAuthEntity temp : userAuthList) {
                User u = userMap.get(temp.getAuthId());
                if (u == null) {
                    continue;
                }
                IrrAuthTaskLogEntity cancelLog = new IrrAuthTaskLogEntity();
                cancelLog.setUserId(userId);
                cancelLog.setUserName(SecurityUtil.getUserName());
                cancelLog.setAuthId(temp.getAuthId());
                cancelLog.setAuthName(u.getUserName());
                cancelLog.setDealResult("取消授权");
                irrAuthTaskLogService.add(cancelLog);
                remove(temp);
            }
            return IrrUtil.getMap(true, "取消成功！");
        }
        //不全部取消
        if (StringUtils.isNotBlank(data)) {
            String[] authArr = data.split(",");
            //日志集合
            List<IrrAuthTaskLogEntity> authLogList = new ArrayList<IrrAuthTaskLogEntity>();
            Map<String, IrrAuthEntity> userAuthMap = new HashMap<String, IrrAuthEntity>();
            if (CollectionUtils.isNotEmpty(userAuthList)) {
                for (IrrAuthEntity temp : userAuthList) {
                    userAuthMap.put(temp.getAuthId(), temp);
                }
            }
            //新增授权
            for (String temp : authArr) {
                if (userAuthMap.get(temp) != null) {
                    userAuthMap.remove(temp);
                    continue;
                }
                User u = userMap.get(temp);
                if (u == null) {
                    continue;
                }
                IrrAuthEntity entity = new IrrAuthEntity();
                entity.setUserId(userId);
                entity.setAuthId(temp);
                IrrAuthTaskLogEntity bean = new IrrAuthTaskLogEntity();
                bean.setUserId(userId);
                bean.setUserName(SecurityUtil.getUserName());
                bean.setAuthId(temp);
                bean.setAuthName(u.getUserName());
                bean.setDealResult("授权");
                authLogList.add(bean);
                add(entity);
            }
            //取消授权
            if (MapUtils.isNotEmpty(userAuthMap)) {
                for (String key : userAuthMap.keySet()) {
                    IrrAuthTaskLogEntity bean = new IrrAuthTaskLogEntity();
                    bean.setUserId(userId);
                    bean.setUserName(SecurityUtil.getUserName());
                    bean.setAuthId(key);
                    bean.setDealResult("取消授权");
                    User u = userMap.get(key);
                    if (u != null) {
                        bean.setAuthName(u.getUserName());
                    }
                    authLogList.add(bean);
                    remove(userAuthMap.get(key));
                }
            }
            //授权日志记录
            if (CollectionUtils.isNotEmpty(authLogList)) {
                irrAuthTaskLogService.add(authLogList);
            }
        }
        return IrrUtil.getMap(true, "保存成功！");
    }

    @Override
    public Map<String, Object> findAuth(String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return IrrUtil.getMap(false, "传入的授权人ID为空！");
        }
        //全部用户
        List<User> userList = userService.getRepository().findAll();
        if (CollectionUtils.isEmpty(userList)) {
            return IrrUtil.getMap(false, "没有用户！");
        }
        //当前授权用户
        QIrrAuthEntity qIrrAuth = QIrrAuthEntity.irrAuthEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qIrrAuth.userId.eq(userId));
        Iterator<IrrAuthEntity> selectIterator = repository.findAll(builder).iterator();
        Map<String, IrrAuthEntity> selectMap = new HashMap<String, IrrAuthEntity>();
        while (selectIterator.hasNext()) {
            IrrAuthEntity temp = selectIterator.next();
            selectMap.put(temp.getAuthId(), temp);
        }
        //循环分出用户
        List<User> selectList = new ArrayList<User>();
        List<User> noSelectList = new ArrayList<User>();
        for (User user : userList) {
            if (userId.equals(user.getId())) {
               continue; 
            }
            if (selectMap.get(user.getId()) != null) {
                selectList.add(user);
                continue;
            }
            noSelectList.add(user);
        }
        Map<String, Object> map = IrrUtil.getMap(true);
        map.put("select", selectList);
        map.put("noSelect", noSelectList);
        return map;
    }

    @Override
    public Boolean isAdmin(String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        String sql = "SELECT COUNT(DISTINCT R.FD_CODE) FROM FR_AA_USER_ROLE UR INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID\r\n"
                + "WHERE R.FD_ENABLE=1 AND R.FD_CODE IN ('" + IrrEnum.ADMIN_ROLE_CODE.getValue()
                + "','" + IrrEnum.IR_IT_ROLE_CODE.getValue() + "','" + IrrEnum.ROLE_IRR_ADMIN.getValue()
                + "') AND UR.FD_USER_ID='" + userId + "'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count > 0) {
            return true;
        }
        return false;
    }
    
    public Map<String, String> findAllUserIdAndName() {
        String sql = "SELECT DISTINCT FD_ID,FD_USERNAME FROM FR_AA_USER WHERE FD_ENABLE=1";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String, String> map = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> temp : list) {
                Object idObj = temp.get("FD_ID");
                Object userNameObj = temp.get("FD_USERNAME");
                if(IrrUtil.strObjectIsEmpty(idObj) || IrrUtil.strObjectIsEmpty(userNameObj)) {
                    continue;
                }
                map.put(idObj.toString(), userNameObj.toString());
            }
        }
        return map;
    }

}
