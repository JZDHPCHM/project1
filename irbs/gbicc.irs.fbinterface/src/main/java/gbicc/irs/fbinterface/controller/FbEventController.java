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

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyRepository;
import gbicc.irs.fbinterface.service.FbEventCompanyService;
/**
 * 事件检索相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Controller
@RequestMapping("/event")
public class FbEventController extends SmartClientRestfulCrudController<FbEventCompanyEntity, String, FbEventCompanyRepository, FbEventCompanyService>{

    private static final Logger LOGGER = LogManager.getLogger(FbEquityPledgedController.class);
    /**
     * 根据companyId获取事件检索结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getEvent.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getEvent(String companyId){
        try {
            //610131100000804
            return service.getEvent(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    
    /**
     * 根据companyId获取事件检索结果,递归获取所有结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionEvent.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionEvent(String companyId){
        try {
            //610131100000804
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getRecrusionEvent(companyId,"","",true);
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
