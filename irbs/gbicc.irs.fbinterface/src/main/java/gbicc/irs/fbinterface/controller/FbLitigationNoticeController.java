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

import gbicc.irs.fbinterface.jpa.entity.FbLitigationNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbLitigationNoticeRepository;
import gbicc.irs.fbinterface.service.FbLitigationNoticeService;

/**
 * 涉诉公告相关信息
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Controller
@RequestMapping("/litigationNotice")
public class FbLitigationNoticeController extends SmartClientRestfulCrudController<FbLitigationNoticeEntity, String, FbLitigationNoticeRepository, FbLitigationNoticeService>{

    private static final Logger LOGGER = LogManager.getLogger(FbLitigationNoticeController.class);
    /**
     * 根据companyId获取涉诉公告结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getLitigationNotice.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getLitigationNotice(String companyId){
        try {
            //91120116697414499T
            return service.getLitigationNotice(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    
    /**
     * 根据companyId获取涉诉公告结果，递归获取所有结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionLitigationNotice.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionLitigationNotice(String companyId){
        try {
            //91120116697414499T
            return service.getRecrusionLitigationNotice(companyId,"","");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }

}
