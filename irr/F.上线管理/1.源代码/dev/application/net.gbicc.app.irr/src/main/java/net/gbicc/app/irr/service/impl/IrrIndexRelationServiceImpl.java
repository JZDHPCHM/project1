package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexRelationService;

/**
* 指标关联指标
*
*/
@Service
@Transactional
public class IrrIndexRelationServiceImpl extends DaoServiceImpl<IrrIndexRelationEntity, String, IrrIndexRelationRepository> 
	implements IrrIndexRelationService {
	
	@Autowired private IrrIndexInfoService indexInfoService;

	@Override
	public Map<String, Object> saveIndexRela(String indexId, String indexCode, String indexName, String relaIds)
			throws Exception {
		/*删除历史数据*/
		List<IrrIndexRelationEntity> hisList = getRepository().findByIndexId(indexId);
		if(!CollectionUtils.isEmpty(hisList)) {
			getRepository().deleteByIndexId(indexId);
		}
		if(StringUtils.isBlank(relaIds)) {
			return IrrUtil.getMap(false, "关联指标ID为空");
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
		add(relaList);
		return IrrUtil.getMap(true, "保存成功！");
	}
	
	
}
