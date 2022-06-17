package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportRepository;
/**
 * 年报相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportService extends DaoService<FbAnnualReportEntity, String, FbAnnualReportRepository>{

    /**
     * 根据companyId获取年报结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getAnnualReport(String companyId) throws Exception;
    
    /**
     * 根据companyId获取年报结果，翻页递归获取所有
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionAnnualReport(String companyId,String url,String pageId) throws Exception;

}
