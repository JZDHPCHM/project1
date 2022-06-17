package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessPartyRepository;
import gbicc.irs.fbinterface.service.FbTrialProcessPartyService;

/**
 * 审判流程当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbTrialProcessPartyServiceImpl extends DaoServiceImpl<FbTrialProcessPartyEntity, String, FbTrialProcessPartyRepository> implements FbTrialProcessPartyService{

}
