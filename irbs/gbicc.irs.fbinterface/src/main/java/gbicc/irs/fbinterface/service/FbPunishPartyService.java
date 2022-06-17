package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbPunishPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishPartyRepository;
/**
 * 行政处罚当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
public interface FbPunishPartyService extends DaoService<FbPunishPartyEntity, String, FbPunishPartyRepository>{

}
