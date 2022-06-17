package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrCasemanaResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrCasemanaResultRepository;
import net.gbicc.app.irr.service.IrrCasemanaResultService;

/**
* 案件管理结果
*/
@Service
public class IrrCasemanaResultServiceImpl extends DaoServiceImpl<IrrCasemanaResultEntity, String, IrrCasemanaResultRepository> 
	implements IrrCasemanaResultService {
	
	
}
