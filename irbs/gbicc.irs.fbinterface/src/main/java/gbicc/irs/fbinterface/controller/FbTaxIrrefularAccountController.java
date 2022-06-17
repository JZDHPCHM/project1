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

import gbicc.irs.fbinterface.jpa.entity.FbTaxIrrefularAccountEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTaxIrrefularAccountRepository;
import gbicc.irs.fbinterface.service.FbTaxIrrefularAccountService;

/**
 * 税务非正常户相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Controller
@RequestMapping("/taxIrrefularAccount")
public class FbTaxIrrefularAccountController extends SmartClientRestfulCrudController<FbTaxIrrefularAccountEntity, String, FbTaxIrrefularAccountRepository, FbTaxIrrefularAccountService>{

    private static final Logger LOGGER = LogManager.getLogger(FbTaxIrrefularAccountController.class);
    /**
     * 根据companyId获取税务非正常户结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getTaxIrrefularAccount.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTaxIrrefularAccount(String companyId){
        try {
            //310107000738086
            return service.getTaxIrrefularAccount(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
