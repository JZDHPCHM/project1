package net.gbicc.app.irr.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;
import net.gbicc.app.irr.jpa.support.dto.WeightInfoDTO;

/**
* 评估项目得分
*/
public interface IrrProjResultService extends DaoService<IrrProjResultEntity, String, IrrProjResultRepository> {

	/**
	 * 计算权重
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrProjResultEntity> calcProjRate(IrrTaskEntity task) throws Exception;
	
	/**
	 * 计算小计<br/>
	 * 公式:(OR1+OR2+OR6+OR4+ORA+OR5+OR7*2+OR8+OR9)/9
	 * 改为科目得分乘以权重20200721
	 * @param dimProjResultMap 维度评估项目结果Map
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcOR(Map<String, IrrProjResultEntity> dimProjResultMap) throws Exception;
	
	/**
	 * 计算难以量化风险合计<br/>
	 * 公式:小计*操作风险的占难以量化风险的比重+战略风险*战略风险的占难以量化风险的比重+声誉风险*声誉风险的占难以量化风险的比重+流动性风险*流动性风险的占难以量化风险的比重
	 * @param dimProjResultMap 维度评估项目结果Map
	 * @param projMap 评估项目Map
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcDR(Map<String, IrrProjResultEntity> dimProjResultMap,Map<String, IrrProjTypeEntity> projMap) throws Exception;

	/**
	 * 计算量化风险<br/>
	 * 总分和直接确定可得为100，其他为0
	 * @param key 维度
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcQR(String key) throws Exception;
	
	/**
	 * 计算风险综合评级<br/>
	 * 公式:难以量化风险合计*难以量化风险合计占总风险的比重+量化风险*量化风险占总风险的比重
	 * @param dimProjResultMap 维度评估项目结果Map
	 * @param projMap 评估项目Map
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calcCR(Map<String, IrrProjResultEntity> dimProjResultMap,Map<String, IrrProjTypeEntity> projMap) throws Exception;

	/**
	 * 根据权重维度查询权重内容
	 * @param projTypeDim 权重维度
	 * @param taskId 评估项目ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findProjResultByDim(String taskId,String projTypeDim) throws Exception;

    /**
     * 根据权重维度查询权重内容
     * 
     * @param projTypeDim
     *            权重维度
     * @param taskId
     *            评估项目ID
     * @return
     * @throws Exception
     */
    public List<WeightInfoDTO> findAllProjResultByDim(String taskId, String projTypeDim) throws Exception;

	/**
	 * 编辑权重结果
	 * @param id 记录ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> editProjResult(String id) throws Exception;

    /**
     * 导出权重值
     * 
     * @param task
     *            任务信息
     * @param wb
     * @return
     */
    public Workbook exportProjResult(IrrTaskEntity task, Workbook wb) throws Exception;
}
