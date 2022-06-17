package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderSubscribedInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderSubscribedInfoRepository;

/**
 * 股东信息认缴明细相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbShareholderSubscribedInfoService extends DaoService<FbShareholderSubscribedInfoEntity, String, FbShareholderSubscribedInfoRepository>{

}
