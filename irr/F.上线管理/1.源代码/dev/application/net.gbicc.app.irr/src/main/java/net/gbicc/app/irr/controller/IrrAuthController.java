package net.gbicc.app.irr.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrAuthEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthRepository;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrAuthService;

/**
 * 授权管理
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Controller
@RequestMapping("/irr/auth")
public class IrrAuthController
        extends BootstrapRestfulCrudController<IrrAuthEntity, String, IrrAuthRepository, IrrAuthService> {

    private static final Logger LOG = LogManager.getLogger(IrrAuthController.class);
    
    /**
     * 保存授权
     *
     * @param data
     *            授权数据
     * @param userId
     *            授权人ID
     * @return
     */
    @ResponseBody
    @RequestMapping("saveAuth.action")
    public Map<String, Object> saveAuth(String data, String userId) {
        try {
            return service.saveAuth(data, userId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return IrrUtil.getMap(false, e.getMessage());
        }
    }

    /**
     * 查询授权
     *
     * @param userId
     *            授权人ID
     * @return
     */
    @ResponseBody
    @RequestMapping("findAuth.action")
    public Map<String, Object> findAuth(String userId) {
        try {
            return service.findAuth(userId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return IrrUtil.getMap(false, e.getMessage());
        }
    }

}
