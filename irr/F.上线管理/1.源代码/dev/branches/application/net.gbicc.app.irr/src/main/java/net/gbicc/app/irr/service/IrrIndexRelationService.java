package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;

/**
* 指标关联指标
*
*/
public interface IrrIndexRelationService extends DaoService<IrrIndexRelationEntity, String, IrrIndexRelationRepository> {

}
