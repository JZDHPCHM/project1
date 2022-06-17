package net.gbicc.app.pfm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;
import net.gbicc.app.pfm.service.PfmDepaResultService;

/**
* 部门绩效结果
*
*/
@Controller
@RequestMapping("/pfm/depaResult")
public class PfmDepaResultController extends BootstrapRestfulCrudController<PfmDepaResultEntity, String, PfmDepaResultRepository, PfmDepaResultService> {

	private static final Logger LOG=LogManager.getLogger(PfmDepaResultController.class);
	/**
	 * 数据补录页面部门绩效查询
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryDepaResult.action")
	public Map<String, Object> queryDepaResult(HttpServletRequest request,String taskId, String indexCode, String indexName, Long page,
            Integer size) {
		try {
            return service.queryDepaResult(taskId, indexCode, indexName, page, size);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return (Map<String, Object>) new HashMap<String, Object>().put("flag", false);
		}
	}
}
