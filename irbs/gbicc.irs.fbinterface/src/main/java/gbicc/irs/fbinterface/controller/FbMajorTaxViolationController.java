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

import gbicc.irs.fbinterface.jpa.entity.FbMajorTaxViolationEntity;
import gbicc.irs.fbinterface.jpa.repository.FbMajorTaxViolationRepository;
import gbicc.irs.fbinterface.service.FbMajorTaxViolationService;

/**
 * 重大税收违法相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Controller
@RequestMapping("/majorTaxViolation")
public class FbMajorTaxViolationController extends SmartClientRestfulCrudController<FbMajorTaxViolationEntity, String, FbMajorTaxViolationRepository, FbMajorTaxViolationService>{

    private static final Logger LOGGER = LogManager.getLogger(FbMajorTaxViolationController.class);
    /**
     * 根据companyId获取重大税收违法结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getMajorTaxViolation.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getMajorTaxViolation(String companyId){
        try {
            //3702112805341
            return service.getMajorTaxViolation(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
