package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrLiquriskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrLiquriskResultRepository;

/**
* 流动性风险结果
*/
public interface IrrLiquriskResultService extends DaoService<IrrLiquriskResultEntity, String, IrrLiquriskResultRepository> {

}
