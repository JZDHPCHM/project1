package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbPunishReasonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishReasonRepository;

/**
 * 行政处罚处罚事由相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
public interface FbPunishReasonService extends DaoService<FbPunishReasonEntity, String, FbPunishReasonRepository>{

}
