package net.gbicc.app.irr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjTypeRepository;
import net.gbicc.app.irr.service.IrrProjTypeService;

/**
* 评估项目
*/
@Service
@Transactional
public class IrrProjTypeServiceImpl extends DaoServiceImpl<IrrProjTypeEntity, String, IrrProjTypeRepository> 
	implements IrrProjTypeService {
	
	@Autowired private JdbcTemplate jdbcTemplate;

	@Override
	public List<IrrProjTypeEntity> findProjTypeSelect(Integer codeLength) throws Exception {
		String sql = "SELECT PT.* FROM T_IRR_PROJ_TYPE PT WHERE 1=1 ";
		if(codeLength != null) {
			sql += "AND LENGTH(PT.TYPE_CODE)="+codeLength;
		}
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrProjTypeEntity>(IrrProjTypeEntity.class));
	}

	@Override
	public Map<String, String> projTypeSelect(Integer codeLength) throws Exception {
		List<IrrProjTypeEntity> list = findProjTypeSelect(codeLength);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for(IrrProjTypeEntity projType : list) {
			map.put(projType.getId(), projType.getTypeName()+"("+projType.getTypeCode()+")");
		}
		return map;
	}

}
