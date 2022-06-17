package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishPartyRepository;
import gbicc.irs.fbinterface.service.FbPunishPartyService;
/**
 * 行政处罚当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishPartyServiceImpl extends DaoServiceImpl<FbPunishPartyEntity, String, FbPunishPartyRepository> implements FbPunishPartyService{

}
