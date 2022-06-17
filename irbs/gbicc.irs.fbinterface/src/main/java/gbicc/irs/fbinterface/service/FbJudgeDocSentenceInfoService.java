package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocSentenceInfoRepository;
/**
 * 判决文书判决金额给付方
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocSentenceInfoService extends DaoService<FbJudgeDocSentenceInfoEntity, String, FbJudgeDocSentenceInfoRepository>{

}
