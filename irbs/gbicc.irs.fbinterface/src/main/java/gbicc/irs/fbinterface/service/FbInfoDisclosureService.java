package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureRepository;
import net.sf.json.JSONObject;
/**
 * 信息披露相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbInfoDisclosureService extends DaoService<FbInfoDisclosureEntity, String, FbInfoDisclosureRepository>{

    /**
     * 根据companyId获取信息披露结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getInfoDisclosure(String companyId) throws Exception;
    /**
     * 根据companyId获取信息披露结果，递归获取所有
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionInfoDisclosure(String companyId, String url, String pageId) throws Exception;
    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;


}
