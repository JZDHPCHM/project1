package net.gbicc.app.pfm.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.pfm.jpa.entity.PfmResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmResultRepository;
import net.gbicc.app.pfm.service.PfmResultService;

/**
 * 绩效结果
 * 
 * @author FC
 * @version v1.0 2019年7月4日
 */
@Service
public class PfmResultServiceImpl extends DaoServiceImpl<PfmResultEntity, String, PfmResultRepository>
        implements PfmResultService {

}
