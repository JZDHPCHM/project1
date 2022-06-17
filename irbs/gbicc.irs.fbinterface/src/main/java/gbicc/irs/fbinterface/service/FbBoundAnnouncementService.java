package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbBoundAnnouncementEntity;
import gbicc.irs.fbinterface.jpa.repository.FbBoundAnnouncementRepository;
import net.sf.json.JSONObject;

/**
 * 债券公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbBoundAnnouncementService extends DaoService<FbBoundAnnouncementEntity, String, FbBoundAnnouncementRepository>{

    /**
     * 根据companyId获取债券公告结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getBoundAnnouncement(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbBoundAnnouncementEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
