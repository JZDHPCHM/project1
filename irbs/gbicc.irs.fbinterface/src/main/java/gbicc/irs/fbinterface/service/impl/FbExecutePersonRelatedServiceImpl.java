package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbExecutePersonRelatedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbExecutePersonRelatedRepository;
import gbicc.irs.fbinterface.service.FbExecutePersonRelatedService;

/**
 * 被执行人相关信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbExecutePersonRelatedServiceImpl extends DaoServiceImpl<FbExecutePersonRelatedEntity, String, FbExecutePersonRelatedRepository> implements FbExecutePersonRelatedService{

}
