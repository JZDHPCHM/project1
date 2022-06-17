package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyKeywordEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyKeywordRepository;
import gbicc.irs.fbinterface.service.FbEventCompanyKeywordService;
/**
 * 事件检索公司简称相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventCompanyKeywordServiceImpl extends DaoServiceImpl<FbEventCompanyKeywordEntity, String, FbEventCompanyKeywordRepository> implements FbEventCompanyKeywordService{

}
