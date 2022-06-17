package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 
 * ClassName: ReportRatingBranch <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月22日 下午3:59:26 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_RATING_BRANCH")
public class ReportRatingBranch extends AuditorEntity implements Serializable{



	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -4319268610910920426L;

	public ReportRatingBranch() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	//地区
	@Column(name="FD_AREA", length=200)
	private String area;
	
	
	//分支机构
	@Column(name="FD_KIND_NAME", length=200)
	private String kindName;
	
	
	//aaa 客户数目
	@Column(name="FD_AAA_NUM", length=200)
	private Double aaaNum;
	
	//aaa 占比
	@Column(name="FD_AAA_PERCENT", length=200)
	private Double aaaPercent;
	
	//aa+ 客户数目
	@Column(name="FD_AA_P_NUM", length=200)
	private Double aaPNum;
	
	//aa+ 占比
	@Column(name="FD_AA_P_PERCENT", length=200)
	private Double aaPPercent;
	
	//aa 客户数目
	@Column(name="FD_AA_NUM", length=200)
	private Double aaNum;
	
	//aa 占比
	@Column(name="FD_AA_PERCENT", length=200)
	private Double aaPercent;
	
	//aa- 客户数目
	@Column(name="FD_AA_M_NUM", length=200)
	private Double aaMNum;
	
	//aa- 占比
	@Column(name="FD_AA_M_PERCENT", length=200)
	private Double aaMPercent;
	
	//a+ 客户数目
	@Column(name="FD_A_P_NUM", length=200)
	private Double aPNum;
	
	//a+ 占比
	@Column(name="FD_A_P_PERCENT", length=200)
	private Double aPPercent;
	
	//a 客户数目
	@Column(name="FD_A_NUM", length=200)
	private Double aNum;
	
	//a 占比
	@Column(name="FD_A_PERCENT", length=200)
	private Double aPercent;
	
	
	//a- 客户数目
	@Column(name="FD_A_M_NUM", length=200)
	private Double aMNum;
	
	//a- 占比
	@Column(name="FD_A_M_PERCENT", length=200)
	private Double aMPercent;
	
	//bbb+ 客户数目
	@Column(name="FD_BBB_P_NUM", length=200)
	private Double bbbPNum;
	
	//bbb+ 占比
	@Column(name="FD_BBB_P_PERCENT", length=200)
	private Double bbbPPercent;
	
	//bbb 客户数目
	@Column(name="FD_BBB_NUM", length=200)
	private Double bbbNum;
	
	//bbb 占比
	@Column(name="FD_BBB_PERCENT", length=200)
	private Double bbbPercent;
	

	//bbb- 客户数目
	@Column(name="FD_BBB_M_NUM", length=200)
	private Double bbbMNum;
	
	//bbb- 占比
	@Column(name="FD_BBB_M_PERCENT", length=200)
	private Double bbbMPercent;
	
	//bb+ 客户数目
	@Column(name="FD_BB_P_NUM", length=200)
	private Double bbPNum;
	
	//bb+ 占比
	@Column(name="FD_BB_P_PERCENT", length=200)
	private Double bbPPercent;
	
	//bb 客户数目
	@Column(name="FD_BB_NUM", length=200)
	private Double bbNum;
	
	//bb 占比
	@Column(name="FD_BB_PERCENT", length=200)
	private Double bbPercent;
	
	//bb- 客户数目
	@Column(name="FD_BB_M_NUM", length=200)
	private Double bbMNum;
	
	//bb- 占比
	@Column(name="FD_BB_M_PERCENT", length=200)
	private Double bbMPercent;
	
	//b 客户数目
	@Column(name="FD_B_NUM", length=200)
	private Double bNum;
	
	//b 占比
	@Column(name="FD_B_PERCENT", length=200)
	private Double bPercent;
	
	
	//c 客户数目
	@Column(name="FD_C_NUM", length=200)
	private Double cNum;
	
	//c 占比
	@Column(name="FD_C_PERCENT", length=200)
	private Double cPercent;
	
	//d 客户数目
	@Column(name="FD_D_NUM", length=200)
	private Double dNum;
	
	//d 占比
	@Column(name="FD_D_PERCENT", length=200)
	private Double dPercent;
	
	//分支机构合计   客户数目
	@Column(name="FD_SUM_NUM", length=200)
	private Double sumNum;
	
	//分支机构合计  占比
	@Column(name="FD_SUM_PERCENT", length=200)
	private Double sumPercent;
	
	//类型：1、客户数目、2、授信金额、3、业务余额
	@Column(name="FD_TYPE", length=10)
	private Integer type;

	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private String repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_MONTH", length=200)
	private Integer month;


	public Double getAaaNum() {
		return aaaNum;
	}

	public void setAaaNum(Double aaaNum) {
		this.aaaNum = aaaNum;
	}

	public Double getAaaPercent() {
		return aaaPercent;
	}

	public void setAaaPercent(Double aaaPercent) {
		this.aaaPercent = aaaPercent;
	}

	public Double getAaPNum() {
		return aaPNum;
	}

	public void setAaPNum(Double aaPNum) {
		this.aaPNum = aaPNum;
	}

