package gbicc.irs.rti.service.impl;

import java.util.Map;

import javax.jdo.annotations.Transactional;

import gbicc.irs.commom.module.jpa.support.SpringUtil;
import gbicc.irs.rti.service.IProcess;

public class Process  implements IProcess {

	
	
	@Override
	@Transactional
	public Map<String, String> doProcess(Map<String, Object> map) throws Exception {
		RTIBpMasterServiceImpl bpService = (RTIBpMasterServiceImpl) SpringUtil.getBean("rTIService");
		return bpService.parsingData(map);
	}
}
