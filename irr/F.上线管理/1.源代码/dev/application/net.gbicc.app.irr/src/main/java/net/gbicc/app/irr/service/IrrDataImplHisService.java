package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrDataImplHisEntity;
import net.gbicc.app.irr.jpa.repository.IrrDataImplHisRepository;

/**
*	irr历史数据导入service
*/
public interface IrrDataImplHisService extends DaoService<IrrDataImplHisEntity, String, IrrDataImplHisRepository> {

	/**
	 * 上传zip包irr历史数据
	 * @param file
	 * @param year 年
	 * @param quarter 季度
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> readIrrHisDataImpl(MultipartFile file,String year,String quarter) throws Exception;
	
	/**
	 * poi读取excel文件
	 * @param fileName 文件名称
	 * @param taskBatch 任务期次
	 * @param wb 文档
	 * @throws Exception
	 */
	public List<IrrDataImplHisEntity> poiReadExcel(String fileName,String taskBatch,Workbook wb) throws Exception;
	
	/**
	 * 获取历史数据公司
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getHisDataOrg() throws Exception;
	
	/**
	 * 获取历史数据期次
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getHisDataTaskBatch() throws Exception;
	
	/**
	 * 生成所有wb
	 * @param taskBatch	期次
	 * @return
	 * @throws Exception
	 */
	public Map<String, Workbook> createReportAllExcel(String taskBatch) throws Exception;
	
	/**
	 * 根据条件查询历史数据
	 * @param taskBatch 期次
	 * @param xbrlCode	xbrl机构编码
	 * @param projTypeCode 评估项目编码
	 * @return
	 * @throws Exception
	 */
	public List<IrrDataImplHisEntity> findHisDataByCondhition(String taskBatch,String xbrlCode,String projTypeCode) throws Exception;

    /**
     * 查询历史数据
     * 
     * @param data
     *            查询信息
     * @param page
     *            第几页
     * @param size
     *            每页多少
     * @return
     */
    public Map<String, Object> findHisData(String data, Long page, Integer size) throws Exception;
	
}
 