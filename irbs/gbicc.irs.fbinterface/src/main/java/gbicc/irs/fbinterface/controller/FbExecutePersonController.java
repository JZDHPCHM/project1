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

import gbicc.irs.fbinterface.jpa.entity.FbExecutePersonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbExecutePersonRepository;
import gbicc.irs.fbinterface.service.FbExecutePersonService;

/**
 * 被执行人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Controller
@RequestMapping("/executePerson")
public class FbExecutePersonController extends SmartClientRestfulCrudController<FbExecutePersonEntity, String, FbExecutePersonRepository, FbExecutePersonService>{

    private static final Logger LOGGER = LogManager.getLogger(FbExecutePersonController.class);
    /**
     * 根据companyId获取被执行人结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getExecutePerson.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getExecutePerson(String companyId){
        try {
            //9144030027939873X7
            return service.getExecutePerson(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    
    /**
     * 根据companyId获取被执行人结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionExecutePerson.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionExecutePerson(String companyId){
        try {
            //9144030027939873X7
            long startTime = System.currentTimeMillis();
            Map<String, Object> map = service.getRecrusionExecutePerson(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return map;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
