package gbicc.irs.main.rating.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CustRatingInfo{
	private String fdcustcode;
	private String custName;
	private String finalLevel;
	private String admissionSuggest;
	private String internName;
	private String internLevel;
	private String finalName;
	private String sco;
	private String bl;
	private String firRep;
	private String type;
	private String internDate;
	private String finalAdvice;
	private String modelName;
	private String ratingType;
	private String ranking;
	private String park;
	
	private List<GenerateModel> generate = new ArrayList<GenerateModel>();
	private List<ScoreDetail> scoreDetail = new ArrayList<ScoreDetail>();
	
	
	
	
	
	
	
	public String getPark() {
		return park;
	}
	public void setPark(String park) {
		this.park = park;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getRatingType() {
		return ratingType;
	}
	public void setRatingType(String ratingType) {
		this.ratingType = ratingType;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getFdcustcode() {
		return fdcustcode;
	}
	public void setFdcustcode(String fdcustcode) {
		this.fdcustcode = fdcustcode;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getFinalLevel() {
		return finalLevel;
	}
	public void setFinalLevel(String finalLevel) {
		this.finalLevel = finalLevel;
	}
	public String getAdmissionSuggest() {
		return admissionSuggest;
	}
	public void setAdmissionSuggest(String admissionSuggest) {
		this.admissionSuggest = admissionSuggest;
	}
	public String getInternName() {
		return internName;
	}
	public void setInternName(String internName) {
		this.internName = internName;
	}
	public String getInternLevel() {
		return internLevel;
	}
	public void setInternLevel(String internLevel) {
		this.internLevel = internLevel;
	}
	public String getFinalName() {
		return finalName;
	}
	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInternDate() {
		return internDate;
	}
	public void setInternDate(String internDate) {
		this.internDate = internDate;
	}
	public String getFinalAdvice() {
		return finalAdvice;
	}
	public void setFinalAdvice(String finalAdvice) {
		this.finalAdvice = finalAdvice;
	}
	public List<GenerateModel> getGenerate() {
		return generate;
	}
	public void setGenerate(List<GenerateModel> generate) {
		this.generate = generate;
	}
	public List<ScoreDetail> getScoreDetail() {
		return scoreDetail;
	}
	public void setScoreDetail(List<ScoreDetail> scoreDetail) {
		this.scoreDetail = scoreDetail;
	}
	public String getSco() {
		return sco;
	}
	public void setSco(String sco) {
		this.sco = sco;
	}
	public String getFirRep() {
		return firRep;
	}
	public void setFirRep(String firRep) {
		this.firRep = firRep;
	}
	public String getBl() {
		return bl;
	}
	public void setBl(String bl) {
		this.bl = bl;
	}
}

