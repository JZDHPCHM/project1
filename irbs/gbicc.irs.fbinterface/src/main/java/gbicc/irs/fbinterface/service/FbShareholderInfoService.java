package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderInfoRepository;

/**
 * 股东信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbShareholderInfoService extends DaoService<FbShareholderInfoEntity, String, FbShareholderInfoRepository>{

    /**
     * 根据companyId获取股东信息结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getShareholderInfo(String companyId) throws Exception;

    /**
     * 根据companyId获取股东信息结果，递归获取所有结果
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionShareholderInfo(String companyId, String url, String pageId) throws Exception;

}
