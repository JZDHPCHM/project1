package gbicc.irs.esb.service.service;

import java.util.Map;

public interface IProcess {
	public Map<String, String> doProcess(Map<String, Object> map) throws Exception;
}
