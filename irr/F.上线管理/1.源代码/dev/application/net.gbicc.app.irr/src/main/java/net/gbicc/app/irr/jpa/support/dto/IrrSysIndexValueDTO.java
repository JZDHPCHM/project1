package net.gbicc.app.irr.jpa.support.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* IRR系统指标值实体
*/
public class IrrSysIndexValueDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 指标编号
	 */
	private String indexId;
	/**
	 * 机构编号
	 */
	private String orgId;
	/**
	 * 渠道编号
	 */
	private String channelId;
	/**
	 * 指标值
	 */
	private BigDecimal indexValue;
	/**
	 * 评估期
	 */
	private String evalDate;
	/**
	 * 加载日期
	 */
	private Date loadDt;
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public BigDecimal getIndexValue() {
		return indexValue;
	}
	public void setIndexValue(BigDecimal indexValue) {
		this.indexValue = indexValue;
	}
	public String getEvalDate() {
		return evalDate;
	}
	public void setEvalDate(String evalDate) {
		this.evalDate = evalDate;
	}
	public Date getLoadDt() {
		return loadDt;
	}
	public void setLoadDt(Date loadDt) {
		this.loadDt = loadDt;
	}
}
