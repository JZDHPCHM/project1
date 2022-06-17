package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyOtherEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyOtherRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocPartyOtherService;
/**
 * 裁判文书其他角色相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocPartyOtherServiceImpl extends DaoServiceImpl<FbJudgeDocPartyOtherEntity, String, FbJudgeDocPartyOtherRepository> implements FbJudgeDocPartyOtherService{

}
