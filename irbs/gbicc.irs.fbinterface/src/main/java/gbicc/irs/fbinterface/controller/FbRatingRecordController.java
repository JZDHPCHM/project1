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

import gbicc.irs.fbinterface.jpa.entity.FbRatingRecordEntity;
import gbicc.irs.fbinterface.jpa.repository.FbRatingRecordRepository;
import gbicc.irs.fbinterface.service.FbRatingRecordService;

/**
 * 评级记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Controller
@RequestMapping("/ratingRecord")
public class FbRatingRecordController extends SmartClientRestfulCrudController<FbRatingRecordEntity, String, FbRatingRecordRepository, FbRatingRecordService>{

    private static final Logger LOGGER = LogManager.getLogger(FbRatingRecordController.class);
    /**
     * 根据companyId获取评级记录结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRatingRecord.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRatingRecord(String companyId){
        try {
            //91110000710925462X
            return service.getRatingRecord(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
