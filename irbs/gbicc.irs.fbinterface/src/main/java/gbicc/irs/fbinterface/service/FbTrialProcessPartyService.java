package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessPartyRepository;

/**
 * 审判流程当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbTrialProcessPartyService extends DaoService<FbTrialProcessPartyEntity, String, FbTrialProcessPartyRepository>{

}
