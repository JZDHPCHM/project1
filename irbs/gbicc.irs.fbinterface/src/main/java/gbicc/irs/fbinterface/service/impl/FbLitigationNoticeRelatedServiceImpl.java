package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbLitigationNoticeRelatedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbLitigationNoticeRelatedRepository;
import gbicc.irs.fbinterface.service.FbLitigationNoticeRelatedService;

/**
 * 涉诉公告相关信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Service
public class FbLitigationNoticeRelatedServiceImpl extends DaoServiceImpl<FbLitigationNoticeRelatedEntity, String, FbLitigationNoticeRelatedRepository> implements FbLitigationNoticeRelatedService{

}
