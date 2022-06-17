package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventOrganizationDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventOrganizationDetailRepository;
/**
 * 事件检索组织实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventOrganizationDetailService extends DaoService<FbEventOrganizationDetailEntity, String, FbEventOrganizationDetailRepository>{

}
