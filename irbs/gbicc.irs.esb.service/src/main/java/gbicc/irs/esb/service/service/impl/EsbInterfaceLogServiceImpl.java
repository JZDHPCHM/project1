package gbicc.irs.esb.service.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.esb.service.entity.EsbInterfaceLog;
import gbicc.irs.esb.service.repository.EsbInterfaceLogRepository;
import gbicc.irs.esb.service.service.EsbInterfaceLogService;

@Service("EsbInterfaceLogServiceImpl")
public class EsbInterfaceLogServiceImpl extends DaoServiceImpl<EsbInterfaceLog, String, EsbInterfaceLogRepository>
		implements EsbInterfaceLogService {

}
