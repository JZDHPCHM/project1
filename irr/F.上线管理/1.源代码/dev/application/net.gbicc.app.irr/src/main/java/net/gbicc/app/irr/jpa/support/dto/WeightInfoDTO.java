package net.gbicc.app.irr.jpa.support.dto;
/**
* 权重内容临时类
*/

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;

public class WeightInfoDTO {
	
	/**
	 * 评估项目权重结果
	 */
	private IrrProjResultEntity projResult;
	/**
	 * 北京
	 */
	private String beijing;
	/**
	 * 天津
	 */
	private String tianjin;
	/**
	 * 辽宁
	 */
	private String liaoning;
	/**
	 * 大连
	 */
	private String dalian;
	/**
	 * 江苏
	 */
	private String jiangsu;
	/**
	 * 山东
	 */
	private String shandong;
	/**
	 * 青岛
	 */
	private String qingdao;
	/**
	 * 河南
	 */
	private String henan;
	/**
	 * 广东
	 */
	private String guangdong;
	/**
	 * 四川
	 */
	private String sichuan;
	
	public IrrProjResultEntity getProjResult() {
		return projResult;
	}
	public void setProjResult(IrrProjResultEntity projResult) {
		this.projResult = projResult;
	}
	public String getBeijing() {
		return beijing;
	}
	public void setBeijing(String beijing) {
		this.beijing = beijing;
	}
	public String getTianjin() {
		return tianjin;
	}
	public void setTianjin(String tianjin) {
		this.tianjin = tianjin;
	}
	public String getLiaoning() {
		return liaoning;
	}
	public void setLiaoning(String liaoning) {
		this.liaoning = liaoning;
	}
	public String getDalian() {
		return dalian;
	}
	public void setDalian(String dalian) {
		this.dalian = dalian;
	}
	public String getJiangsu() {
		return jiangsu;
	}
	public void setJiangsu(String jiangsu) {
		this.jiangsu = jiangsu;
	}
	public String getShandong() {
		return shandong;
	}
	public void setShandong(String shandong) {
		this.shandong = shandong;
	}
	public String getQingdao() {
		return qingdao;
	}
	public void setQingdao(String qingdao) {
		this.qingdao = qingdao;
	}
	public String getHenan() {
		return henan;
	}
	public void setHenan(String henan) {
		this.henan = henan;
	}
	public String getGuangdong() {
		return guangdong;
	}
	public void setGuangdong(String guangdong) {
		this.guangdong = guangdong;
	}
	public String getSichuan() {
		return sichuan;
	}
	public void setSichuan(String sichuan) {
		this.sichuan = sichuan;
	}
}
