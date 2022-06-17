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

import gbicc.irs.fbinterface.jpa.entity.FbIndustrialChangeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbIndustrialChangerRepository;
import gbicc.irs.fbinterface.service.FbIndustrialChangeService;

/**
 * 工商变更相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Controller
@RequestMapping("/industrialChange")
public class FbIndustrialChangeController extends SmartClientRestfulCrudController<FbIndustrialChangeEntity, String, FbIndustrialChangerRepository, FbIndustrialChangeService>{

    private static final Logger LOGGER = LogManager.getLogger(FbIndustrialChangeController.class);
    /**
     * 根据companyId获取工商变更结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getIndustrialChangeInfo.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getIndustrialChangeInfo(String companyId){
        try {
            //91310000781140339K
            return service.getIndustrialChangeInfo(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
