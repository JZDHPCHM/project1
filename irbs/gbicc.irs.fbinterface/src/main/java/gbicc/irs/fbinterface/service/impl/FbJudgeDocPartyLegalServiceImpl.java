package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyLegalEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyLegalRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocPartyLegalService;
/**
 * 裁判文书法定代表人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocPartyLegalServiceImpl extends DaoServiceImpl<FbJudgeDocPartyLegalEntity, String, FbJudgeDocPartyLegalRepository> implements FbJudgeDocPartyLegalService{

}
