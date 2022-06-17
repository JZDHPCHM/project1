package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocPartyService;
/**
 * 裁判文书当事人详情相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocPartyServiceImpl extends DaoServiceImpl<FbJudgeDocPartyEntity, String, FbJudgeDocPartyRepository> implements FbJudgeDocPartyService{

}
