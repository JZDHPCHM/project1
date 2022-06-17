package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderSubscribedInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderSubscribedInfoRepository;
import gbicc.irs.fbinterface.service.FbShareholderSubscribedInfoService;

/**
 * 股东信息认缴明细相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbShareholderSubscribedInfoServiceImpl extends DaoServiceImpl<FbShareholderSubscribedInfoEntity, String, FbShareholderSubscribedInfoRepository> implements FbShareholderSubscribedInfoService{

}
