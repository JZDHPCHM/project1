package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocSentenceRepository;
/**
 * 裁判文书判决金额相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocSentenceService extends DaoService<FbJudgeDocSentenceEntity, String, FbJudgeDocSentenceRepository>{

}
