package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 年报资产状况信息
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_ASSETS")
public class FbAnnualReportAssetsEntity extends AuditorEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @Column(name="ID",length=36)
    private String id;
    /**
     * 统一社会信用代码、注册号
     */
    @Column(name="COMPANY_ID")
    private String companyId;
    /**
     * 年报ID
     */
    @Column(name="ANNUAL_REPORT_ID")
    private String annualReportId;
    /**
     * 净利润
     */
    @Column(name="NET_PROFIT")
    private String netProfit;
    /**
     * 利润总额
     */
    @Column(name="PROFI_TOTAL")
    private String profiTotal;
    /**
     * 所有者权益合计
     */
    @Column(name="TOTAL_OWNER_EQUITY")
    private String totalOwnerEquity;
    /**
     * 纳税总额
     */
    @Column(name="TOTAL_TAX_AMOUNT")
    private String totalTaxAmount;
    /**
     * 营业总收入
     */
    @Column(name="TOTAL_OPERATE_INCOME")
    private String totalOperateIncome;
    /**
     * 营业总收入中主营业务收入
     */
    @Column(name="MAIN_OPERATE_INCOME")
    private String mainOperateIncome;
    /**
     * 负债总额
     */
    @Column(name="TOTAL_LIABILITY_AMOUNT")
    private String totalLiabilityAmount;
    /**
     * 资产总额
     */
    @Column(name="TOTAL_ASSETS")
    private String totalAssets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getAnnualReportId() {
        return annualReportId;
    }

    public void setAnnualReportId(String annualReportId) {
        this.annualReportId = annualReportId == null ? null : annualReportId.trim();
    }

    public String getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(String netProfit) {
        this.netProfit = netProfit == null ? null : netProfit.trim();
    }

    public String getProfiTotal() {
        return profiTotal;
    }

    public void setProfiTotal(String profiTotal) {
        this.profiTotal = profiTotal == null ? null : profiTotal.trim();
    }

    public String getTotalOwnerEquity() {
        return totalOwnerEquity;
    }

    public void setTotalOwnerEquity(String totalOwnerEquity) {
        this.totalOwnerEquity = totalOwnerEquity == null ? null : totalOwnerEquity.trim();
    }

    public String getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(String totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount == null ? null : totalTaxAmount.trim();
    }

    public String getTotalOperateIncome() {
        return totalOperateIncome;
    }

    public void setTotalOperateIncome(String totalOperateIncome) {
        this.totalOperateIncome = totalOperateIncome == null ? null : totalOperateIncome.trim();
    }

    public String getMainOperateIncome() {
        return mainOperateIncome;
    }

    public void setMainOperateIncome(String mainOperateIncome) {
        this.mainOperateIncome = mainOperateIncome == null ? null : mainOperateIncome.trim();
    }

    public String getTotalLiabilityAmount() {
        return totalLiabilityAmount;
    }

    public void setTotalLiabilityAmount(String totalLiabilityAmount) {
        this.totalLiabilityAmount = totalLiabilityAmount == null ? null : totalLiabilityAmount.trim();
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets == null ? null : totalAssets.trim();
    }
}