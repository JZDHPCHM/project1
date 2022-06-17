package gbicc.irs.defaultcustomer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import gbicc.irs.defaultcustomer.support.DefaultCustomerAffirmMode;

/**
 * 违约事件定义实体类
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_DC_EVENT_DEF")
public class DefaultEventLib extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 720575746768864955L;

	public DefaultEventLib() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//违约事件编号
	@Column(name="FD_EVENT_CODE", length=50)
	private String eventCode;
	
	//违约事件编号
	@Column(name="FD_EVENT_RULESEXPRE", length=1000)
	private String eventRulesExpression;
		
	//违约事件描述
	@Column(name="FD_EVENT_DESCRIBE", length=1000)
	private String eventDescribe;
	
	//静默群 天
	@Column(name="FD_QUIET_PERIOD", length=1000)
	private String quietPeriod;
	
	//违约事件描述
	@Column(name="FD_MARK", length=2000)
	private String mark;
	
	//违约事件类型
	@Column(name="FD_TYPE", length=50)
	@Enumerated(EnumType.STRING)
	private DefaultCustomerAffirmMode type;
	
	//是否有效
	@Column(name="FD_VALID")
	@org.hibernate.annotations.Type(type="numeric_boolean")
	private Boolean valid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventRulesExpression() {
		return eventRulesExpression;
	}

	public void setEventRulesExpression(String eventRulesExpression) {
		this.eventRulesExpression = eventRulesExpression;
	}

	public String getEventDescribe() {
		return eventDescribe;
	}

	public void setEventDescribe(String eventDescribe) {
		this.eventDescribe = eventDescribe;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public DefaultCustomerAffirmMode getType() {
		return type;
	}

	public void setType(DefaultCustomerAffirmMode type) {
		this.type = type;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(String quietPeriod) {
		this.quietPeriod = quietPeriod;
	}


	
}

