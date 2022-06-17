package com.gbicc.aicr.system.dto;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {
	private static final long serialVersionUID = 145325L;
	
	private String loginName;
	private String userName;
	private String roleCode;
	private String roleName;
	private String depCode;
	private String depName;
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDepCode() {
		return depCode;
	}
	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@Override
	public String toString() {
		return "UserInfoDTO [loginName=" + loginName + ", userName=" + userName + ", roleCode=" + roleCode
				+ ", roleName=" + roleName + ", depCode=" + depCode + ", depName=" + depName + "]";
	}
	
}
