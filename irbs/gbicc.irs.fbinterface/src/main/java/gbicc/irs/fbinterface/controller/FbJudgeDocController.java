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

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocService;
/**
 * 裁判文书相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Controller
@RequestMapping("/judgeDoc")
public class FbJudgeDocController extends SmartClientRestfulCrudController<FbJudgeDocEntity, String, FbJudgeDocRepository, FbJudgeDocService>{

    private static final Logger LOGGER = LogManager.getLogger(FbInfoDisclosureController.class);
    /**
     * 根据companyId获取裁判文书结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getJudgeDoc.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getJudgeDoc(String companyId){
        try {
            //9144030027939873X7
            return service.getJudgeDoc(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    /**
     * 根据companyId获取裁判文书结果，递归获取所有
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionJudgeDoc.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionJudgeDoc(String companyId){
        try {
            //9144030027939873X7
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = service.getRecrusionJudgeDoc(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
