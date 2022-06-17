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

import gbicc.irs.fbinterface.jpa.entity.FbEquityPledgedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEquityPledgedRepository;
import gbicc.irs.fbinterface.service.FbEquityPledgedService;

/**
 * 股权出质操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
@Controller
@RequestMapping("/equityPledged")
public class FbEquityPledgedController extends SmartClientRestfulCrudController<FbEquityPledgedEntity, String, FbEquityPledgedRepository, FbEquityPledgedService>{

    private static final Logger LOGGER = LogManager.getLogger(FbEquityPledgedController.class);
    /**
     * 根据companyId获取股权出质结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getEquityPledgedInfo.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getEquityPledgedInfo(String companyId){
        try {
            //91310000781140339K
            long startTime = System.currentTimeMillis();
            
            Map<String, Object> resultMap = service.getEquityPledgedInfo(companyId);
            
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
