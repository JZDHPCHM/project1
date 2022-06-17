package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 工商资料
 * 
 * @author FC
 * @version v1.0 2019年9月11日
 */
@Entity
@Table(name = "FB_INTERFACE_IC_INF")
public class FbInterfaceIcInfEntity extends AuditorEntity implements Serializable {

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
     * 业务主管单位
     */
    @Column(name = "BIZ_HEAD_UNIT")
    private String bizHeadUnit;

    /**
     * 发照日期
     */
    @Column(name = "LICEN_DT")
    private String licenDt;

    /**
     * 发证机关
     */
    @Column(name = "ISSUCTF_ORG")
    private String issuctfOrg;

    /**
     * 吊销日期
     */
    @Column(name = "REVOKE_DT")
    private String revokeDt;

    /**
     * 名称
     */
    @Column(name = "COMPANY_NAME")
    private String companyName;

    /**
     * 地址
     */
    @Column(name = "COMPANY_ADDRESS")
    private String companyAddress;

    /**
     * 多证合一
     */
    @Column(name = "MORE_ONE")
    private String moreOne;

    /**
     * 成立日期
     */
    @Column(name = "ESTAB_DT")
    private String estabDt;

    /**
     * 执行事务合伙人
     */
    @Column(name = "EXEC_AFR_COPSN")
    private String execAfrCopsn;

    /**
     * 执行事务合伙人（委派代表）
     */
    @Column(name = "EXEC_AFR_COPSN_DELE")
    private String execAfrCopsnDele;

    /**
     * 投资人
     */
    @Column(name = "IVSTR")
    private String ivstr;

    /**
     * 标准化名称
     */
    @Column(name = "STD_NAME")
    private String stdName;

    /**
     * 核准日期
     */
    @Column(name = "STD_DT")
    private String stdDt;

    /**
     * 法代/执行
     */
    @Column(name = "FADE_EXEC")
    private String fadeExec;

    /**
     * 法定代表人
     */
    @Column(name = "LEGAL_REPR")
    private String legalRepr;

    /**
     * 注册号
     */
    @Column(name = "RGST_NO")
    private String rgstNo;

    /**
     * 注册资本币种
     */
    @Column(name = "RGST_CPTL_CCY")
    private String rgstCptlCcy;

    /**
     * 注册资本（万元）
     */
    @Column(name = "RGST_CPTL")
    private BigDecimal rgstCptl;

    /**
     * 注吊销日期
     */
    @Column(name = "NOTE_REVOKE_DT")
    private String noteRevokeDt;

    /**
     * 注销日期
     */
    @Column(name = "CNCL_DT")
    private String cnclDt;

    /**
     * 派出企业名称
     */
    @Column(name = "DISP_ETP_NAME")
    private String dispEtpName;

    /**
     * 登记时间
     */
    @Column(name = "REGST_DT")
    private String regstDt;

    /**
     * 登记机关
     */
    @Column(name = "REGST_ORG")
    private String regstOrg;

    /**
     * 登记状态
     */
    @Column(name = "REGST_STATUS")
    private String regstStatus;

    /**
     * 省市
     */
    @Column(name = "PROV_CITY")
    private String provCity;

    /**
     * 类型
     */
    @Column(name = "TP_NAME")
    private String tpName;

    /**
     * 类型代码
     */
    @Column(name = "TP_CODE")
    private String tpCode;

    /**
     * 经济性质
     */
    @Column(name = "ECON_PROP")
    private String econProp;

    /**
     * 经营期限自
     */
    @Column(name = "MG_DEDLN_START")
    private String mgDedlnStart;

    /**
     * 经营期限至
     */
    @Column(name = "MG_DEDLN_END")
    private String mgDedlnEnd;

    /**
     * 经营者
     */
    @Column(name = "OPERATOR")
    private String operator;

    /**
     * 经营范围
     */
    @Column(name = "MG_SCP")
    private String mgScp;

    /**
     * 统一社会信用代码
     */
    @Column(name = "UNIF_SOCI_CRED_CODE")
    private String unifSociCredCode;

    /**
     * 联系电话
     */
    @Column(name = "CTCT_TEL")
    private String ctctTel;

    /**
     * 评估等级
     */
    @Column(name = "EVAL_LVL")
    private String evalLvl;

    /**
     * 负责人
     */
    @Column(name = "CHRGTOR")
    private String chrgtor;

    /**
     * 迁入地工商局
     */
    @Column(name = "IN_INDU")
    private String inIndu;

    /**
     * 首席代表
     */
    @Column(name = "CHIEF_BHAF")
    private String chiefBhaf;

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

    public String getBizHeadUnit() {
        return bizHeadUnit;
    }

    public void setBizHeadUnit(String bizHeadUnit) {
        this.bizHeadUnit = bizHeadUnit == null ? null : bizHeadUnit.trim();
    }

    public String getLicenDt() {
        return licenDt;
    }

    public void setLicenDt(String licenDt) {
        this.licenDt = licenDt == null ? null : licenDt.trim();
    }

    public String getIssuctfOrg() {
        return issuctfOrg;
    }

    public void setIssuctfOrg(String issuctfOrg) {
        this.issuctfOrg = issuctfOrg == null ? null : issuctfOrg.trim();
    }

    public String getRevokeDt() {
        return revokeDt;
    }

