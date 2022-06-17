package net.gbicc.app.irr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexOptionRepository;
import net.gbicc.app.irr.service.IrrIndexOptionService;

/**
*指标选项
*/
@Service
public class IrrIndexOptionServiceImpl extends DaoServiceImpl<IrrIndexOptionEntity, String, IrrIndexOptionRepository> 
	implements IrrIndexOptionService {
	
	@Autowired private JdbcTemplate jdbcTemplate;

	@Override
	public List<IrrIndexOptionEntity> findAllMaxPoints() {
		String sql = "SELECT ROT.* FROM (SELECT RANK() OVER(PARTITION BY INDEX_ID ORDER BY OPTION_SCORE DESC) RO,OP.* FROM T_IRR_INDEX_OPTION OP) ROT WHERE ROT.RO=1";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrIndexOptionEntity>(IrrIndexOptionEntity.class));
	}

}
