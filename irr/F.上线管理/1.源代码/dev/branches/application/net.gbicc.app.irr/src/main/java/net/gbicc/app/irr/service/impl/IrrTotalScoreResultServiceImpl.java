package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrTotalScoreResultRepository;
import net.gbicc.app.irr.service.IrrTotalScoreResultService;

/**
* 总分多维度结果
*/
@Service
public class IrrTotalScoreResultServiceImpl extends DaoServiceImpl<IrrTotalScoreResultEntity, String, IrrTotalScoreResultRepository> 
	implements IrrTotalScoreResultService {


}
