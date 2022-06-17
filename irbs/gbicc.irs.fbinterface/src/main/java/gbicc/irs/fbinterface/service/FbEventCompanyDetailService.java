package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyDetailRepository;
/**
 * 事件检索公司实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventCompanyDetailService extends DaoService<FbEventCompanyDetailEntity, String, FbEventCompanyDetailRepository>{

}
