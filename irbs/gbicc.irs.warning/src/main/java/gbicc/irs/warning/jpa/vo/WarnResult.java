package gbicc.irs.warning.jpa.vo;

/**
    * t_view_warn 实体类
    * Tue Sep 24 15:09:39 GMT+08:00 2019 WangShuaiHeng
    */ 
public class WarnResult{
	private String id;
	private String rulecode;
	private String rulename;
	private String taskseqno;
	private String lesseeid;
	private String warnsignlevel;
	private String warncateid;
	private String warnsubid;
	private String warndesc;
	private String assotype;
	private String assoid;
	private String warntime;
	private String dispresult;
	private String disptime;
	private String pushStatus;
	private String businessProcess;
	private String assoName;
	
	
	public String getAssoName() {
		return assoName;
	}
	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}
	public String getBusinessProcess() {
		return businessProcess;
	}
	public void setBusinessProcess(String businessProcess) {
		this.businessProcess = businessProcess;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRulecode() {
		return rulecode;
	}
	public void setRulecode(String rulecode) {
		this.rulecode = rulecode;
	}
	public String getRulename() {
		return rulename;
	}
	public void setRulename(String rulename) {
		this.rulename = rulename;
	}
	public String getTaskseqno() {
		return taskseqno;
	}
	public void setTaskseqno(String taskseqno) {
		this.taskseqno = taskseqno;
	}
	public String getLesseeid() {
		return lesseeid;
	}
	public void setLesseeid(String lesseeid) {
		this.lesseeid = lesseeid;
	}
	public String getWarnsignlevel() {
		return warnsignlevel;
	}
	public void setWarnsignlevel(String warnsignlevel) {
		this.warnsignlevel = warnsignlevel;
	}
	public String getWarncateid() {
		return warncateid;
	}
	public void setWarncateid(String warncateid) {
		this.warncateid = warncateid;
	}
	public String getWarnsubid() {
		return warnsubid;
	}
	public void setWarnsubid(String warnsubid) {
		this.warnsubid = warnsubid;
	}
	public String getWarndesc() {
		return warndesc;
	}
	public void setWarndesc(String warndesc) {
		this.warndesc = warndesc;
	}
	public String getAssotype() {
		return assotype;
	}
	public void setAssotype(String assotype) {
		this.assotype = assotype;
	}
	public String getAssoid() {
		return assoid;
	}
	public void setAssoid(String assoid) {
		this.assoid = assoid;
	}
	public String getWarntime() {
		return warntime;
	}
	public void setWarntime(String warntime) {
		this.warntime = warntime;
	}
	public String getDispresult() {
		return dispresult;
	}
	public void setDispresult(String dispresult) {
		this.dispresult = dispresult;
	}
	public String getDisptime() {
		return disptime;
	}
	public void setDisptime(String disptime) {
		this.disptime = disptime;
	}
	public String getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(String pushStatus) {
		this.pushStatus = pushStatus;
	}
	
	
}

