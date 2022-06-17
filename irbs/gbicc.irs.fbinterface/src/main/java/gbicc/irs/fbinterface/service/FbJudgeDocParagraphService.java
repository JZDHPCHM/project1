package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocParagraphEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocParagraphRepository;
/**
 * 裁判文书段落相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbJudgeDocParagraphService extends DaoService<FbJudgeDocParagraphEntity, String, FbJudgeDocParagraphRepository>{

}
