package gbicc.irs.assetsRating.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.assetsRating.support.RatingAssetsStepJsonSerializer;
import gbicc.irs.main.rating.support.RatingStepType;

/**
 * 租赁物清单实体表
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_LEASEITEM_LIST")
public class AssetsLeaseItem extends AuditorEntity implements Serializable {

	private static final long serialVersionUID = 5996709246396242639L;
	
	public AssetsLeaseItem() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//评级对象
	@Column(name="ASSETS_RATING_ID",length=36)
	private String assetsRatingId;
	
	//序号
	@Column(name="SERIAL_NUMBER",length=36)
	private String serialNumber;
	
	//租赁物名称
	@Column(name="LEASEITEM_NAME",length=100)
	private String leaseitemName;
	
	//型号
	@Column(name="FD_MODEL",length=100)
	private String model;
	
	//计量单位
	@Column(name="MEASURING_UNIT",length=100)
	private String measuringUnit;
	
	//数量
	@Column(name="AMOUNT",length=100)
	private String amount;
	
	//购买价款-直租填写
	@Column(name="PURCHASE_PRICE",length=100)
	private String purchasePrice;
	
	//原值-回租填写
	@Column(name="ORIGINAL_VALUE",length=100)
	private String originalValue;

	//净值-回租填写
	@Column(name="NET_WORTH",length=100)
	private String netWorth;
	
	
	//是否核心租赁物（Y或N）
	@Column(name="ISCORELEASE",length=36)
	private String isCoreLease;
	
	//价值评估方法
	@Column(name="VALUATION_METHOD",length=36)
	private String valuationMethod;
	
	//租赁物价值（元）
	@Column(name="LEASE_VALUE",length=36)
	private String leaseValue;
	
	//备注
	@Column(name="FD_REMARK",length=2000)
	private String comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	} 

	public String getAssetsRatingId() {
		return assetsRatingId;
	}

	public void setAssetsRatingId(String assetsRatingId) {
		this.assetsRatingId = assetsRatingId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getLeaseitemName() {
		return leaseitemName;
	}

	public void setLeaseitemName(String leaseitemName) {
		this.leaseitemName = leaseitemName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMeasuringUnit() {
		return measuringUnit;
	}

	public void setMeasuringUnit(String measuringUnit) {
		this.measuringUnit = measuringUnit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(String netWorth) {
		this.netWorth = netWorth;
	}

	public String getIsCoreLease() {
		return isCoreLease;
	}

	public void setIsCoreLease(String isCoreLease) {
		this.isCoreLease = isCoreLease;
	}

	public String getValuationMethod() {
		return valuationMethod;
	}

	public void setValuationMethod(String valuationMethod) {
		this.valuationMethod = valuationMethod;
	}

	public String getLeaseValue() {
		return leaseValue;
	}

	public void setLeaseValue(String leaseValue) {
		this.leaseValue = leaseValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
