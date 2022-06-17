package gbicc.irs.rti.service;

import java.util.Map;

public interface IProcess {
	public Map<String, String> doProcess(Map<String, Object> map) throws Exception;
}
