package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexResultRepository;

/**
* 指标结果
*/
public interface IrrIndexResultService extends DaoService<IrrIndexResultEntity, String, IrrIndexResultRepository> {

}
