package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyRoleEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyRoleRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocPartyRoleService;
/**
 * 裁判文书角色相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocPartyRoleServiceImpl extends DaoServiceImpl<FbJudgeDocPartyRoleEntity, String, FbJudgeDocPartyRoleRepository> implements FbJudgeDocPartyRoleService{

}
