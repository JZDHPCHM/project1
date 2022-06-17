package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrStrariskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrStrariskResultRepository;
import net.gbicc.app.irr.service.IrrStrariskResultService;

/**
*	战略风险结果
*/
@Service
public class IrrStrariskResultServiceImpl extends DaoServiceImpl<IrrStrariskResultEntity, String, IrrStrariskResultRepository> 
	implements IrrStrariskResultService {

}
