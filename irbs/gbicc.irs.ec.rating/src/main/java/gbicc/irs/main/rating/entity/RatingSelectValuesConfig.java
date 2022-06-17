package gbicc.irs.main.rating.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 组件候选值定义实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_R_CFG_VALUES")
public class RatingSelectValuesConfig extends AuditorEntity implements Serializable {
	private static final long serialVersionUID = 3917145143245537229L;

	public RatingSelectValuesConfig() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//问题定义ID
	@Column(name="FD_DEF_ID", length=36)
	private String defId;
	
	//问题定义编号
	@Column(name="FD_DEF_CODE",length=50)
	private String defCode;
	
	//选项序号
	@Column(name="FD_ORDER_NUM")
	private Integer orderNum;
	
	//显示值
	@Column(name="FD_DIS_VAL",length=500)
	private String displayValue;
	
	//使用值
	@Column(name="FD_VAL")
	private String realValue;
	
	//权重
	@Column(name="FD_WEIGHT",length=1000)
	private String weight;
	
	//补录项ID	
	@Column(name="FD_ADDIT_ID",length=36)
	private String additID;
	
	//备注
	@Column(name="FD_REMARKS",length=1000)
	private String reMarks;
		
	//选择项列表
	@Column(name="FD_IS_HIS",length=1000)
	@org.hibernate.annotations.Type(type="numeric_boolean")
	private Boolean isHis;
	
	//调整分数
	@Column(name="FD_ADJUSTMENT_FRACTION")
	private String adjustmentFraction;

	//调整类型
	@Column(name="FD_ADJUSTMENT_TYPE")
	private String adjustmentType;
	
	public String getAdjustmentFraction() {
		return adjustmentFraction;
	}

	public void setAdjustmentFraction(String adjustmentFraction) {
		this.adjustmentFraction = adjustmentFraction;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getDefCode() {
		return defCode;
	}

	public void setDefCode(String defCode) {
		this.defCode = defCode;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getRealValue() {
		return realValue;
	}

	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAdditID() {
		return additID;
	}

	public void setAdditID(String additID) {
		this.additID = additID;
	}

	public String getReMarks() {
		return reMarks;
	}

	public void setReMarks(String reMarks) {
		this.reMarks = reMarks;
	}

	public Boolean getIsHis() {
		return isHis;
	}

	public void setIsHis(Boolean isHis) {
		this.isHis = isHis;
	}

}
