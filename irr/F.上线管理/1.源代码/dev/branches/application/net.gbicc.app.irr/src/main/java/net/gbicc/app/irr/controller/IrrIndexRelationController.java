package net.gbicc.app.irr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexRelationService;

/**
* 指标关联指标
*
*/
@Controller
@RequestMapping("/irr/indexRela")
public class IrrIndexRelationController extends BootstrapRestfulCrudController<IrrIndexRelationEntity, String, IrrIndexRelationRepository, IrrIndexRelationService> {

	private static final Logger LOG = LogManager.getLogger(IrrIndexInfoController.class);
	
	@Autowired
	private IrrIndexInfoService indexInfoService;
	
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
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			/*删除历史数据*/
			List<IrrIndexRelationEntity> orgList = service.getRepository().findByIndexId(indexId);
			if(orgList == null || orgList.size() <= 0) {
				service.getRepository().deleteByIndexId(indexId);
			}
			if(StringUtils.isBlank(relaIds)) {
				map.put("flag", true);
				return map;
			}
			/*关联ID去重*/
			Set<String> set = new HashSet<String>();
			String[] relaArr = relaIds.split(",");
			for(String str : relaArr) {
				set.add(str);
			}
			/*保存*/
			Iterator<String> setIt = set.iterator();
			List<IrrIndexRelationEntity> relaList = new ArrayList<IrrIndexRelationEntity>();
			while(setIt.hasNext()) {
				String id = setIt.next();
				IrrIndexRelationEntity entity = new IrrIndexRelationEntity();
				entity.setIndexId(indexId);
				entity.setIndexCode(indexCode);
				entity.setIndexName(indexName);
				IrrIndexInfoEntity index = indexInfoService.findById(id);
				entity.setRelaId(index.getId());
				entity.setRelaCode(index.getIndexCode());
				entity.setRelaName(index.getIndexName());
				relaList.add(entity);
			}
			service.add(relaList);
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			map.put("data", e.getMessage());
			return map;
		}
		return map;
	}
}
