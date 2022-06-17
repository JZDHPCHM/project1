package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 
 * ClassName: ReportRatingKinds <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:整体借款人评级分布—门类行业 实体类 <br/>  
 * date: 2019年4月22日 上午9:59:24 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_RATING_KINDS")
public class ReportRatingKinds extends AuditorEntity implements Serializable{



	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -4319268610910920426L;

	public ReportRatingKinds() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;

	//行业名称
	@Column(name="FD_KIND_NAME", length=200)
	private String kindName;
	
	//aaa 客户数目
	@Column(name="FD_AAA_NUM", length=200)
	private Double aaaNum;
	
	//aaa 占比
	@Column(name="FD_AAA_PERCENT", length=200)
	private String aaaPercent;
	
	//aa+ 客户数目
	@Column(name="FD_AA_P_NUM", length=200)
	private Double aaPNum;
	
	//aa+ 占比
	@Column(name="FD_AA_P_PERCENT", length=200)
	private String aaPPercent;
	
	//aa 客户数目
	@Column(name="FD_AA_NUM", length=200)
	private Double aaNum;
	
	//aa 占比
	@Column(name="FD_AA_PERCENT", length=200)
	private String aaPercent;
	
	//aa- 客户数目
	@Column(name="FD_AA_M_NUM", length=200)
	private Double aaMNum;
	
	//aa- 占比
	@Column(name="FD_AA_M_PERCENT", length=200)
	private String aaMPercent;
	
	//a+ 客户数目
	@Column(name="FD_A_P_NUM", length=200)
	private Double aPNum;
	
	//a+ 占比
	@Column(name="FD_A_P_PERCENT", length=200)
	private String aPPercent;
	
	//a 客户数目
	@Column(name="FD_A_NUM", length=200)
	private Double aNum;
	
	//a 占比
	@Column(name="FD_A_PERCENT", length=200)
	private String aPercent;
	
	
	//a- 客户数目
	@Column(name="FD_A_M_NUM", length=200)
	private Double aMNum;
	
	//a- 占比
	@Column(name="FD_A_M_PERCENT", length=200)
	private String aMPercent;
	
	//bbb+ 客户数目
	@Column(name="FD_BBB_P_NUM", length=200)
	private Double bbbPNum;
	
	//bbb+ 占比
	@Column(name="FD_BBB_P_PERCENT", length=200)
	private String bbbPPercent;
	
	//bbb 客户数目
	@Column(name="FD_BBB_NUM", length=200)
	private Double bbbNum;
	
	//bbb 占比
	@Column(name="FD_BBB_PERCENT", length=200)
	private String bbbPercent;
	

	//bbb- 客户数目
	@Column(name="FD_BBB_M_NUM", length=200)
	private Double bbbMNum;
	
	//bbb- 占比
	@Column(name="FD_BBB_M_PERCENT", length=200)
	private String bbbMPercent;
	
	//bb+ 客户数目
	@Column(name="FD_BB_P_NUM", length=200)
	private Double bbPNum;
	
	//bb+ 占比
	@Column(name="FD_BB_P_PERCENT", length=200)
	private String bbPPercent;
	
	//bb 客户数目
	@Column(name="FD_BB_NUM", length=200)
	private Double bbNum;
	
	//bb 占比
	@Column(name="FD_BB_PERCENT", length=200)
	private String bbPercent;
	
	//bb- 客户数目
	@Column(name="FD_BB_M_NUM", length=200)
	private Double bbMNum;
	
	//bb- 占比
	@Column(name="FD_BB_M_PERCENT", length=200)
	private String bbMPercent;
	
	//b 客户数目
	@Column(name="FD_B_NUM", length=200)
	private Double bNum;
	
	//b 占比
	@Column(name="FD_B_PERCENT", length=200)
	private String bPercent;
	
	
	//c 客户数目
	@Column(name="FD_C_NUM", length=200)
	private Double cNum;
	
	//c 占比
	@Column(name="FD_C_PERCENT", length=200)
	private String cPercent;
	
	//d 客户数目
	@Column(name="FD_D_NUM", length=200)
	private Double dNum;
	
	//d 占比
	@Column(name="FD_D_PERCENT", length=200)
	private String dPercent;
	
	
	
	//类型：1、客户数目、2、授信金额、3、业务余额
	@Column(name="FD_TYPE", length=10)
	private Integer type;
	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private Double repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_MONTH", length=200)
	private Integer month;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public Double getAaaNum() {
		return aaaNum;
	}

	public void setAaaNum(Double aaaNum) {
		this.aaaNum = aaaNum;
	}

	public String getAaaPercent() {
		return aaaPercent;
	}

	public void setAaaPercent(String aaaPercent) {
		this.aaaPercent = aaaPercent;
	}

