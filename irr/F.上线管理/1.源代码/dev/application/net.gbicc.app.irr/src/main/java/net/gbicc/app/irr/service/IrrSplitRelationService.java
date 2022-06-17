package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrSplitRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrSplitRelationRepository;

/**
* 拆分指标service接口
*
*/
public interface IrrSplitRelationService
		extends DaoService<IrrSplitRelationEntity, String, IrrSplitRelationRepository> {

}
