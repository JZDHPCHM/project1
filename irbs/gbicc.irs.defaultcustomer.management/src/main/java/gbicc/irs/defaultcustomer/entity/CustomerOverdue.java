package gbicc.irs.defaultcustomer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="NS_CUST_OVERDUE")
public class CustomerOverdue implements Serializable{

	private static final long serialVersionUID = 2341597577423560024L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="PK_ID", length=36)
	private String id;
	
	
	@Column(name="CUST_ID")
	private String custId;
	
	@Column(name="OVERDUE_TYPE")
	private String overdueType;

	@Column(name="DATA_DATE")
	private String dataDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getOverdueType() {
		return overdueType;
	}

	public void setOverdueType(String overdueType) {
		this.overdueType = overdueType;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	
	
	
}
