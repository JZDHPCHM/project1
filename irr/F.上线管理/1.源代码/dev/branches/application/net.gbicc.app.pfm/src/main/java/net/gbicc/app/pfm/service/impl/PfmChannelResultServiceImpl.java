package net.gbicc.app.pfm.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;
import net.gbicc.app.pfm.service.PfmChannelResultService;

/**
* 渠道绩效结果
*/
@Service
public class PfmChannelResultServiceImpl extends DaoServiceImpl<PfmChannelResultEntity, String, PfmChannelResultRepository> 
	implements PfmChannelResultService {

}
