package net.gbicc.app.xbrl.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.xbrl.jpa.entity.XbrlRegulatorMappingEntity;
import net.gbicc.app.xbrl.jpa.repository.XbrlRegulatorMappingRepository;

/**
*	监管机构映射service接口
*/
public interface XbrlRegulatorMappingService extends DaoService<XbrlRegulatorMappingEntity, String, XbrlRegulatorMappingRepository> {

	/**
	 * xbrl登录验证
	 * @param companyCode 机构编码 
	 * @param isRead	是否读取权限
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> xbrlRegister(String companyCode,Boolean isRead) throws Exception;
	
	/**
	 * xbrl报告请求（初始化或查看）
	 * @param companyCode 机构编码
	 * @param reportEndDate 报告截止日期，例如20190331(季末期次)
	 * @param reportType 报告类型
	 * @param isInit 是否初始化
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> xbrlReport(String companyCode,String reportEndDate,String reportType,Boolean isInit) throws Exception;
	
	/**
	 * irr报告（初始化或查看）
	 * @param companyCode 机构编码
	 * @param reportEndDate 报告截止日期，例如20190331(季末期次)
	 * @param isInit 是否初始化
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> irrXbrlReport(String companyCode,String reportEndDate,Boolean isInit) throws Exception;
}
