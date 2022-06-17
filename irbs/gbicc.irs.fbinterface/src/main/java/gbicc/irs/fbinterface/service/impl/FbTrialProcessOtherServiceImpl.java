package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessOtherEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessOtherRepository;
import gbicc.irs.fbinterface.service.FbTrialProcessOtherService;

/**
 * 审判流程其他角色相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbTrialProcessOtherServiceImpl extends DaoServiceImpl<FbTrialProcessOtherEntity, String, FbTrialProcessOtherRepository> implements FbTrialProcessOtherService{

}
