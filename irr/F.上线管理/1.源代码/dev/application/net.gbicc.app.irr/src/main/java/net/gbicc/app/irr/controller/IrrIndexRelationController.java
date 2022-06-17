package net.gbicc.app.irr.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrIndexRelationService;

/**
* 指标关联指标
*
*/
@Controller
@RequestMapping("/irr/indexRela")
public class IrrIndexRelationController extends BootstrapRestfulCrudController<IrrIndexRelationEntity, String, IrrIndexRelationRepository, IrrIndexRelationService> {

	private static final Logger LOG = LogManager.getLogger(IrrIndexInfoController.class);
	
	/**
	 * 保存关联指标
	 * @param indexId 指标ID
	 * @param indexCode 指标编码
	 * @param indexName 指标名称
	 * @param relaIds 关联指标ID字符串，中间用,隔开
	 * @return
	 */
	@RequestMapping("/saveIndexRela.action")
	@ResponseBody
	public Map<String, Object> saveIndexRela(String indexId,String indexCode,String indexName,String relaIds){
		try {
			return service.saveIndexRela(indexId, indexCode, indexName, relaIds);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
}
