package gbicc.irs.query.entity;

import java.io.Serializable;

/**
 * 评级次数监控查询实体
 * 
 * @author 寇鹏阳
 *
 */
public class RatingMonitoringWrapper implements Serializable {

	private static final long serialVersionUID = -3500748083050281667L;

	public RatingMonitoringWrapper() {
	}

	private String id;
	
	// 客户编号
	private String custNo;

	// 客户名称
	private String custName;

	//发起人
	private String launchName;

	//发起次数
	private Integer launchNum;

	private String startDt;
	
	private String endDt;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getLaunchName() {
		return launchName;
	}

	public void setLaunchName(String launchName) {
		this.launchName = launchName;
	}

	public Integer getLaunchNum() {
		return launchNum;
	}

	public void setLaunchNum(Integer launchNum) {
		this.launchNum = launchNum;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	
}
