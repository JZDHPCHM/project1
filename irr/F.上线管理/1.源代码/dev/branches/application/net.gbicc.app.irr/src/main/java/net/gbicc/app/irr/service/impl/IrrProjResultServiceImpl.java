package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;
import net.gbicc.app.irr.service.IrrProjResultService;

/**
* 评估项目得分
*/
@Service
public class IrrProjResultServiceImpl extends DaoServiceImpl<IrrProjResultEntity, String, IrrProjResultRepository> 
	implements IrrProjResultService {

}
