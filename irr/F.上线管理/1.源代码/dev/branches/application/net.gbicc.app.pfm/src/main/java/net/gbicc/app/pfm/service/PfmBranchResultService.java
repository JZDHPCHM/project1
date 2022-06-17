package net.gbicc.app.pfm.service;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;

/** 
* 分公司绩效
*/
public interface PfmBranchResultService extends DaoService<PfmBranchResultEntity, String, PfmBranchResultRepository> {

}
