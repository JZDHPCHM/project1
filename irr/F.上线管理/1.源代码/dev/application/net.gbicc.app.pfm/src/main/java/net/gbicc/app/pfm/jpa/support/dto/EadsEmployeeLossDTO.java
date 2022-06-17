package net.gbicc.app.pfm.jpa.support.dto;

import java.io.Serializable;

public class EadsEmployeeLossDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
    private String uuid;
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 分公司
	 */
	private String orgName;
	/**
	 * 离职人数
	 */
    private Integer quitnum;
	/**
	 * 员工人数
	 */
	private Integer employeeNum;
	/**
	 * 新增人数
	 */
	private Integer addNum;
	/**
	 * 离职率
	 */
    private String quitRate;
    /**
     * 分值
     */
    private Double score;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getQuitnum() {
        return quitnum;
    }

    public void setQuitnum(Integer quitnum) {
        this.quitnum = quitnum;
    }

    public Integer getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(Integer employeeNum) {
        this.employeeNum = employeeNum;
    }

    public Integer getAddNum() {
        return addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }

    public String getQuitRate() {
        return quitRate;
    }

    public void setQuitRate(String quitRate) {
        this.quitRate = quitRate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
