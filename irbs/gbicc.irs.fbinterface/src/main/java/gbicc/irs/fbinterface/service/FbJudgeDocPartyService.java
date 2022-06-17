package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocPartyRepository;
/**
 * 裁判文书当事人详情相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocPartyService extends DaoService<FbJudgeDocPartyEntity, String, FbJudgeDocPartyRepository>{

}