	public Double getAaPPercent() {
		return aaPPercent;
	}

	public void setAaPPercent(Double aaPPercent) {
		this.aaPPercent = aaPPercent;
	}

	public Double getAaNum() {
		return aaNum;
	}

	public void setAaNum(Double aaNum) {
		this.aaNum = aaNum;
	}

	public Double getAaPercent() {
		return aaPercent;
	}

	public void setAaPercent(Double aaPercent) {
		this.aaPercent = aaPercent;
	}

	public Double getAaMNum() {
		return aaMNum;
	}

	public void setAaMNum(Double aaMNum) {
		this.aaMNum = aaMNum;
	}

	public Double getAaMPercent() {
		return aaMPercent;
	}

	public void setAaMPercent(Double aaMPercent) {
		this.aaMPercent = aaMPercent;
	}

	public Double getaPNum() {
		return aPNum;
	}

	public void setaPNum(Double aPNum) {
		this.aPNum = aPNum;
	}

	public Double getaPPercent() {
		return aPPercent;
	}

	public void setaPPercent(Double aPPercent) {
		this.aPPercent = aPPercent;
	}

	public Double getaNum() {
		return aNum;
	}

	public void setaNum(Double aNum) {
		this.aNum = aNum;
	}

	public Double getaPercent() {
		return aPercent;
	}

	public void setaPercent(Double aPercent) {
		this.aPercent = aPercent;
	}

	public Double getaMNum() {
		return aMNum;
	}

	public void setaMNum(Double aMNum) {
		this.aMNum = aMNum;
	}

	public Double getaMPercent() {
		return aMPercent;
	}

	public void setaMPercent(Double aMPercent) {
		this.aMPercent = aMPercent;
	}

	public Double getBbbPNum() {
		return bbbPNum;
	}

	public void setBbbPNum(Double bbbPNum) {
		this.bbbPNum = bbbPNum;
	}

	public Double getBbbPPercent() {
		return bbbPPercent;
	}

	public void setBbbPPercent(Double bbbPPercent) {
		this.bbbPPercent = bbbPPercent;
	}

	public Double getBbbNum() {
		return bbbNum;
	}

	public void setBbbNum(Double bbbNum) {
		this.bbbNum = bbbNum;
	}

	public Double getBbbPercent() {
		return bbbPercent;
	}

	public void setBbbPercent(Double bbbPercent) {
		this.bbbPercent = bbbPercent;
	}

	public Double getBbbMNum() {
		return bbbMNum;
	}

	public void setBbbMNum(Double bbbMNum) {
		this.bbbMNum = bbbMNum;
	}

	public Double getBbbMPercent() {
		return bbbMPercent;
	}

	public void setBbbMPercent(Double bbbMPercent) {
		this.bbbMPercent = bbbMPercent;
	}

	public Double getBbPNum() {
		return bbPNum;
	}

	public void setBbPNum(Double bbPNum) {
		this.bbPNum = bbPNum;
	}

	public Double getBbPPercent() {
		return bbPPercent;
	}

	public void setBbPPercent(Double bbPPercent) {
		this.bbPPercent = bbPPercent;
	}

	public Double getBbNum() {
		return bbNum;
	}

	public void setBbNum(Double bbNum) {
		this.bbNum = bbNum;
	}

	public Double getBbPercent() {
		return bbPercent;
	}

	public void setBbPercent(Double bbPercent) {
		this.bbPercent = bbPercent;
	}

	public Double getBbMNum() {
		return bbMNum;
	}

	public void setBbMNum(Double bbMNum) {
		this.bbMNum = bbMNum;
	}

	public Double getBbMPercent() {
		return bbMPercent;
	}

	public void setBbMPercent(Double bbMPercent) {
		this.bbMPercent = bbMPercent;
	}

	public Double getbNum() {
		return bNum;
	}

	public void setbNum(Double bNum) {
		this.bNum = bNum;
	}

	public Double getbPercent() {
		return bPercent;
	}

	public void setbPercent(Double bPercent) {
		this.bPercent = bPercent;
	}

	public Double getcNum() {
		return cNum;
	}

	public void setcNum(Double cNum) {
		this.cNum = cNum;
	}

	public Double getcPercent() {
		return cPercent;
	}

	public void setcPercent(Double cPercent) {
		this.cPercent = cPercent;
	}

	public Double getdNum() {
		return dNum;
	}

	public void setdNum(Double dPNum) {
		this.dNum = dNum;
	}

	public Double getdPercent() {
		return dPercent;
	}

	public void setdPercent(Double dPercent) {
		this.dPercent = dPercent;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getRepTime() {
		return repTime;
	}

	public void setRepTime(String repTime) {
		this.repTime = repTime;
	}


	public Double getSumNum() {
		return sumNum;
	}

	public void setSumNum(Double sumNum) {
		this.sumNum = sumNum;
	}

	public Double getSumPercent() {
		return sumPercent;
	}

	public void setSumPercent(Double sumPercent) {
		this.sumPercent = sumPercent;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Transient
	private String typeName;

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return type==1?"客户数目":type==2?"授信金额":"业务余额";
	}
}

