package net.gbicc.app.pfm.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;

/**
* 部门绩效结果
*/
public interface PfmDepaResultService extends DaoService<PfmDepaResultEntity, String, PfmDepaResultRepository> {

}
