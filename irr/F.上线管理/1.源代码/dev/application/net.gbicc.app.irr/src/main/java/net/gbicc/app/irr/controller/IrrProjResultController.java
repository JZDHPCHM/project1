package net.gbicc.app.irr.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrProjResultService;

/**
* 评估项目得分
*/
@Controller
@RequestMapping("/irr/projResult")
public class IrrProjResultController extends BootstrapRestfulCrudController<IrrProjResultEntity, String, IrrProjResultRepository, IrrProjResultService> {

	private static final Logger LOG = LogManager.getLogger(IrrProjResultController.class);
	
	@Autowired private IrrProjResultService irrProjResultService;
	
	/**
	 * 查询权重维度内容
	 * @param taskId 评估计划ID
	 * @param projTypeDim 评估项目多维度
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findProjResult.action")
	public Map<String, Object> findProjResult(String taskId,String projTypeDim){
		try {
			return irrProjResultService.findProjResultByDim(taskId,projTypeDim);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 在线编辑权重值
	 * @param id 记录ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editProjResult.action")
	public Map<String, Object> editProjResult(String id){
		try {
			return irrProjResultService.editProjResult(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping("/test.action")
	public void test() {
		try {
//			OR1 理赔业务线
//			OR2 财务管理
//			OR6 资金运用业务线
//			OR4 公司治理业务线
//			ORA 销售、承保、保全业务线
//			OR5 准备金、再保险管理
//			OR7 信息系统
//			OR8 案件管理
//			OR9 战略风险
			Map<String, IrrProjResultEntity> dimProjResultMap=new HashMap<String,IrrProjResultEntity>();
			IrrProjResultEntity entityOR1=new IrrProjResultEntity();
			entityOR1.setTotalScore(new BigDecimal("69.35"));
			dimProjResultMap.put("OR1", entityOR1);
			
			IrrProjResultEntity entityOR2=new IrrProjResultEntity();
			entityOR2.setTotalScore(new BigDecimal("65.00"));
			dimProjResultMap.put("OR2", entityOR2);
			
			IrrProjResultEntity entityOR6=new IrrProjResultEntity();
			entityOR6.setTotalScore(new BigDecimal("84.04"));
			dimProjResultMap.put("OR6", entityOR6);
			
			IrrProjResultEntity entityOR4=new IrrProjResultEntity();
			entityOR4.setTotalScore(new BigDecimal("80.00"));
			dimProjResultMap.put("OR4", entityOR4);
			
			IrrProjResultEntity entityORA=new IrrProjResultEntity();
			entityORA.setTotalScore(new BigDecimal("0"));
			dimProjResultMap.put("ORA", entityORA);
			
			IrrProjResultEntity entityOR5=new IrrProjResultEntity();
			entityOR5.setTotalScore(new BigDecimal("95.00"));
			dimProjResultMap.put("OR5", entityOR5);
			
			IrrProjResultEntity entityOR7=new IrrProjResultEntity();
			entityOR7.setTotalScore(new BigDecimal("74.00"));
			dimProjResultMap.put("OR7", entityOR7);
			
			IrrProjResultEntity entityOR8=new IrrProjResultEntity();
			entityOR8.setTotalScore(new BigDecimal("90.90"));
			dimProjResultMap.put("OR8", entityOR8);
			
			IrrProjResultEntity entityOR9=new IrrProjResultEntity();
			entityOR9.setTotalScore(new BigDecimal("76.00"));
			dimProjResultMap.put("OR9", entityOR9);
			service.calcOR(dimProjResultMap);
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}

}
