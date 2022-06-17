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

import gbicc.irs.fbinterface.jpa.entity.FbOverdueTaxCallEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOverdueTaxCallReposity;
import gbicc.irs.fbinterface.service.FbOverdueTaxCallService;

/**
 * 催缴/欠税相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Controller
@RequestMapping("/overdueTaxCall")
public class FbOverdueTaxCallController extends SmartClientRestfulCrudController<FbOverdueTaxCallEntity, String, FbOverdueTaxCallReposity, FbOverdueTaxCallService>{

    private static final Logger LOGGER = LogManager.getLogger(FbOverdueTaxCallController.class);
    /**
     * 根据companyId获取催缴/欠税结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getOverdueTaxCall.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getOverdueTaxCall(String companyId){
        try {
            //9144030027939873X7
            return service.getOverdueTaxCall(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
