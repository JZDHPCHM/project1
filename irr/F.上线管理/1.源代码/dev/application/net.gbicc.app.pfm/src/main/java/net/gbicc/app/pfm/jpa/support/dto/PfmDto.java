package net.gbicc.app.pfm.jpa.support.dto;
/**
*	绩效临时对象
*/
public class PfmDto {

	/**
	 * 取得分的指标编码
	 */
	private String scoreIndexCode;
	/**
	 * 指标名称
	 */
	private String indexName;
	/**
	 * 权重
	 */
	private String weight;
	/**
	 * 标准分值
	 */
	private String stanValue;
	/**
	 * 序号
	 */
	private Integer sortNum;
	public String getScoreIndexCode() {
		return scoreIndexCode;
	}
	public void setScoreIndexCode(String scoreIndexCode) {
		this.scoreIndexCode = scoreIndexCode;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getStanValue() {
		return stanValue;
	}
	public void setStanValue(String stanValue) {
		this.stanValue = stanValue;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	
}
