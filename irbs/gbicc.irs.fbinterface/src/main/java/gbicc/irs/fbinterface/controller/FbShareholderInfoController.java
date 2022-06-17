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

import gbicc.irs.fbinterface.jpa.entity.FbShareholderInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderInfoRepository;
import gbicc.irs.fbinterface.service.FbShareholderInfoService;

/**
 * 股东信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Controller
@RequestMapping("/shareholderInfo")
public class FbShareholderInfoController extends SmartClientRestfulCrudController<FbShareholderInfoEntity, String, FbShareholderInfoRepository, FbShareholderInfoService>{

    private static final Logger LOGGER = LogManager.getLogger(FbShareholderInfoController.class);
    /**
     * 根据companyId获取股东信息结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getShareholderInfo.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getShareholderInfo(String companyId){
        try {
            //91310000781140339K
            return service.getShareholderInfo(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取股东信息结果，递归获取所有结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionShareholderInfo.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionShareholderInfo(String companyId){
        try {
            //91310000781140339K
            long startTime = System.currentTimeMillis();
            Map<String, Object> returnMap = service.getRecrusionShareholderInfo(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return returnMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
