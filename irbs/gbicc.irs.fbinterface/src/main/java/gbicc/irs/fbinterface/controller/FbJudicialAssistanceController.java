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

import gbicc.irs.fbinterface.jpa.entity.FbJudicialAssistanceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudicialAssistanceRepository;
import gbicc.irs.fbinterface.service.FbJudicialAssistanceService;

/**
 * 司法协助相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Controller
@RequestMapping("/judicialAssistance")
public class FbJudicialAssistanceController extends SmartClientRestfulCrudController<FbJudicialAssistanceEntity, String, FbJudicialAssistanceRepository, FbJudicialAssistanceService>{

    private static final Logger LOGGER = LogManager.getLogger(FbJudicialAssistanceController.class);
    /**
     * 根据companyId获取司法协助结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getJudicialAssistance.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getJudicialAssistance(String companyId){
        try {
            //310110000564277
            return service.getJudicialAssistance(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
