package gbicc.irs.fbinterface.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbBoundAnnouncementEntity;
import gbicc.irs.fbinterface.jpa.repository.FbBoundAnnouncementRepository;
import gbicc.irs.fbinterface.service.FbBoundAnnouncementService;

/**
 * 债券公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Controller
@RequestMapping("/boundAnnouncement")
public class FbBoundAnnouncementController extends SmartClientRestfulCrudController<FbBoundAnnouncementEntity, String, FbBoundAnnouncementRepository, FbBoundAnnouncementService>{

    private static final Logger LOGGER = LogManager.getLogger(FbBoundAnnouncementController.class);
    /**
     * 根据companyId获取债券公告结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getBoundAnnouncement.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getBoundAnnouncement(String companyId){
        try {
            //91110000710925462X
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getBoundAnnouncement(companyId);
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
