package net.gbicc.app.pfm.jpa.support.dto;

import java.io.Serializable;

public class EadsContinueChargeRateDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
    private String uuid;
	/**
	 * 月份
	 */
	private String month;
	/**
	 * 二级机构
	 */
	private String secOrg;
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 实收保费
	 */
	private Double receiptsAmt;
	/**
	 * 应收保费
	 */
	private Double receivableAmt;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSecOrg() {
        return secOrg;
    }

    public void setSecOrg(String secOrg) {
        this.secOrg = secOrg;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Double getReceiptsAmt() {
        return receiptsAmt;
    }

    public void setReceiptsAmt(Double receiptsAmt) {
        this.receiptsAmt = receiptsAmt;
    }

    public Double getReceivableAmt() {
        return receivableAmt;
    }

    public void setReceivableAmt(Double receivableAmt) {
        this.receivableAmt = receivableAmt;
    }

    public String getResultRate() {
        return resultRate;
    }

    public void setResultRate(String resultRate) {
        this.resultRate = resultRate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
