package gbicc.irs.debtRating.debt.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectRatingInfo {

	public String proName;//项目名称
	public String proType;//项目类型
	public String leaseChannel;
	public String zlbj;//租赁本金
	public String zlqx;//租赁期限
	public Double finalBs;//复评保障倍数
	public String ratingDate;//评级时间
	public String bsFws;//保障倍数分位数
	public Double internBs;//初评保障倍数
	public String finalName;//复评人
	public String internName;//初评人
	public String assetReview;//资产复核人
	public String levelBs;//资产复核人
	public String suggest;
	
	
	
	public List<Map<String,Object>>  zbfx= new ArrayList<Map<String,Object>>();
	public List<Map<String,Object>>  dxfx= new ArrayList<Map<String,Object>>();
	
	
	
	
	
	
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getLevelBs() {
		return levelBs;
	}
	public void setLevelBs(String levelBs) {
		this.levelBs = levelBs;
	}

	
	public List<Map<String, Object>> getDxfx() {
		return dxfx;
	}
	public void setDxfx(List<Map<String, Object>> list) {
		this.dxfx = list;
	}
	public List<Map<String, Object>> getZbfx() {
		return zbfx;
	}
	public void setZbfx(List<Map<String, Object>> zbfx) {
		this.zbfx = zbfx;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	public String getLeaseChannel() {
		return leaseChannel;
	}
	public void setLeaseChannel(String leaseChannel) {
		this.leaseChannel = leaseChannel;
	}
	public String getZlbj() {
		return zlbj;
	}
	public void setZlbj(String zlbj) {
		this.zlbj = zlbj;
	}
	public String getZlqx() {
		return zlqx;
	}
	public void setZlqx(String zlqx) {
		this.zlqx = zlqx;
	}
	public Double getFinalBs() {
		return finalBs;
	}
	public void setFinalBs(Double finalBs) {
		this.finalBs = finalBs;
	}
	public String getRatingDate() {
		return ratingDate;
	}
	public void setRatingDate(String ratingDate) {
		this.ratingDate = ratingDate;
	}
	public String getBsFws() {
		return bsFws;
	}
	public void setBsFws(String bsFws) {
		this.bsFws = bsFws;
	}
	public Double getInternBs() {
		return internBs;
	}
	public void setInternBs(Double internBs) {
		this.internBs = internBs;
	}
	public String getFinalName() {
		return finalName;
	}
	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
	public String getInternName() {
		return internName;
	}
	public void setInternName(String internName) {
		this.internName = internName;
	}
	public String getAssetReview() {
		return assetReview;
	}
	public void setAssetReview(String assetReview) {
		this.assetReview = assetReview;
	}
	
}
