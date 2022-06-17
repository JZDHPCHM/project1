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

import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrSplitRelationRepository;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrSplitRelationService;

/**
* 拆分指标controller
*
*/
@Controller
@RequestMapping("/irr/splitRela")
public class IrrSplitRelationController extends BootstrapRestfulCrudController<IrrSplitRelationEntity, String, IrrSplitRelationRepository, IrrSplitRelationService> {

	private static final Logger LOG = LogManager.getLogger(IrrSplitRelationController.class);
	
	@Autowired
	private IrrSplitIndexService irrSplitIndexService;
	
	/**
	 * 保存拆分关联指标
	 * @param splitId 拆分指标ID
	 * @param splitCode 拆分指标编码
	 * @param splitName 拆分指标名称
	 * @param relaIds 拆分指标关联指标ID（中间用，隔开）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSplitRela.action")
	public Map<String,Object> saveSplitRela(String splitId,String splitCode,String splitName,String relaIds){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(StringUtils.isBlank(splitId) || StringUtils.isBlank(relaIds)) {
				LOG.error("拆分指标ID为空，无法保存");
				map.put("flag", false);
			}
			service.getRepository().deleteBySplitId(splitId);
			/*去重*/
			Set<String> idSet = new HashSet<String>();
			String[] idArr = relaIds.split(",");
			for(String str : idArr) {
				idSet.add(str);
			}
			Iterator<String> it = idSet.iterator();
			List<IrrSplitRelationEntity> list = new ArrayList<IrrSplitRelationEntity>();
			while(it.hasNext()) {
				String id = it.next();
				IrrSplitRelationEntity temp = new IrrSplitRelationEntity();
				temp.setSplitId(splitId);
				temp.setSplitCode(splitCode);
				temp.setSplitName(splitName);
				IrrSplitIndexEntity rela = irrSplitIndexService.findById(id);
				temp.setRelaId(rela.getId());
				temp.setRelaCode(rela.getSplitCode());
				temp.setRelaName(rela.getSplitName());
				list.add(temp);
			}
			service.add(list);
			map.put("flag", true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("flag",false);
		}
		return map;
	}
}
