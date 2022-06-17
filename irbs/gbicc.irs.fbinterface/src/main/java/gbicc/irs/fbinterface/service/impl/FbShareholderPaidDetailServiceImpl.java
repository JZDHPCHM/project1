package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderPaidDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderPaidDetailRepository;
import gbicc.irs.fbinterface.service.FbShareholderPaidDetailService;

/**
 * 股东信息实缴明细相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbShareholderPaidDetailServiceImpl extends DaoServiceImpl<FbShareholderPaidDetailEntity, String, FbShareholderPaidDetailRepository> implements FbShareholderPaidDetailService{

}
