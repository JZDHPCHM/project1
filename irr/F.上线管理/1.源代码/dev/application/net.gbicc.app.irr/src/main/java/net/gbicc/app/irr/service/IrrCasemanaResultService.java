package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrCasemanaResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrCasemanaResultRepository;

/**
* 案件管理结果
*/
public interface IrrCasemanaResultService extends DaoService<IrrCasemanaResultEntity, String, IrrCasemanaResultRepository> {

}
