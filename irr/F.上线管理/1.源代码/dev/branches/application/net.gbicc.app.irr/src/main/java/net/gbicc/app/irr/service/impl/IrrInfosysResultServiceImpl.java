package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrInfosysResultRepository;
import net.gbicc.app.irr.service.IrrInfosysResultService;

/**
* 信息系统结果
*/
@Service
public class IrrInfosysResultServiceImpl extends DaoServiceImpl<IrrInfosysResultEntity, String, IrrInfosysResultRepository> 
	implements IrrInfosysResultService {

}
