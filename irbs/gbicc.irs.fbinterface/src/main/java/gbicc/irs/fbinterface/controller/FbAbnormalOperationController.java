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

import gbicc.irs.fbinterface.jpa.entity.FbAbnormalOperationEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAbnormalOperationRepository;
import gbicc.irs.fbinterface.service.FbAbnormalOperationService;

/**
 * 经营异常相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Controller
@RequestMapping("/abnormalOperation")
public class FbAbnormalOperationController extends SmartClientRestfulCrudController<FbAbnormalOperationEntity, String, FbAbnormalOperationRepository, FbAbnormalOperationService>{

    private static final Logger LOGGER = LogManager.getLogger(FbAbnormalOperationController.class);
    /**
     * 根据companyId获取经营异常结果
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getAbnormalOperation.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAbnormalOperation(String companyId){
        try {
            //230103600769617
            return service.getAbnormalOperation(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
