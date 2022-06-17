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
 * 工商资料-曾用名
 * 
 * @author FC
 * @version v1.0 2019年9月11日
 */
@Entity
@Table(name = "FB_INTERFACE_IC_INF_USEDNAME")
public class FbInterfaceIcInfUsednameEntity extends AuditorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36)
    private String id;

    /**
     * 统一社会信用代码、注册号
     */
    @Column(name = "COMPANY_ID")
    private String companyId;

    /**
     * 工商资料ID
     */
    @Column(name = "IC_INF_ID")
    private String icInfId;

    /**
     * 曾用名
     */
    @Column(name = "USED_NAME")
    private String usedName;

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

    public String getIcInfId() {
        return icInfId;
    }

    public void setIcInfId(String icInfId) {
        this.icInfId = icInfId == null ? null : icInfId.trim();
    }

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName == null ? null : usedName.trim();
    }
}