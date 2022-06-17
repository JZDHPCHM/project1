package com.gbicc.aicr.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name = "SYS_ROLE")
//新核心角色表
public class SystemRole extends AuditorEntity implements Serializable {
	private static final long serialVersionUID = 1L;
		public static long getSerialversionuid() {
		return serialVersionUID;
	}

		@Id
		@GeneratedValue(generator = "system-uuid")
		@GenericGenerator(name = "system-uuid", strategy = "uuid2")
		// 角色号
		@Column(name = "ROLE_ID")
		private String roleId;
		// 角色名
		@Column(name = "ROLE_NAME")
		private String roleName;
		// 角色状态
		@Column(name = "ROLE_STATUS")
		private String roleStatus;
		// 角色类型
		@Column(name = "ROLE_TYPE")
		private String roleType;
		// 创建人
		@Column(name = "INPUT_USER_ID")
		private String inputUserId;
		// 创建时间
		@Column(name = "INPUT_TIME")
		private String inputTime;
		// 更新时间
		@Column(name = "UPDATE_TIME")
		private String updateTime;
		//是否选择
		@Transient
		private String isSelected;

		public String getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(String isSelected) {
			this.isSelected = isSelected;
		}

		public String getRoleId() {
			return roleId;
		}

		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

		public String getRoleStatus() {
			return roleStatus;
		}

		public void setRoleStatus(String roleStatus) {
			this.roleStatus = roleStatus;
		}

		public String getRoleType() {
			return roleType;
		}

		public void setRoleType(String roleType) {
			this.roleType = roleType;
		}

		public String getInputUserId() {
			return inputUserId;
		}

		public void setInputUserId(String inputUserId) {
			this.inputUserId = inputUserId;
		}

		public String getInputTime() {
			return inputTime;
		}

		public void setInputTime(String inputTime) {
			this.inputTime = inputTime;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

	
}
