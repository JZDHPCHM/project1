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

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessRepository;
import gbicc.irs.fbinterface.service.FbTrialProcessService;

/**
 * 审判流程相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Controller
@RequestMapping("/trialProcess")
public class FbTrialProcessController extends SmartClientRestfulCrudController<FbTrialProcessEntity, String, FbTrialProcessRepository, FbTrialProcessService>{

    private static final Logger LOGGER = LogManager.getLogger(FbTrialProcessController.class);
    /**
     * 根据companyId获取审判流程结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getTrialProcess.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTrialProcess(String companyId){
        try {
            //91440300715201631K
            return service.getTrialProcess(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取审判流程结果，递归获取所有
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionTrialProcess.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionTrialProcess(String companyId){
        try {
            //91440300715201631K
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getRecrusionTrialProcess(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
