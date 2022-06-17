package gbicc.irs.defaultcustomer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 违约客户管理审批记录实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_DC_APPROVAL_LOGS")
public class DefaultCustomerApproveLogs extends AuditorEntity implements Serializable {
	
	private static final long serialVersionUID = -7897244417317013912L;

	public DefaultCustomerApproveLogs() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//业务主键ID
	@Column(name="FD_BUSSID")
	private String bussId;
	
	//角色名称
	@Column(name="FD_ROLE_NAME")
	private String roleName;
	
	//角色编号
	@Column(name="FD_ROLE_CODE")
	private String roleCode;
	
	//用户编号
	@Column(name="FD_USER_CODE")
	private String userCode;
	
	//用户名称
	@Column(name="FD_USER_NAME")
	private String userName;
	
	//审批意见
	@Column(name="FD_APPROVAL_OPINION")
	private String approvalOpinion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBussId() {
		return bussId;
	}

	public void setBussId(String bussId) {
		this.bussId = bussId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApprovalOpinion() {
		return approvalOpinion;
	}

	public void setApprovalOpinion(String approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
	}
	
}
