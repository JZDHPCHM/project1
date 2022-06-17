package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyUsedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyUsedRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocPartyUsedService;
/**
 * 裁判文书曾用名相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocPartyUsedServiceImpl extends DaoServiceImpl<FbJudgeDocPartyUsedEntity, String, FbJudgeDocPartyUsedRepository> implements FbJudgeDocPartyUsedService{

}
