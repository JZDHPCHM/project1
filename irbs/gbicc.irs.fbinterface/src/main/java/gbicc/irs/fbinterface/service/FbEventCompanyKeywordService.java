package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyKeywordEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyKeywordRepository;
/**
 * 事件检索公司简称相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventCompanyKeywordService extends DaoService<FbEventCompanyKeywordEntity, String, FbEventCompanyKeywordRepository>{

}