	public Double getAaPNum() {
		return aaPNum;
	}

	public void setAaPNum(Double aaPNum) {
		this.aaPNum = aaPNum;
	}

	public String getAaPPercent() {
		return aaPPercent;
	}

	public void setAaPPercent(String aaPPercent) {
		this.aaPPercent = aaPPercent;
	}

	public Double getAaNum() {
		return aaNum;
	}

	public void setAaNum(Double aaNum) {
		this.aaNum = aaNum;
	}

	public String getAaPercent() {
		return aaPercent;
	}

	public void setAaPercent(String aaPercent) {
		this.aaPercent = aaPercent;
	}

	public Double getAaMNum() {
		return aaMNum;
	}

	public void setAaMNum(Double aaMNum) {
		this.aaMNum = aaMNum;
	}

	public String getAaMPercent() {
		return aaMPercent;
	}

	public void setAaMPercent(String aaMPercent) {
		this.aaMPercent = aaMPercent;
	}

	public Double getaPNum() {
		return aPNum;
	}

	public void setaPNum(Double aPNum) {
		this.aPNum = aPNum;
	}

	public String getaPPercent() {
		return aPPercent;
	}

	public void setaPPercent(String aPPercent) {
		this.aPPercent = aPPercent;
	}

	public Double getaNum() {
		return aNum;
	}

	public void setaNum(Double aNum) {
		this.aNum = aNum;
	}

	public String getaPercent() {
		return aPercent;
	}

	public void setaPercent(String aPercent) {
		this.aPercent = aPercent;
	}

	public Double getaMNum() {
		return aMNum;
	}

	public void setaMNum(Double aMNum) {
		this.aMNum = aMNum;
	}

	public String getaMPercent() {
		return aMPercent;
	}

	public void setaMPercent(String aMPercent) {
		this.aMPercent = aMPercent;
	}

	public Double getBbbPNum() {
		return bbbPNum;
	}

	public void setBbbPNum(Double bbbPNum) {
		this.bbbPNum = bbbPNum;
	}

	public String getBbbPPercent() {
		return bbbPPercent;
	}

	public void setBbbPPercent(String bbbPPercent) {
		this.bbbPPercent = bbbPPercent;
	}

	public Double getBbbNum() {
		return bbbNum;
	}

	public void setBbbNum(Double bbbNum) {
		this.bbbNum = bbbNum;
	}

	public String getBbbPercent() {
		return bbbPercent;
	}

	public void setBbbPercent(String bbbPercent) {
		this.bbbPercent = bbbPercent;
	}

	public Double getBbbMNum() {
		return bbbMNum;
	}

	public void setBbbMNum(Double bbbMNum) {
		this.bbbMNum = bbbMNum;
	}

	public String getBbbMPercent() {
		return bbbMPercent;
	}

	public void setBbbMPercent(String bbbMPercent) {
		this.bbbMPercent = bbbMPercent;
	}

	public Double getBbPNum() {
		return bbPNum;
	}

	public void setBbPNum(Double bbPNum) {
		this.bbPNum = bbPNum;
	}

	public String getBbPPercent() {
		return bbPPercent;
	}

	public void setBbPPercent(String bbPPercent) {
		this.bbPPercent = bbPPercent;
	}

	public Double getBbNum() {
		return bbNum;
	}

	public void setBbNum(Double bbNum) {
		this.bbNum = bbNum;
	}

	public String getBbPercent() {
		return bbPercent;
	}

	public void setBbPercent(String bbPercent) {
		this.bbPercent = bbPercent;
	}

	public Double getBbMNum() {
		return bbMNum;
	}

	public void setBbMNum(Double bbMNum) {
		this.bbMNum = bbMNum;
	}

	public String getBbMPercent() {
		return bbMPercent;
	}

	public void setBbMPercent(String bbMPercent) {
		this.bbMPercent = bbMPercent;
	}

	public Double getbNum() {
		return bNum;
	}

	public void setbNum(Double bNum) {
		this.bNum = bNum;
	}

	public String getbPercent() {
		return bPercent;
	}

	public void setbPercent(String bPercent) {
		this.bPercent = bPercent;
	}

	public Double getcNum() {
		return cNum;
	}

	public void setcNum(Double cNum) {
		this.cNum = cNum;
	}

	public String getcPercent() {
		return cPercent;
	}

	public void setcPercent(String cPercent) {
		this.cPercent = cPercent;
	}

	public Double getdNum() {
		return dNum;
	}

	public void setdNum(Double dNum) {
		this.dNum = dNum;
	}

	public String getdPercent() {
		return dPercent;
	}

	public void setdPercent(String dPercent) {
		this.dPercent = dPercent;
	}

	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
		this.repTime = repTime;
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

