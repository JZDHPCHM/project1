package net.gbicc.app.pfm.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;
import net.gbicc.app.pfm.service.PfmBranchResultService;

/**
* 分公司绩效结果
*/
@Service
public class PfmBranchResultServiceImpl extends DaoServiceImpl<PfmBranchResultEntity, String, PfmBranchResultRepository> 
	implements PfmBranchResultService {


}
