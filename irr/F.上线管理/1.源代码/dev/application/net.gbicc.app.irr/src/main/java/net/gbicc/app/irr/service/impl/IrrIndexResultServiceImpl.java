package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexResultRepository;
import net.gbicc.app.irr.service.IrrIndexResultService;

/**
* 指标结果
*/
@Service
public class IrrIndexResultServiceImpl extends DaoServiceImpl<IrrIndexResultEntity, String, IrrIndexResultRepository> 
	implements IrrIndexResultService {

}
