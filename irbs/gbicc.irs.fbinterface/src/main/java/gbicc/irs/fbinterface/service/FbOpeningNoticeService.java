package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticeRepository;
import net.sf.json.JSONObject;

/**
 * 开庭公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbOpeningNoticeService extends DaoService<FbOpeningNoticeEntity, String, FbOpeningNoticeRepository>{

    /**
     * 根据companyId获取开庭公告结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getOpeningNotice(String companyId) throws Exception;

    /**
     * 根据companyId获取开庭公告结果，递归获取所有结果
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionOpeningNotice(String companyId, String url, String pageId) throws Exception;

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
