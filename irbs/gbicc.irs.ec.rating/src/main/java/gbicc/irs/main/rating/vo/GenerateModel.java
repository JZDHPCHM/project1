package gbicc.irs.main.rating.vo;

public class GenerateModel {
	//指标名称,指标编码,得分标准分值,得分比例,同业存量平均得分比例,行业偏离度;
	public String id;
	public String code;
	public String name;
	public String indextype;
	public String score;
	public String standardScore;
	public String scoreProportion;
	public String scoreAvg;
	public String scoreDivergence;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIndextype() {
		return indextype;
	}
	public void setIndextype(String indextype) {
		this.indextype = indextype;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
	public String getStandardScore() {
		return standardScore;
	}
	public void setStandardScore(String standardScore) {
		this.standardScore = standardScore;
	}
	public String getScoreProportion() {
		return scoreProportion;
	}
	public void setScoreProportion(String scoreProportion) {
		this.scoreProportion = scoreProportion;
	}
	public String getScoreAvg() {
		return scoreAvg;
	}
	public void setScoreAvg(String scoreAvg) {
		this.scoreAvg = scoreAvg;
	}
	public String getScoreDivergence() {
		return scoreDivergence;
	}
	public void setScoreDivergence(String scoreDivergence) {
		this.scoreDivergence = scoreDivergence;
	}
	
}
