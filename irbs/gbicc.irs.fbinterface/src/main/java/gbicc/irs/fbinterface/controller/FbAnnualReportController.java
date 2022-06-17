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

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportService;
/**
 * 年报相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Controller
@RequestMapping("/annualReport")
public class FbAnnualReportController extends SmartClientRestfulCrudController<FbAnnualReportEntity, String, FbAnnualReportRepository, FbAnnualReportService>{

    private static final Logger LOGGER = LogManager.getLogger(FbAnnualReportController.class);
    /**
     * 根据companyId获取年报结果
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getAnnualReport.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAnnualReport(String companyId){
        try {
            //91310000781140339K
            return service.getAnnualReport(companyId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
    
    /**
     * 根据companyId获取年报结果，翻页递归获取
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value="/getRecrusionAnnualReport.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getRecrusionAnnualReport(String companyId){
        try {
            //91310000781140339K
            long startTime = System.currentTimeMillis();
            Map<String, Object> map = service.getRecrusionAnnualReport(companyId,"","");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            return map;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,"获取失败"+e.getMessage());
        }
    }
}
