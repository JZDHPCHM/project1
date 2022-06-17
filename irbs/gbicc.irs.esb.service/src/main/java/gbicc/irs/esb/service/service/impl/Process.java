package gbicc.irs.esb.service.service.impl;

import java.util.Map;

import javax.jdo.annotations.Transactional;

import gbicc.irs.commom.module.jpa.support.SpringUtil;
import gbicc.irs.esb.service.service.IProcess;
import gbicc.irs.main.rating.service.impl.BpMasterServiceImpl;

public class Process  implements IProcess {

	
	
	@Override
	@Transactional
	public Map<String, String> doProcess(Map<String, Object> map) throws Exception {
		BpMasterServiceImpl bpService = (BpMasterServiceImpl) SpringUtil.getBean("clockDictionary");
		return bpService.parsingData(map);
	}
}
