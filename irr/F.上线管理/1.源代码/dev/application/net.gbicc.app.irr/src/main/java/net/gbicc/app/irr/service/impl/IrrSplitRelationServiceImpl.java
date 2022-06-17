package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrSplitRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrSplitRelationRepository;
import net.gbicc.app.irr.service.IrrSplitRelationService;

/**
* 拆分指标关联service实现类
*
*/
@Service
public class IrrSplitRelationServiceImpl extends
		DaoServiceImpl<IrrSplitRelationEntity, String, IrrSplitRelationRepository> implements IrrSplitRelationService {

}
