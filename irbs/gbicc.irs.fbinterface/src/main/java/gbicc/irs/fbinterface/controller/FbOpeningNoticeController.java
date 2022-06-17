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

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticeRepository;
import gbicc.irs.fbinterface.service.FbOpeningNoticeService;

/**
 * 开庭公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Controller
@RequestMapping("/openingNotice")
public class FbOpeningNoticeController extends SmartClientRestfulCrudController<FbOpeningNoticeEntity, String, FbOpeningNoticeRepository, FbOpeningNoticeService>{

    private static final Logger LOGGER = LogManager.getLogger(FbOpeningNoticeController.class);
    /**
     * 根据companyId获取开庭公告结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getOpeningNotice.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getOpeningNotice(String companyId){
        try {
            //210200000021891
            return service.getOpeningNotice(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取开庭公告结果，递归获取所有结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionOpeningNotice.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionOpeningNotice(String companyId){
        try {
            //210200000021891
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getRecrusionOpeningNotice(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
