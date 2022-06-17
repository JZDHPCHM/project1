package org.wsp.framework.security.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OuthLoginService {

	public Integer outhLogin(HttpServletRequest request, HttpServletResponse response);
	
	public Object authorize(HttpServletRequest request, HttpServletResponse response);
}
