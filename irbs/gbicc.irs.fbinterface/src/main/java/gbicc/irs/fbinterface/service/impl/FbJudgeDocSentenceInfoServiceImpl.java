package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocSentenceInfoRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocSentenceInfoService;
/**
 * 裁判文书判决金额给付方
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocSentenceInfoServiceImpl extends DaoServiceImpl<FbJudgeDocSentenceInfoEntity, String, FbJudgeDocSentenceInfoRepository> implements FbJudgeDocSentenceInfoService{

}
