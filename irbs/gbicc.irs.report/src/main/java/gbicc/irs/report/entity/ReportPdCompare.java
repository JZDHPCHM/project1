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
 * ClassName: ReportPdCompare <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2019年5月6日 上午9:29:26 <br/>
 *
 * @author xiaoxianlie
 * @since JDK 1.8
 */
@Entity
@Table(name = "IRS_REPORT_PD_COMPARE")
public class ReportPdCompare extends AuditorEntity implements Serializable {


    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     *
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 8958216887983190292L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "FD_GUEST_GR")
    private String fdGuestGr;

    //PD平均值
    @Column(name = "FD_PD_AVG")
    private Double fdPdAvg;

    //客户数目
    @Column(name = "FD_CUSTOM_NUM")
    private Double fdCustomNum;

    //违约客户数目
    @Column(name = "FD_ILEGAL_CUSTOM_NUM")
    private Double fdIlegalCustomNum;

    //实际违约率
    @Column(name = "FD_ACTUAL_ILEGAL_PER")
    private Double fdActualIlegalPer;

    //EAD金额
    @Column(name = "FD_EAD_NUM")
    private Double fdEadNum;

    //EAD占比
    @Column(name = "FD_EAD_PER")
    private Double fdEadPer;

    //报表时间
    @Column(name = "FD_REP_TIME", length = 200)
    private String repTime;

    //报表时间
    @Column(name = "FD_YEAR", length = 200)
    private Integer year;

    public ReportPdCompare() {
    }


    public Double getFdPdAvg() {
        return fdPdAvg;
    }

    public void setFdPdAvg(Double fdPdAvg) {
        this.fdPdAvg = fdPdAvg;
    }

    public Double getFdCustomNum() {
        return fdCustomNum;
    }

    public void setFdCustomNum(Double fdCustomNum) {
        this.fdCustomNum = fdCustomNum;
    }

    public Double getFdIlegalCustomNum() {
        return fdIlegalCustomNum;
    }

    public void setFdIlegalCustomNum(Double fdIlegalCustomNum) {
        this.fdIlegalCustomNum = fdIlegalCustomNum;
    }

    public Double getFdActualIlegalPer() {
        return fdActualIlegalPer;
    }

    public void setFdActualIlegalPer(Double fdActualIlegalPer) {
        this.fdActualIlegalPer = fdActualIlegalPer;
    }

    public Double getFdEadNum() {
        return fdEadNum;
    }

    public void setFdEadNum(Double fdEadNum) {
        this.fdEadNum = fdEadNum;
    }

    public Double getFdEadPer() {
        return fdEadPer;
    }

    public void setFdEadPer(Double fdEadPer) {
        this.fdEadPer = fdEadPer;
    }


    public Integer getYear() {
        return year;
    }


    public void setYear(Integer year) {
        this.year = year;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFdGuestGr() {
        return fdGuestGr;
    }


    public void setFdGuestGr(String fdGuestGr) {
        this.fdGuestGr = fdGuestGr;
    }


    public String getRepTime() {
        return repTime;
    }


    public void setRepTime(String repTime) {
        this.repTime = repTime;
    }

}