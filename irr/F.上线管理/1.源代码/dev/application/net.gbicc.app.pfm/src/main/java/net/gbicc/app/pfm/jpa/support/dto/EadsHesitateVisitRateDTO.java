package net.gbicc.app.pfm.jpa.support.dto;

import java.io.Serializable;

public class EadsHesitateVisitRateDTO implements Serializable{
	
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
	 * 回访件数
	 */
	private Integer visitNum;
	/**
	 * 保单件数
	 */
	private Integer policyNum;
	/**
	 * 比率
	 */
    private String resultRate;
    /**
     * 得分
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

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public Integer getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(Integer policyNum) {
        this.policyNum = policyNum;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getResultRate() {
        return resultRate;
    }

    public void setResultRate(String resultRate) {
        this.resultRate = resultRate;
    }

}
