package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjTypeRepository;
import net.gbicc.app.irr.jpa.support.dto.JqGridTreeNodeDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
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
        String sql = "SELECT PT.* FROM T_IRR_PROJ_TYPE PT WHERE PT.IS_USE='" + IrrEnum.YESNO_Y.getValue() + "' ";
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
        Map<String, String> map = new LinkedHashMap<String, String>();
		for(IrrProjTypeEntity projType : list) {
			map.put(projType.getId(), projType.getTypeName()+"("+projType.getTypeCode()+")");
		}
		return map;
	}

	@Override
	public IrrProjTypeEntity findProjByCondition(String childCode) throws Exception {
		String sql = "SELECT PAR.* FROM T_IRR_PROJ_TYPE PAR INNER JOIN T_IRR_PROJ_TYPE CHIL ON PAR.ID=CHIL.PID WHERE CHIL.TYPE_CODE='"+childCode+"'";
		List<IrrProjTypeEntity> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrProjTypeEntity>(IrrProjTypeEntity.class));
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<IrrProjTypeEntity> findProjChildByParentCode(String parentCode) throws Exception {
		if(StringUtils.isBlank(parentCode)) {
			return null;
		}
		String sql = "SELECT PT.* FROM T_IRR_PROJ_TYPE PT INNER JOIN T_IRR_PROJ_TYPE PT2 ON PT.PID=PT2.ID WHERE PT2.TYPE_CODE='"+parentCode+"'";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrProjTypeEntity>(IrrProjTypeEntity.class));
	}

	@Override
	public List<JqGridTreeNodeDTO> findJqGridTreeProjType(String typeCode) throws Exception {
		if(StringUtils.isBlank(typeCode)) {
			typeCode = IrrEnum.JSTREE_ROOT.getValue();
		}
		List<IrrProjTypeEntity> list = getRepository().findByPCode(typeCode);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<JqGridTreeNodeDTO> returnList = new ArrayList<JqGridTreeNodeDTO>();
		for(IrrProjTypeEntity proj : list) {
			JqGridTreeNodeDTO dto = new JqGridTreeNodeDTO();
			dto.setObj(proj);
			dto.setLevel(0);
			if(IrrEnum.YESNO_Y.getValue().equals(proj.getIsLeaf())) {
				dto.setIsLeaf(true);
				dto.setLoaded(true);
			}else {
				dto.setIsLeaf(false);
			}
			dto.setParent(proj.getpCode());
			returnList.add(dto);
		}
		return returnList;
	}

	@Override
	public Map<String, String> projTypeCodeSelect(Integer codeLength) throws Exception {
		List<IrrProjTypeEntity> list = findProjTypeSelect(codeLength);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
        Map<String, String> map = new LinkedHashMap<String, String>();
		for(IrrProjTypeEntity projType : list) {
			map.put(projType.getTypeCode(), projType.getTypeName()+"("+projType.getTypeCode()+")");
		}
		return map;
	}

    @Override
    public Map<String, String> fiveProjTypeSelect() throws Exception {
        String sql = "SELECT * FROM T_IRR_PROJ_TYPE WHERE TYPE_CODE IN ('OR11','LR01','SR01','OR17','OR16')";
        List<IrrProjTypeEntity> list = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<IrrProjTypeEntity>(IrrProjTypeEntity.class));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (IrrProjTypeEntity projType : list) {
            map.put(projType.getId(), projType.getTypeName() + "(" + projType.getTypeCode() + ")");
        }
        return map;
    }

}
