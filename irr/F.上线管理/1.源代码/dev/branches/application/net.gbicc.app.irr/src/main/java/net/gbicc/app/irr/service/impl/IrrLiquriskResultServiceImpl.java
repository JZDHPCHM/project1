package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrLiquriskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrLiquriskResultRepository;
import net.gbicc.app.irr.service.IrrLiquriskResultService;

/**
* 流动性风险结果
*/
@Service
public class IrrLiquriskResultServiceImpl extends DaoServiceImpl<IrrLiquriskResultEntity, String, IrrLiquriskResultRepository> 
	implements IrrLiquriskResultService {

}
