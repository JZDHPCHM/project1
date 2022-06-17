package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;

/**
* 评估项目得分
*/
public interface IrrProjResultService extends DaoService<IrrProjResultEntity, String, IrrProjResultRepository> {

}
