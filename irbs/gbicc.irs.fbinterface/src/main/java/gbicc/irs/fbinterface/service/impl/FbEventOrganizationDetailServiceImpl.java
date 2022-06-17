package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventOrganizationDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventOrganizationDetailRepository;
import gbicc.irs.fbinterface.service.FbEventOrganizationDetailService;
/**
 * 事件检索组织实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventOrganizationDetailServiceImpl extends DaoServiceImpl<FbEventOrganizationDetailEntity, String, FbEventOrganizationDetailRepository> implements FbEventOrganizationDetailService{

}
