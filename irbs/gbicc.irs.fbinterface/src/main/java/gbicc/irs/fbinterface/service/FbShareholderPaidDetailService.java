package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderPaidDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderPaidDetailRepository;

/**
 * 股东信息实缴明细相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbShareholderPaidDetailService extends DaoService<FbShareholderPaidDetailEntity, String, FbShareholderPaidDetailRepository>{

}
