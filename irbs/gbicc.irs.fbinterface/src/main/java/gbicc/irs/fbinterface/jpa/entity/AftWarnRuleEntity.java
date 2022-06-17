package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name="T_AFT_WARN_RULE")
public class AftWarnRuleEntity extends AuditorEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @Column(name="ID")
    private String id;
    /**
     * 规则编码
     */
    @Column(name="RULE_CODE")
    private String ruleCode;
    /**
     * 规则名称
     */
    @Column(name="RULE_NAME")
    private String ruleName;
    /**
     * 规则大类
     */
    @Column(name="RULE_TYPE")
    private String ruleType;
    /**
     * 规则小类
     */
    @Column(name="RULE_SUB_TYPE")
    private String ruleSubType;
    /**
     * 适用关联人类型
     */
    @Column(name="RELE_TYPE")
    private String releType;
    /**
     * 预警等级
     */
    @Column(name="WARN_LEVEL")
    private String warnLevel;
    /**
     * 规则跑批频率
     */
    @Column(name="FREQUENCY")
    private String frequency;
    /**
     * 触发机制描述
     */
    @Column(name="TRIG_DESC")
    private String trigDesc;
    /**
     * 是否生效
     */
    @Column(name="IS_VALID")
    private String isValid;
    /**
     * 是否已删除
     */
    @Column(name="IS_DELETE")
    private String isDelete;
    /**
     * 预警信号来源
     */
    @Column(name="SIGNAL_SOURCE")
    private String signalSource;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRuleCode() {
        return ruleCode;
    }
    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getRuleType() {
        return ruleType;
    }
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
    public String getRuleSubType() {
        return ruleSubType;
    }
    public void setRuleSubType(String ruleSubType) {
        this.ruleSubType = ruleSubType;
    }
    public String getReleType() {
        return releType;
    }
    public void setReleType(String releType) {
        this.releType = releType;
    }
    public String getWarnLevel() {
        return warnLevel;
    }
    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public String getTrigDesc() {
        return trigDesc;
    }
    public void setTrigDesc(String trigDesc) {
        this.trigDesc = trigDesc;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    public String getSignalSource() {
        return signalSource;
    }
    public void setSignalSource(String signalSource) {
        this.signalSource = signalSource;
    }
}
