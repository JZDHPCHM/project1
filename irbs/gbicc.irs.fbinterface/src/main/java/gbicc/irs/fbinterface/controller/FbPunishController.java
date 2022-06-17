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

import gbicc.irs.fbinterface.jpa.entity.FbPunishEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishRepository;
import gbicc.irs.fbinterface.service.FbPunishService;
/**
 * 行政处罚相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Controller
@RequestMapping("/punish")
public class FbPunishController extends SmartClientRestfulCrudController<FbPunishEntity, String, FbPunishRepository, FbPunishService>{

    private static final Logger LOGGER = LogManager.getLogger(FbPunishController.class);
    /**
     * 根据companyId获取行政处罚结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getPunish.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getPunish(String companyId){
        try {
            //91440101671838701U
            return service.getPunish(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取行政处罚结果，递归获取所有结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionPunish.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionPunish(String companyId){
        try {
            //91440101671838701U
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getRecrusionPunish(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
