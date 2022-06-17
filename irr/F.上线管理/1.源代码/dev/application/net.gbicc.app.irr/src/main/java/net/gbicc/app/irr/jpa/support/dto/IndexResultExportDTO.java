package net.gbicc.app.irr.jpa.support.dto;

import java.io.Serializable;

/**
* 指标结果
*/
public class IndexResultExportDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 行次
	 */
	private String indexLine;
	/**
	 * 指标名称
	 */
	private String indexName;
	/**
	 * 上一季度指标结果
	 */
	private String indexResultQ1;
	/**
	 * 上一季度指标得分
	 */
	private String indexScoreQ1;
	/**
	 * 当前季度指标结果
	 */
	private String indexResultQ2;
	/**
	 * 当前季度指标得分
	 */
	private String indexScoreQ2;
	/**
	 * 指标验证
	 */
	private String dataVali;

	public String getIndexLine() {
		return indexLine;
	}

	public void setIndexLine(String indexLine) {
		this.indexLine = indexLine;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexResultQ1() {
		return indexResultQ1;
	}

	public void setIndexResultQ1(String indexResultQ1) {
		this.indexResultQ1 = indexResultQ1;
	}

	public String getIndexResultQ2() {
		return indexResultQ2;
	}

	public void setIndexResultQ2(String indexResultQ2) {
		this.indexResultQ2 = indexResultQ2;
	}

	public String getDataVali() {
		return dataVali;
	}

	public void setDataVali(String dataVali) {
		this.dataVali = dataVali;
	}

	public String getIndexScoreQ1() {
		return indexScoreQ1;
	}

	public void setIndexScoreQ1(String indexScoreQ1) {
		this.indexScoreQ1 = indexScoreQ1;
	}

	public String getIndexScoreQ2() {
		return indexScoreQ2;
	}

	public void setIndexScoreQ2(String indexScoreQ2) {
		this.indexScoreQ2 = indexScoreQ2;
	}
	
}