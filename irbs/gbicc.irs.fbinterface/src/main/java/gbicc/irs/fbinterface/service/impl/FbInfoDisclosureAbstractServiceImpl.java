package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureAbstractEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureAbstractRepository;
import gbicc.irs.fbinterface.service.FbInfoDisclosureAbstractService;
/**
 * 信息披露摘要相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbInfoDisclosureAbstractServiceImpl extends DaoServiceImpl<FbInfoDisclosureAbstractEntity, String, FbInfoDisclosureAbstractRepository> implements FbInfoDisclosureAbstractService{

}
