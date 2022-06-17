package net.gbicc.app.pfm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;

/** 
* 分公司绩效
*/
public interface PfmBranchResultService extends DaoService<PfmBranchResultEntity, String, PfmBranchResultRepository> {
	/**
	 * 绩效补录页面分公司绩效查询
	 * @param searchProjTypeId
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @return
	 */

    public Map<String, Object> queryBranchResult(String taskId, String indexCode, String indexName, Long page,
            Integer size);
	/**
	 * 读取绩效帮助模板信息
	 * @return
	 */
	public Map<String, Map<String,Object>> readPrmHelpInfo();
	
	/**
	 * 获取绩效维度标题
	 * @param titleRow 标题行
	 * @return
	 * @throws Exception
	 */
	public String[] getPfmSheetDimTitle(Row titleRow) throws Exception;
	
	/**
	 * 生成绩效数据补录excel操作对象
	 * @return
	 */
	public Workbook createPfmDataSuppWb(Workbook wb);
	
	/**
	 * 读取数据补录
	 * @param wb excel操作对象
	 * @param taskId 评估计划ID
	 * @throws Exception
	 */
	public void readPfmDataSupp(Workbook wb,String taskId,String taskBatch) throws Exception;
	
    /**
     * 更新员工流失率、犹豫期内电话回访成功率、新契约回访完成率、续期收费率、退（撤）保率指标信息
     * 
     * @param indexName
     *            指标
     * @param channelName
     *            渠道
     * @param excludeBranchMap
     *            不计算的分公司map
     */
    public String updateIndexInfo(String indexName, String channelName, Map<String, String> excludeBranchMap)
            throws Exception;

    /**
     * 查询绩效结果
     *
     * @param id
     *            评估计划ID
     * @param page
     *            第几页
     * @param size
     *            每页多少
     * @return
     * @throws Exception
     */
    public Map<String, Object> findPfmResult(String id, Long page, Integer size) throws Exception;

    /**
     * 查询绩效结果
     *
     * @param id
     *            计划ID
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findPfmResult(String id) throws Exception;

    /**
     * 设置其他的值
     * 
     * @param task
     *            任务
     * @param workbook
     * @return
     */
    public Workbook setOtherData(IrrTaskEntity task, Workbook workbook) throws Exception;

    /**
     * 导出绩效得分
     * 
     * @param id
     *            计划ID
     * @return
     */
    public void downloadPfmScore(String id, HttpServletResponse response) throws Exception;
}
