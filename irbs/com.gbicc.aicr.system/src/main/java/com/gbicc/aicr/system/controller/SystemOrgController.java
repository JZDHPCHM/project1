package com.gbicc.aicr.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.repository.OrgRepository;

import com.gbicc.aicr.system.service.SystemOrgService;
import com.gbicc.aicr.system.support.dto.JsTreeNodeDTO;

/**
 * 系统机构控制器
 * 
 * @author FC
 * @version v1.0 2019年6月20日
 */
@Controller
@RequestMapping("/system/org")
public class SystemOrgController
        extends BootstrapRestfulCrudController<Org, String, OrgRepository, SystemOrgService> {

    private static final Logger LOG = LogManager.getLogger(SystemOrgController.class);

    /**
     * 根据树的根ID查询机构树
     * 
     * @param id
     *            树的唯一ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/findOrg.action")
    public List<JsTreeNodeDTO> findOrg(String id) {
        List<JsTreeNodeDTO> list = new ArrayList<JsTreeNodeDTO>();
        try {
            list = service.findOrgJsTree(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }
        return list;
    }

    
    /**
     * 消息推送页
     *
     * @return
     */
    @RequestMapping("/msgPush.action")
    public String msgPush() {
        return "gbicc/aicr/view/msgPush.html";
    }
}
