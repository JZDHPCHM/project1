package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocParagraphEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocParagraphRepository;
import gbicc.irs.fbinterface.service.FbJudgeDocParagraphService;
/**
 * 裁判文书段落相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocParagraphServiceImpl extends DaoServiceImpl<FbJudgeDocParagraphEntity, String, FbJudgeDocParagraphRepository> implements FbJudgeDocParagraphService{

}
