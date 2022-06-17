package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocSentenceRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocSentenceService;
/**
 * 裁判文书判决金额相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocSentenceServiceImpl extends DaoServiceImpl<FbJudgeDocSentenceEntity, String, FbJudgeDocSentenceRepository> implements FbJudgeDocSentenceService{

}
