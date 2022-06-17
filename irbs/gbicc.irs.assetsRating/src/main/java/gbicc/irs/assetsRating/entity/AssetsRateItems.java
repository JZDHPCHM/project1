package gbicc.irs.assetsRating.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;
import org.springframework.data.annotation.CreatedDate;
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name = "NS_ASSETS_RATE_ITEMS ")
public class AssetsRateItems implements Serializable {

	private static final long serialVersionUID = -3456809335174136601L;

	public AssetsRateItems() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "FD_ID", length = 36)
	private String id;
	// 评级ID
	@Column(name = "FD_ASSETS_RATE_ID")
	private String assetsRateId;
	// 项目编号
	@Column(name = "FD_PROJECT_NUMBER")
	private String projectNumber;
	// 指标编码
	@Column(name = "FD_CODE")
	private String code;
	// 指标值
	@Column(name = "FD_VALUE")
	private String value;
	// 是否有效
	@Column(name = "FD_VAILD")
	private String valid;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssetsRateId() {
		return assetsRateId;
	}
	public void setAssetsRateId(String assetsRateId) {
		this.assetsRateId = assetsRateId;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	@Override
	public String toString() {
		return "AssetsRateItems [id=" + id + ", assetsRateId=" + assetsRateId + ", projectNumber=" + projectNumber
				+ ", code=" + code + ", value=" + value + ", valid=" + valid + "]";
	}

	
}
