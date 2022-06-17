package org.wsp.framework.security.impl.jpa.installer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.wsp.framework.core.installer.AbstractInstallerItem;

@Component("frAdministratorInstallerItem")
public class AdministratorInstallerItem extends AbstractInstallerItem implements Ordered{
	public static final int ORDER =Ordered.HIGHEST_PRECEDENCE+3000;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Override
	public int getOrder() {
		return ORDER;
	}
	
	@Override
	public boolean isEnable() {
		return false;
	}

	@Override
	public String getTemplateLoaction() {
		return "org/wsp/framework/security/impl/jpa/view/installer_administrator.html";
	}
	
	@Override
	public void validate(Map<String, String> config) throws Exception {
		if(!ObjectUtils.isEmpty(config)){
			String admin_username =config.get("admin_username");
			String admin_password =config.get("admin_password");
			if(ObjectUtils.isEmpty(admin_username)){
				throw new Exception("User Name of Administrator is Empty!"); 
			}
			if(ObjectUtils.isEmpty(admin_password)){
				throw new Exception("Password of Administrator is Empty!"); 
			}
		}
	}
	
	@Override
	public Map<String, String> prepareDefaultConfig() {
		Map<String,String> result =new HashMap<String,String>();
		result.put("admin_username", "admin");
		result.put("admin_password", "admin");
		return result;
	}
	
	@Override
	public Map<String, Object> prepareUiModel() {
		Map<String,Object> result =new HashMap<String,Object>();
		result.put("admin_username", "admin");
		result.put("admin_password", "admin");
		return result;
	}

	@Override
	public Map<String, String> prepareDatabasePlaceholderReplacements(Map<String,String> config) {
		Map<String, String> result =new HashMap<String,String>();
		result.put("adminUsername", config.get("admin_username"));
		result.put("adminPassword", passwordEncoder.encode(config.get("admin_password")));
		return result;
	}
	
	@Override
	public int getExecuteTimeWeight() {
		return 10;
	}
}
