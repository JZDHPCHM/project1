package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbRatingRecordEntity;
import gbicc.irs.fbinterface.jpa.repository.FbRatingRecordRepository;

/**
 * 评级记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbRatingRecordService extends DaoService<FbRatingRecordEntity, String, FbRatingRecordRepository>{

    /**
     * 根据companyId获取评级记录结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getRatingRecord(String companyId) throws Exception;

}
