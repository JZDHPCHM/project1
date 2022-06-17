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

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureRepository;
import gbicc.irs.fbinterface.service.FbInfoDisclosureService;

/**
 * 信息披露相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Controller
@RequestMapping("/infoDisclosure")
public class FbInfoDisclosureController extends SmartClientRestfulCrudController<FbInfoDisclosureEntity, String, FbInfoDisclosureRepository, FbInfoDisclosureService>{

    private static final Logger LOGGER = LogManager.getLogger(FbInfoDisclosureController.class);
    /**
     * 根据companyId获取信息披露结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getInfoDisclosure.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getInfoDisclosure(String companyId){
        try {
            //91330000720084740E
            return service.getInfoDisclosure(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取信息披露结果，递归获取所有
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionInfoDisclosure.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionInfoDisclosure(String companyId){
        try {
            //91330000720084740E
            long startTime = System.currentTimeMillis();
            service.getRecrusionInfoDisclosure(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return AppUtil.getMap(true,"chenggong");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
