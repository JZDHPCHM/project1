package net.gbicc.app.pfm.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;

/**
* 渠道绩效结果
*/
public interface PfmChannelResultService extends DaoService<PfmChannelResultEntity, String, PfmChannelResultRepository> {

}