    public void setRevokeDt(String revokeDt) {
        this.revokeDt = revokeDt == null ? null : revokeDt.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getMoreOne() {
        return moreOne;
    }

    public void setMoreOne(String moreOne) {
        this.moreOne = moreOne == null ? null : moreOne.trim();
    }

    public String getEstabDt() {
        return estabDt;
    }

    public void setEstabDt(String estabDt) {
        this.estabDt = estabDt == null ? null : estabDt.trim();
    }

    public String getExecAfrCopsn() {
        return execAfrCopsn;
    }

    public void setExecAfrCopsn(String execAfrCopsn) {
        this.execAfrCopsn = execAfrCopsn == null ? null : execAfrCopsn.trim();
    }

    public String getExecAfrCopsnDele() {
        return execAfrCopsnDele;
    }

    public void setExecAfrCopsnDele(String execAfrCopsnDele) {
        this.execAfrCopsnDele = execAfrCopsnDele == null ? null : execAfrCopsnDele.trim();
    }

    public String getIvstr() {
        return ivstr;
    }

    public void setIvstr(String ivstr) {
        this.ivstr = ivstr == null ? null : ivstr.trim();
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName == null ? null : stdName.trim();
    }

    public String getStdDt() {
        return stdDt;
    }

    public void setStdDt(String stdDt) {
        this.stdDt = stdDt == null ? null : stdDt.trim();
    }

    public String getFadeExec() {
        return fadeExec;
    }

    public void setFadeExec(String fadeExec) {
        this.fadeExec = fadeExec == null ? null : fadeExec.trim();
    }

    public String getLegalRepr() {
        return legalRepr;
    }

    public void setLegalRepr(String legalRepr) {
        this.legalRepr = legalRepr == null ? null : legalRepr.trim();
    }

    public String getRgstNo() {
        return rgstNo;
    }

    public void setRgstNo(String rgstNo) {
        this.rgstNo = rgstNo == null ? null : rgstNo.trim();
    }

    public String getRgstCptlCcy() {
        return rgstCptlCcy;
    }

    public void setRgstCptlCcy(String rgstCptlCcy) {
        this.rgstCptlCcy = rgstCptlCcy == null ? null : rgstCptlCcy.trim();
    }

    public BigDecimal getRgstCptl() {
        return rgstCptl;
    }

    public void setRgstCptl(BigDecimal rgstCptl) {
        this.rgstCptl = rgstCptl;
    }

    public String getNoteRevokeDt() {
        return noteRevokeDt;
    }

    public void setNoteRevokeDt(String noteRevokeDt) {
        this.noteRevokeDt = noteRevokeDt == null ? null : noteRevokeDt.trim();
    }

    public String getCnclDt() {
        return cnclDt;
    }

    public void setCnclDt(String cnclDt) {
        this.cnclDt = cnclDt == null ? null : cnclDt.trim();
    }

    public String getDispEtpName() {
        return dispEtpName;
    }

    public void setDispEtpName(String dispEtpName) {
        this.dispEtpName = dispEtpName == null ? null : dispEtpName.trim();
    }

    public String getRegstDt() {
        return regstDt;
    }

    public void setRegstDt(String regstDt) {
        this.regstDt = regstDt == null ? null : regstDt.trim();
    }

    public String getRegstOrg() {
        return regstOrg;
    }

    public void setRegstOrg(String regstOrg) {
        this.regstOrg = regstOrg == null ? null : regstOrg.trim();
    }

    public String getRegstStatus() {
        return regstStatus;
    }

    public void setRegstStatus(String regstStatus) {
        this.regstStatus = regstStatus == null ? null : regstStatus.trim();
    }

    public String getProvCity() {
        return provCity;
    }

    public void setProvCity(String provCity) {
        this.provCity = provCity == null ? null : provCity.trim();
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName == null ? null : tpName.trim();
    }

    public String getTpCode() {
        return tpCode;
    }

    public void setTpCode(String tpCode) {
        this.tpCode = tpCode == null ? null : tpCode.trim();
    }

    public String getEconProp() {
        return econProp;
    }

    public void setEconProp(String econProp) {
        this.econProp = econProp == null ? null : econProp.trim();
    }

    public String getMgDedlnStart() {
        return mgDedlnStart;
    }

    public void setMgDedlnStart(String mgDedlnStart) {
        this.mgDedlnStart = mgDedlnStart == null ? null : mgDedlnStart.trim();
    }

    public String getMgDedlnEnd() {
        return mgDedlnEnd;
    }

    public void setMgDedlnEnd(String mgDedlnEnd) {
        this.mgDedlnEnd = mgDedlnEnd == null ? null : mgDedlnEnd.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getMgScp() {
        return mgScp;
    }

    public void setMgScp(String mgScp) {
        this.mgScp = mgScp == null ? null : mgScp.trim();
    }

    public String getUnifSociCredCode() {
        return unifSociCredCode;
    }

    public void setUnifSociCredCode(String unifSociCredCode) {
        this.unifSociCredCode = unifSociCredCode == null ? null : unifSociCredCode.trim();
    }

    public String getCtctTel() {
        return ctctTel;
    }

    public void setCtctTel(String ctctTel) {
        this.ctctTel = ctctTel == null ? null : ctctTel.trim();
    }

    public String getEvalLvl() {
        return evalLvl;
    }

    public void setEvalLvl(String evalLvl) {
        this.evalLvl = evalLvl == null ? null : evalLvl.trim();
    }

    public String getChrgtor() {
        return chrgtor;
    }

    public void setChrgtor(String chrgtor) {
        this.chrgtor = chrgtor == null ? null : chrgtor.trim();
    }

    public String getInIndu() {
        return inIndu;
    }

    public void setInIndu(String inIndu) {
        this.inIndu = inIndu == null ? null : inIndu.trim();
    }

    public String getChiefBhaf() {
        return chiefBhaf;
    }

    public void setChiefBhaf(String chiefBhaf) {
        this.chiefBhaf = chiefBhaf == null ? null : chiefBhaf.trim();
    }
}