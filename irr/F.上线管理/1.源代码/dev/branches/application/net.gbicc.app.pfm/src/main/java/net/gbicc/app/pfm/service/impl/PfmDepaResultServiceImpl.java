package net.gbicc.app.pfm.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;
import net.gbicc.app.pfm.service.PfmDepaResultService;

/**
* 部门绩效结果
*/
@Service
public class PfmDepaResultServiceImpl extends DaoServiceImpl<PfmDepaResultEntity, String, PfmDepaResultRepository> 
	implements PfmDepaResultService {

}
