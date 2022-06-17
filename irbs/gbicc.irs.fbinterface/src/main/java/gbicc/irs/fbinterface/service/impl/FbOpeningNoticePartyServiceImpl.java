package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticePartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticePartyRepository;
import gbicc.irs.fbinterface.service.FbOpeningNoticePartyService;

/**
 * 开庭公告当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbOpeningNoticePartyServiceImpl extends DaoServiceImpl<FbOpeningNoticePartyEntity, String, FbOpeningNoticePartyRepository> implements FbOpeningNoticePartyService{

}
