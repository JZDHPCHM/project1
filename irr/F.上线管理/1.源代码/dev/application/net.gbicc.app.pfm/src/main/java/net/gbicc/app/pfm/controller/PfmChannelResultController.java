package net.gbicc.app.pfm.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;
import net.gbicc.app.pfm.service.PfmChannelResultService;

/**
* 渠道绩效结果
*
*/
@Controller
@RequestMapping("/pfm/channelResult")
public class PfmChannelResultController extends BootstrapRestfulCrudController<PfmChannelResultEntity, String, PfmChannelResultRepository, PfmChannelResultService> {

	private static final Logger LOG=LogManager.getLogger(PfmChannelResultController.class);
	/**
	 * 数据补录页面渠道绩效查询
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryChannelResult.action")
	public Map<String, Object> queryChannelResult(String taskId, String indexCode, String indexName, Long page,
            Integer size) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
            map = service.queryChannelResult(taskId, indexCode, indexName, page, size);
			 return map;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return (Map<String, Object>) new HashMap<String,Object>().put("flag", false);
		}
		
	}
	
}
