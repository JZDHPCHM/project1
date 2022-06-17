package org.wsp.framework.security.service;

public interface RoleSynchronousService {
	
	public Integer prepare() throws Exception;
	public Integer syncRole() throws Exception;
	public Integer complete() throws Exception;
	
}
