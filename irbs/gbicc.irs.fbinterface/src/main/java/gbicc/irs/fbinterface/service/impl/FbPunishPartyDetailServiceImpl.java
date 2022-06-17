package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishPartyDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishPartyDetailRepository;
import gbicc.irs.fbinterface.service.FbPunishPartyDetailService;
/**
 * 行政处罚当事人处罚详情相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishPartyDetailServiceImpl extends DaoServiceImpl<FbPunishPartyDetailEntity, String, FbPunishPartyDetailRepository> implements FbPunishPartyDetailService{

}
