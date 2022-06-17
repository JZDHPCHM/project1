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
 * 
 * ClassName: ReportMoveCustom <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月5日 下午2:51:07 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_MOVE_CUSTOM")
public class ReportMoveCustom extends AuditorEntity implements Serializable{


	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -5675931274754908101L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;

	@Column(name="FD_A_M_DISTRI")
	private Double fdAMDistri;

	@Column(name="FD_A_P_DISTRI")
	private Double fdAPDistri;

	@Column(name="FD_A_DISTRI")
	private Double fdADistri;

	@Column(name="FD_AA_M_DISTRI")
	private Double fdAaMDistri;

	@Column(name="FD_AA_P_DISTRI")
	private Double fdAaPDistri;

	@Column(name="FD_AA_DISTRI")
	private Double fdAaDistri;

	@Column(name="FD_AAA_DISTRI")
	private Double fdAaaDistri;


	@Column(name="FD_B_DISTRI")
	private Double fdBDistri;

	@Column(name="FD_BB_M_DISTRI")
	private Double fdBbMDistri;

	@Column(name="FD_BB_P_DISTRI")
	private Double fdBbPDistri;

	@Column(name="FD_BB_DISTRI")
	private Double fdBbDistri;

	@Column(name="FD_BBB_M_DISTRI")
	private Double fdBbbMDistri;

	@Column(name="FD_BBB_P_DISTRI")
	private Double fdBbbPDistri;

	@Column(name="FD_BBB_DISTRI")
	private Double fdBbbDistri;

	@Column(name="FD_C_DISTRI")
	private Double fdCDistri;

	@Column(name="FD_D_DISTRI")
	private Double fdDDistri;

	//分布情况
	@Column(name="FD_KIND_NAME")
	private String fdKindName;
	
	
	//上期合计
	@Column(name="FD_LAST_SUM_DISTRI")
	private Double fdLastSumDistri;
	
	//向上迁移总数
	@Column(name="FD_MOVE_UP_NUM")
	private Double fdMoveUpNum;
	
	//向上迁移占比
	@Column(name="FD_MOVE_UP_PER")
	private Double fdMoveUpPer;
	
	//向下迁移总数
	@Column(name="FD_MOVE_DOWN_NUM")
	private Double fdMoveDownNum;
	
	//向下迁移占比
	@Column(name="FD_MOVE_DOWN_PER")
	private Double fdMoveDownPer;

	//迁移指数
	@Column(name="FD_MOVE_POINT")
	private Double fdMovePoint;
	
	
	
	@Column(name="FD_QUARTER")
	private Integer quarter;

	@Column(name="FD_REP_TIME")
	private String fdRepTime;

	@Column(name="FD_YEAR")
	private Integer year;

	public ReportMoveCustom() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getFdAMDistri() {
		return fdAMDistri;
	}

	public void setFdAMDistri(Double fdAMDistri) {
		this.fdAMDistri = fdAMDistri;
	}

	public Double getFdAPDistri() {
		return fdAPDistri;
	}

	public void setFdAPDistri(Double fdAPDistri) {
		this.fdAPDistri = fdAPDistri;
	}

	public Double getFdADistri() {
		return fdADistri;
	}

	public void setFdADistri(Double fdADistri) {
		this.fdADistri = fdADistri;
	}

	public Double getFdAaMDistri() {
		return fdAaMDistri;
	}

	public void setFdAaMDistri(Double fdAaMDistri) {
		this.fdAaMDistri = fdAaMDistri;
	}

	public Double getFdAaPDistri() {
		return fdAaPDistri;
	}

	public void setFdAaPDistri(Double fdAaPDistri) {
		this.fdAaPDistri = fdAaPDistri;
	}

	public Double getFdAaDistri() {
		return fdAaDistri;
	}

	public void setFdAaDistri(Double fdAaDistri) {
		this.fdAaDistri = fdAaDistri;
	}

	public Double getFdAaaDistri() {
		return fdAaaDistri;
	}

	public void setFdAaaDistri(Double fdAaaDistri) {
		this.fdAaaDistri = fdAaaDistri;
	}

	public Double getFdBDistri() {
		return fdBDistri;
	}

	public void setFdBDistri(Double fdBDistri) {
		this.fdBDistri = fdBDistri;
	}

	public Double getFdBbMDistri() {
		return fdBbMDistri;
	}

	public void setFdBbMDistri(Double fdBbMDistri) {
		this.fdBbMDistri = fdBbMDistri;
	}

	public Double getFdBbPDistri() {
		return fdBbPDistri;
	}

	public void setFdBbPDistri(Double fdBbPDistri) {
		this.fdBbPDistri = fdBbPDistri;
	}

	public Double getFdBbDistri() {
		return fdBbDistri;
	}

	public void setFdBbDistri(Double fdBbDistri) {
		this.fdBbDistri = fdBbDistri;
	}

	public Double getFdBbbMDistri() {
		return fdBbbMDistri;
	}

	public void setFdBbbMDistri(Double fdBbbMDistri) {
		this.fdBbbMDistri = fdBbbMDistri;
	}

	public Double getFdBbbPDistri() {
		return fdBbbPDistri;
	}

	public void setFdBbbPDistri(Double fdBbbPDistri) {
		this.fdBbbPDistri = fdBbbPDistri;
	}

	public Double getFdBbbDistri() {
		return fdBbbDistri;
	}

	public void setFdBbbDistri(Double fdBbbDistri) {
		this.fdBbbDistri = fdBbbDistri;
	}

	public Double getFdCDistri() {
		return fdCDistri;
	}

	public void setFdCDistri(Double fdCDistri) {
		this.fdCDistri = fdCDistri;
	}

	public Double getFdDDistri() {
		return fdDDistri;
	}

	public void setFdDDistri(Double fdDDistri) {
		this.fdDDistri = fdDDistri;
	}

	public String getFdKindName() {
		return fdKindName;
	}

	public void setFdKindName(String fdKindName) {
		this.fdKindName = fdKindName;
	}

	public Double getFdLastSumDistri() {
		return fdLastSumDistri;
	}

	public void setFdLastSumDistri(Double fdLastSumDistri) {
		this.fdLastSumDistri = fdLastSumDistri;
	}

	public Double getFdMoveUpNum() {
		return fdMoveUpNum;
	}

	public void setFdMoveUpNum(Double fdMoveUpNum) {
		this.fdMoveUpNum = fdMoveUpNum;
	}

	public Double getFdMoveUpPer() {
		return fdMoveUpPer;
	}

	public void setFdMoveUpPer(Double fdMoveUpPer) {
		this.fdMoveUpPer = fdMoveUpPer;
	}

	public Double getFdMoveDownNum() {
		return fdMoveDownNum;
	}

	public void setFdMoveDownNum(Double fdMoveDownNum) {
		this.fdMoveDownNum = fdMoveDownNum;
	}

	public Double getFdMoveDownPer() {
		return fdMoveDownPer;
	}

	public void setFdMoveDownPer(Double fdMoveDownPer) {
		this.fdMoveDownPer = fdMoveDownPer;
	}

	public Double getFdMovePoint() {
		return fdMovePoint;
	}

	public void setFdMovePoint(Double fdMovePoint) {
		this.fdMovePoint = fdMovePoint;
	}

	public String getFdRepTime() {
		return fdRepTime;
	}

	public void setFdRepTime(String fdRepTime) {
		this.fdRepTime = fdRepTime;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}


	
}