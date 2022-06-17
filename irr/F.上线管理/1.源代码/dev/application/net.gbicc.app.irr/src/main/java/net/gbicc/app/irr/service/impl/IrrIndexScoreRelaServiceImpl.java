package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexScoreRelaEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexScoreRelaRepository;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexScoreRelaService;

/**
* 指标得分关联指标
*/
@Service
@Transactional
public class IrrIndexScoreRelaServiceImpl extends DaoServiceImpl<IrrIndexScoreRelaEntity, String, IrrIndexScoreRelaRepository> 
	implements IrrIndexScoreRelaService{
	
	@Autowired private IrrIndexInfoService indexInfoService;
	@Autowired private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> saveScoreRela(String indexId, String indexCode, String indexName, String relaIds)
			throws Exception {
		/*删除历史数据*/
		List<IrrIndexScoreRelaEntity> hisList = getRepository().findByIndexId(indexId);
		if(!CollectionUtils.isEmpty(hisList)) {
			getRepository().deleteByIndexId(indexId);
		}
		if(StringUtils.isBlank(relaIds)) {
			return IrrUtil.getMap(true, "保存成功！");
		}
		/*关联ID去重*/
		Set<String> set = new HashSet<String>();
		String[] relaArr = relaIds.split(",");
		for(String str : relaArr) {
			set.add(str);
		}
		/*保存*/
		Iterator<String> setIt = set.iterator();
		List<IrrIndexScoreRelaEntity> relaList = new ArrayList<IrrIndexScoreRelaEntity>();
		while(setIt.hasNext()) {
			String id = setIt.next();
			IrrIndexScoreRelaEntity entity = new IrrIndexScoreRelaEntity();
			entity.setIndexId(indexId);
			entity.setIndexCode(indexCode);
			entity.setIndexName(indexName);
			if (StringUtils.isNotBlank(id)) {
				IrrIndexInfoEntity index = indexInfoService.findById(id);
				entity.setRelaId(index.getId());
				entity.setRelaCode(index.getIndexCode());
				entity.setRelaName(index.getIndexName());
			}
			relaList.add(entity);
		}
		add(relaList);
		return IrrUtil.getMap(true, "保存成功");
	}

	@Override
	public List<String> findRelaIndexCodeByCondition(String indexId) throws Exception {
		String sql = "SELECT DISTINCT RELA_CODE FROM T_IRR_INDEX_SCORE_RELA WHERE INDEX_ID='"+indexId+"'";
		return jdbcTemplate.queryForList(sql, String.class);
	}
	
	
}
