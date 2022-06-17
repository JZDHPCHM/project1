package gbicc.irs.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 客户财报科目数据 
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_FIN_ACCOUNT_DATA")
public class FinAccountData extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 8727038989454260034L;

	public FinAccountData() {}
	
	//主键
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;

	
	//主财报信息
	@Column(name="FD_REPORT_ID")
	private String finStatementId;
	
	//科目分类
	@Column(name="FD_ACCOUNT_CATEGORY",length=100)
	private String finStatementType;
	
	//行号代码	
	@Column(name="FD_LINE_CODE")
	private Integer lineCode;
		
	//数据项代码
	@Column(name="FD_DATAITEM_CODE", length=20)
	private String finStatementItem;
	
	//数据项名称
	@Column(name="FD_DATAITEM_NAME", length=50)
	private String itemDesc;
	
	//期初值
	@Column(name="FD_BEGING_VALUE")
	private Double beginValue;
	
	//期末值
	@Column(name="FD_END_VALUE")
	private Double amount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(Double beginValue) {
		this.beginValue = beginValue;
	}

	public String getFinStatementType() {
		return finStatementType;
	}

	public void setFinStatementType(String finStatementType) {
		this.finStatementType = finStatementType;
	}

	
	public Integer getLineCode() {
		return lineCode;
	}

	public void setLineCode(Integer lineCode) {
		this.lineCode = lineCode;
	}

	public String getFinStatementItem() {
		return finStatementItem;
	}

	public void setFinStatementItem(String finStatementItem) {
		this.finStatementItem = finStatementItem;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getFinStatementId() {
		return finStatementId;
	}

	public void setFinStatementId(String finStatementId) {
		this.finStatementId = finStatementId;
	}
	

}

