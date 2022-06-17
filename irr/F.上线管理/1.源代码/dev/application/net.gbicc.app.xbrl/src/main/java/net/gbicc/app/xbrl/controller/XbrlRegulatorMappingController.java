package net.gbicc.app.xbrl.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.xbrl.jpa.entity.XbrlRegulatorMappingEntity;
import net.gbicc.app.xbrl.jpa.repository.XbrlRegulatorMappingRepository;
import net.gbicc.app.xbrl.service.XbrlRegulatorMappingService;
import net.gbicc.app.xbrl.util.XbrlUtil;

/**
* 监管机构映射controller
*
*/
@Controller
@RequestMapping("/xbrl")
public class XbrlRegulatorMappingController extends BootstrapRestfulCrudController<XbrlRegulatorMappingEntity, String, XbrlRegulatorMappingRepository, XbrlRegulatorMappingService>{

	private static final Logger LOG = LogManager.getLogger(XbrlRegulatorMappingController.class);
	
	/**
	 * 初始化报告
	 * @param companyCode 机构编码
	 * @param taskBatch 任务期次
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/irr/initReport.action")
	public Map<String, Object> initXbrl(String companyCode,String taskBatch){
		try {
			return service.irrXbrlReport(companyCode, taskBatch, true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return XbrlUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 查看报告
	 * @param companyCode	机构编码
	 * @param taskBatch		季末期次
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/irr/viewReport.action")
	public Map<String, Object> viewReport(String companyCode,String taskBatch){
		try {
			return service.irrXbrlReport(companyCode, taskBatch, false);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return XbrlUtil.getMap(false, e.getMessage());
		}
	}
	
	
}
