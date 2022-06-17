package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosurePartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosurePartyRepository;
import gbicc.irs.fbinterface.service.FbInfoDisclosurePartyService;
/**
 * 信息披露当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbInfoDisclosurePartyServiceImpl extends DaoServiceImpl<FbInfoDisclosurePartyEntity, String, FbInfoDisclosurePartyRepository> implements FbInfoDisclosurePartyService{

}
