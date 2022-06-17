package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyDetailRepository;
import gbicc.irs.fbinterface.service.FbEventCompanyDetailService;
/**
 * 事件检索公司实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventCompanyDetailServiceImpl extends DaoServiceImpl<FbEventCompanyDetailEntity, String, FbEventCompanyDetailRepository> implements FbEventCompanyDetailService{

}
