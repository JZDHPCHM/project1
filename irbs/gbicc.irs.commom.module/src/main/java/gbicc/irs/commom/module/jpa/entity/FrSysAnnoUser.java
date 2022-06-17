package gbicc.irs.commom.module.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name="FR_SYS_ANNO_USER")
public class FrSysAnnoUser extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -735729094276726282L;

	public FrSysAnnoUser() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	@Column(name="FD_A_ID", length=36)
	private String aId;
	
	@Column(name="FD_U_ID", length=36)
	private String uId;

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}
	
	
}

