package net.gbicc.app.pfm.jpa.support.dto;

import java.io.Serializable;

public class EadsRetreatRateDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
    private String uuid;
	/**
	 * 分公司
	 */
    private String branchCom;
	/**
	 * 渠道
	 */
	private String channel;
	/**
	 * 退保
	 */
	private Double retreatAmt;
	/**
	 * 撤保
	 */
	private Double revokeAmt;
	/**
	 * 实收
	 */
	private Double receiptsAmt;
	/**
	 * 预收
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

    public String getBranchCom() {
        return branchCom;
    }

    public void setBranchCom(String branchCom) {
        this.branchCom = branchCom;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Double getRetreatAmt() {
        return retreatAmt;
    }

    public void setRetreatAmt(Double retreatAmt) {
        this.retreatAmt = retreatAmt;
    }

    public Double getRevokeAmt() {
        return revokeAmt;
    }

    public void setRevokeAmt(Double revokeAmt) {
        this.revokeAmt = revokeAmt;
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
