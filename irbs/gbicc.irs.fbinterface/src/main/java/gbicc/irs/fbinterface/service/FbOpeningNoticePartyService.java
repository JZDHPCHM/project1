package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticePartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticePartyRepository;

/**
 * 开庭公告当事人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbOpeningNoticePartyService extends DaoService<FbOpeningNoticePartyEntity, String, FbOpeningNoticePartyRepository>{

}
