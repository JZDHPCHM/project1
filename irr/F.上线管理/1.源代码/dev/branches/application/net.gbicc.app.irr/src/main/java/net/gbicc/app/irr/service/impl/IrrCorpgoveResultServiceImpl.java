package net.gbicc.app.irr.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrCorpgoveResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrCorpgoveResultRepository;
import net.gbicc.app.irr.service.IrrCorpgoveResultService;

/**
*	公司治理结果
*/
@Service
public class IrrCorpgoveResultServiceImpl extends DaoServiceImpl<IrrCorpgoveResultEntity, String, IrrCorpgoveResultRepository> 
	implements IrrCorpgoveResultService {

}
