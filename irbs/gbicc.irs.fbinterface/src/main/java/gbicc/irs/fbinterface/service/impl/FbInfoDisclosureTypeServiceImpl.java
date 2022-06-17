package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureTypeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureTypeRepository;
import gbicc.irs.fbinterface.service.FbInfoDisclosureTypeService;

/**
 * 信息披露公告类别相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbInfoDisclosureTypeServiceImpl extends DaoServiceImpl<FbInfoDisclosureTypeEntity, String, FbInfoDisclosureTypeRepository> implements FbInfoDisclosureTypeService{

}
