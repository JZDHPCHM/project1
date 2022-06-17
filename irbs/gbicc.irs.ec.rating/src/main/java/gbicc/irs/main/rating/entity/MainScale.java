package gbicc.irs.main.rating.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.main.rating.support.MainScaleJsonSerializer;

/**
 * 主标尺
 * @author ljc
 *
 */
@Entity
@Table(name = "NS_CFG_MAIN_SCALE")
@JsonSerialize(using=MainScaleJsonSerializer.class)
public class MainScale extends AuditorEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4607101624698690283L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;

	/**
	 * 主标尺等级
	 */
	@Column(name="FD_SCALE_LEVEL")
	private String scaleLevel;

	/**
	 * 违约概率
	 */
	@Column(name="FD_PD")
	private BigDecimal pd;

	/**
	 * 类型
	 */
	@Column(name="FD_TYPE")
	private String type;

	/**
	 * 排序
	 */
	@Column(name="FD_SORT")
	private Integer sort;
	
	//准入建议
	@Column(name="FD_ADMISSION_SUGGEST")
	private String admissionSuggest;

	public String getAdmissionSuggest() {
		return admissionSuggest;
	}

	public void setAdmissionSuggest(String admissionSuggest) {
		this.admissionSuggest = admissionSuggest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScaleLevel() {
		return scaleLevel;
	}

	public void setScaleLevel(String scaleLevel) {
		this.scaleLevel = scaleLevel;
	}

	public BigDecimal getPd() {
		return pd;
	}

	public void setPd(BigDecimal pd) {
		this.pd = pd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	
}
