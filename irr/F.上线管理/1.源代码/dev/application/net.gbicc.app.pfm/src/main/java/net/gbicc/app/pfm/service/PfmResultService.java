package net.gbicc.app.pfm.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmResultRepository;

/**
 * 绩效结果
 * 
 * @author FC
 * @version v1.0 2019年7月4日
 */
public interface PfmResultService extends DaoService<PfmResultEntity, String, PfmResultRepository> {

}
