package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;


/**
 * The persistent class for the IRS_REPORT_PROFIT_BRANCH database table.
 */
@Entity
@Table(name = "IRS_REPORT_LOAN_FIVE")
public class ReportLoanFive extends AuditorEntity implements Serializable {

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     *
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -8243806347977295336L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "FD_A_M_PROFIT")
    private Double fdAMProfit;

    @Column(name = "FD_A_P_PROFIT")
    private Double fdAPProfit;

    @Column(name = "FD_A_PROFIT")
    private Double fdAProfit;

    @Column(name = "FD_AA_M_PROFIT")
    private Double fdAaMProfit;

    @Column(name = "FD_AA_P_PROFIT")
    private Double fdAaPProfit;

    @Column(name = "FD_AA_PROFIT")
    private Double fdAaProfit;

    @Column(name = "FD_AAA_PROFIT")
    private Double fdAaaProfit;

    @Column(name = "FD_AVG_PROFIT")
    private Double fdAvgProfit;

    @Column(name = "FD_B_PROFIT")
    private Double fdBProfit;

    @Column(name = "FD_BB_M_PROFIT")
    private Double fdBbMProfit;

    @Column(name = "FD_BB_P_PROFIT")
    private Double fdBbPProfit;

    @Column(name = "FD_BB_PROFIT")
    private Double fdBbProfit;

    @Column(name = "FD_BBB_M_PROFIT")
    private Double fdBbbMProfit;

    @Column(name = "FD_BBB_P_PROFIT")
    private Double fdBbbPProfit;

    @Column(name = "FD_BBB_PROFIT")
    private Double fdBbbProfit;

    @Column(name = "FD_C_PROFIT")
    private Double fdCProfit;

    @Column(name = "FD_D_PROFIT")
    private Double fdDProfit;

    @Column(name = "FD_KIND_NAME")
    private String fdKindName;

    @Column(name = "FD_QUARTER")
    private String quater;

    @Column(name = "FD_REP_TIME")
    private String fdRepTime;

    @Column(name = "FD_YEAR")
    private Integer year;

    @Column(name = "FD_TYPE")
    private Integer type;

    public ReportLoanFive() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getFdKindName() {
        return fdKindName;
    }

    public void setFdKindName(String fdKindName) {
        this.fdKindName = fdKindName;
    }

    public String getFdRepTime() {
        return fdRepTime;
    }

    public void setFdRepTime(String fdRepTime) {
        this.fdRepTime = fdRepTime;
    }

    public Double getFdAMProfit() {
        return fdAMProfit;
    }

    public void setFdAMProfit(Double fdAMProfit) {
        this.fdAMProfit = fdAMProfit;
    }

    public Double getFdAPProfit() {
        return fdAPProfit;
    }

    public void setFdAPProfit(Double fdAPProfit) {
        this.fdAPProfit = fdAPProfit;
    }

    public Double getFdAProfit() {
        return fdAProfit;
    }

    public void setFdAProfit(Double fdAProfit) {
        this.fdAProfit = fdAProfit;
    }

    public Double getFdAaMProfit() {
        return fdAaMProfit;
    }

    public void setFdAaMProfit(Double fdAaMProfit) {
        this.fdAaMProfit = fdAaMProfit;
    }

    public Double getFdAaPProfit() {
        return fdAaPProfit;
    }

    public void setFdAaPProfit(Double fdAaPProfit) {
        this.fdAaPProfit = fdAaPProfit;
    }

    public Double getFdAaProfit() {
        return fdAaProfit;
    }

    public void setFdAaProfit(Double fdAaProfit) {
        this.fdAaProfit = fdAaProfit;
    }

    public Double getFdAaaProfit() {
        return fdAaaProfit;
    }

    public void setFdAaaProfit(Double fdAaaProfit) {
        this.fdAaaProfit = fdAaaProfit;
    }

    public Double getFdAvgProfit() {
        return fdAvgProfit;
    }

    public void setFdAvgProfit(Double fdAvgProfit) {
        this.fdAvgProfit = fdAvgProfit;
    }

    public Double getFdBProfit() {
        return fdBProfit;
    }

    public void setFdBProfit(Double fdBProfit) {
        this.fdBProfit = fdBProfit;
    }

    public Double getFdBbMProfit() {
        return fdBbMProfit;
    }

    public void setFdBbMProfit(Double fdBbMProfit) {
        this.fdBbMProfit = fdBbMProfit;
    }

    public Double getFdBbPProfit() {
        return fdBbPProfit;
    }

    public void setFdBbPProfit(Double fdBbPProfit) {
        this.fdBbPProfit = fdBbPProfit;
    }

    public Double getFdBbProfit() {
        return fdBbProfit;
    }

    public void setFdBbProfit(Double fdBbProfit) {
        this.fdBbProfit = fdBbProfit;
    }

    public Double getFdBbbMProfit() {
        return fdBbbMProfit;
    }

    public void setFdBbbMProfit(Double fdBbbMProfit) {
        this.fdBbbMProfit = fdBbbMProfit;
    }

    public Double getFdBbbPProfit() {
        return fdBbbPProfit;
    }

    public void setFdBbbPProfit(Double fdBbbPProfit) {
        this.fdBbbPProfit = fdBbbPProfit;
    }

    public Double getFdBbbProfit() {
        return fdBbbProfit;
    }

    public void setFdBbbProfit(Double fdBbbProfit) {
        this.fdBbbProfit = fdBbbProfit;
    }

    public Double getFdCProfit() {
        return fdCProfit;
    }

    public void setFdCProfit(Double fdCProfit) {
        this.fdCProfit = fdCProfit;
    }

    public Double getFdDProfit() {
        return fdDProfit;
    }

    public void setFdDProfit(Double fdDProfit) {
        this.fdDProfit = fdDProfit;
    }

    public String getQuater() {
        return quater;
    }

    public void setQuater(String quater) {
        this.quater = quater;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}