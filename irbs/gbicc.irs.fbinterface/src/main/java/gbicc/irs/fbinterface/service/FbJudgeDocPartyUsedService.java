package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyUsedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyUsedRepository;
/**
 * 裁判文书曾用名相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocPartyUsedService extends DaoService<FbJudgeDocPartyUsedEntity, String, FbJudgeDocPartyUsedRepository>{

}
