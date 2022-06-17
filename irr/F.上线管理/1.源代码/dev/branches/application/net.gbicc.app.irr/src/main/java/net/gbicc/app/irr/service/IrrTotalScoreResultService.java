package net.gbicc.app.irr.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrTotalScoreResultRepository;

/**
* 总分多维度结果
*/
public interface IrrTotalScoreResultService extends DaoService<IrrTotalScoreResultEntity, String, IrrTotalScoreResultRepository> {

}
