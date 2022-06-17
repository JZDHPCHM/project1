package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureAbstractEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureAbstractRepository;
/**
 * 信息披露摘要相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbInfoDisclosureAbstractService extends DaoService<FbInfoDisclosureAbstractEntity, String, FbInfoDisclosureAbstractRepository>{

}
