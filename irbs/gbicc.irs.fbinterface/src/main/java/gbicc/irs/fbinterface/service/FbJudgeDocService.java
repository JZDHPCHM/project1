package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocRepository;
import net.sf.json.JSONObject;
/**
 * 裁判文书相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocService extends DaoService<FbJudgeDocEntity, String, FbJudgeDocRepository>{

    /**
     * 根据companyId获取裁判文书结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getJudgeDoc(String companyId) throws Exception;
    /**
     * 根据companyId获取裁判文书结果，递归获取所有
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionJudgeDoc(String companyId, String url, String pageId) throws Exception;
    /**
     * 封装结果
     *
     * @param jsonObject
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject jsonObject,String companyId) throws Exception;
}
