package gbicc.irs.warning.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.warning.jpa.entity.AftWarnInfoEntity;
import gbicc.irs.warning.jpa.repository.AftWarnInfoRepository;
import gbicc.irs.warning.jpa.vo.WarnResult;

/**
 * 预警信息表
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
public interface AftWarnInfoService extends DaoService<AftWarnInfoEntity, String, AftWarnInfoRepository> {

    /**
     * 查询预警大类
     * 
     * @param parentCode
     *            预警父编码
     * @return
     */
    public Map<String, Object> findWarnCate(String parentCode) throws Exception;

	boolean warningByCode(String warnCode);


	Map<String, String> findRuleName() throws Exception;

	Map<String, String> findCustName() throws Exception;

	//ResponseWrapper<WarnResult> warningList(Pageable pageable, HttpServletRequest req);



	Map<String, String> updateResult(Map<String, Object> warn) throws Exception;
	/**
	 * @param date2 
	 * @param date1 
	 * @param custName 
	 * @查询全部承租人
	 * @return
	 * @throws Exception
	 */
	ResponseWrapper<Map<String, Object>> queryAllLessee(Integer page, Integer rows, String custName, String date1, String date2) throws Exception;
	/**
	 * @根据承租人查询关联人
	 * @return
	 * @throws Exception
	 */
	ResponseWrapper<Map<String, Object>> findByAssoidY(String lesseeId,Integer page,Integer rows) throws Exception;

	boolean changeFocusOn(String status,String assoID) throws Exception;

	ResponseWrapper<Map<String, Object>> findByAssoidN(String lesseeId, Integer page, Integer rows) throws Exception;

	List<Map<String, Object>> groupWarn(String waterID,Integer page, Integer rows, String custType, String custname, String date1,
			String date2, String date3, String date4, String level, String status, String warnRule, String warnSmalt,
			String warnType, String dispResult, String assoName);

	/**
     * 查询预警历史是否存在记录
     *
     * @param custname
     * @param custType
     * @param level
     * @param warnRule
     * @param warnType
     * @param warnSmalt
     * @param employee
     * @param dispResult
     * @param assoName
     * @param date4
     * @param date3
     * @param date2
     * @param date1
     * @param fileName
     * @return
     */
    public Map<String, Object> queryHistoryCount(String custname, String custType, String level, String warnRule,
			String warnType, String warnSmalt, String employee, String dispResult, String assoName, String date4,
			String date3, String date2, String date1, String highWarn, String medWarn) throws Exception;

	/**
	 * 导出预警历史数据为excel
	 *
	 * @param waterId
	 * @param custname
	 * @param custType
	 * @param level
	 * @param warnRule
	 * @param warnType
	 * @param warnSmalt
	 * @param employee
	 * @param assoName
	 * @param date4
	 * @param date3
	 * @param date2
	 * @param date1
	 */
    public Map<String, Object> exportHistoryData(String custname, String custType, String level, String warnRule,
			String warnType, String warnSmalt, String employee, String dispResult, String assoName, String date4,
			String date3, String date2, String date1, String fileName, String highWarn, String medWarn)
			throws Exception;
    /**
     * 判断预警信号查询是否有数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @return
     */
    public Map<String, Object> queryCount(String waterId, String custname, String warnType, String warnSmalt, String level,
			String pushStatus, String custType, String warnRule, String date2, String date1, String highWarn,
			String medWarn);
    /**
     * /**
     * 导出预警信号数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @param fileName
     * @return
     */
    public Map<String, Object> exportData(String waterId, String custname, String warnType, String warnSmalt, String level,
			String pushStatus, String custType, String warnRule, String date2, String date1, String fileName,
			String highWarn, String medWarn);

	ResponseWrapper<WarnResult> warningList(String waterID, Integer page, Integer rows, String custType,
			String custname, String date1, String date2, String date3, String date4, String level, String status,
			String warnRule, String warnSmalt, String warnType, String dispResult, String assoName, String highWarn,
			String medWarn);


	Map<String, Object> changeFocusOnInter(Map<String, Object> map) throws Exception;

	

	



	
}
