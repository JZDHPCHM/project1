package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbLitigationNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbLitigationNoticeRepository;
import net.sf.json.JSONObject;

/**
 * 涉诉公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public interface FbLitigationNoticeService extends DaoService<FbLitigationNoticeEntity, String, FbLitigationNoticeRepository>{

    /**
     * 根据companyId获取涉诉公告结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getLitigationNotice(String companyId) throws Exception;

    /**
     * 根据companyId获取涉诉公告结果，递归获取所有结果
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionLitigationNotice(String companyId, String url, String pageId) throws Exception;

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
