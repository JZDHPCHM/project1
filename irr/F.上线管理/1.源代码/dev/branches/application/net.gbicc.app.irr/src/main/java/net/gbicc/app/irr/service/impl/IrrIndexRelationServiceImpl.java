package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;
import net.gbicc.app.irr.service.IrrIndexRelationService;

/**
* 指标关联指标
*
*/
@Service
public class IrrIndexRelationServiceImpl extends DaoServiceImpl<IrrIndexRelationEntity, String, IrrIndexRelationRepository> 
	implements IrrIndexRelationService {
	
	
}
