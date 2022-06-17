package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbPunishEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishRepository;
import net.sf.json.JSONObject;

/**
 * 行政处罚相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
public interface FbPunishService extends DaoService<FbPunishEntity, String, FbPunishRepository>{

    /**
     * 根据companyId获取行政处罚结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getPunish(String companyId) throws Exception;
    /**
     * 根据companyId获取行政处罚结果，递归获取所有结果
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionPunish(String companyId, String url, String pageId) throws Exception;
    
    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
