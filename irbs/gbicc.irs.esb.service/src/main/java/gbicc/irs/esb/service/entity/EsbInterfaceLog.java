package gbicc.irs.esb.service.entity;
		
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
* ESB日志记录表
* @author ljc 2019-04-28
*/
@Entity
@Table(name ="ESB_INTERFACE_LOG")
public class EsbInterfaceLog extends AuditorEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
    private String id;

    @Column(name="FD_SERVICE_CODE", length=30)
    private String serviceCode;

    @Column(name="FD_SERVICE_SCENE", length=10)
    private String serviceScene;

    @Column(name="FD_CODE", length=10)
    private String code;
    
    @Column(name="FD_STATUS", length=10)
    private String status;

    @Column(name="FD_MSG", length=2000)
    private String msg;
    
    @Lob 
    @Basic(fetch = FetchType.EAGER) 
    @Column(name="FD_SYS_BODY", columnDefinition="CLOB", nullable=true)
    private String sysBody;

    @Lob 
    @Basic(fetch = FetchType.EAGER) 
    @Column(name="FD_SYS_HEAD", columnDefinition="CLOB", nullable=true)
    private String sysHead;
    
    @Column(name="FD_SOURCE", length=100)
    private String source;
    
    @Column(name="FD_INPUT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputDate;

	public String getSysBody() {
		return sysBody;
	}

	public void setSysBody(String sysBody) {
		this.sysBody = sysBody;
	}

	public String getSysHead() {
		return sysHead;
	}

	public void setSysHead(String sysHead) {
		this.sysHead = sysHead;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceScene() {
		return serviceScene;
	}

	public void setServiceScene(String serviceScene) {
		this.serviceScene = serviceScene;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

    

}