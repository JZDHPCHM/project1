package net.gbicc.app.irr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrChannelResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrChannelResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.service.IrrChannelResultService;

/**
*	渠道结果
*/
@Service
public class IrrChannelResultServiceImpl extends DaoServiceImpl<IrrChannelResultEntity, String, IrrChannelResultRepository> 
	implements IrrChannelResultService {

	@Autowired JdbcTemplate jdbcTemplate;
	
	@Override
	public List<IrrChannelResultEntity> summChannelResult(String taskId) throws Exception {
		String sql = "SELECT DISTINCT II.ID INDEX_ID,II.INDEX_CODE,II.INDEX_NAME,SI.CHANNEL_FLAG,UR.TASK_BATCH,UR.TASK_ID,UR.SPLIT_RESULT_Q2 INDEX_RESULT,UR.COLL_ORG_ID ORG_ID,UR.COLL_ORG_NAME ORG_NAME\r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"WHERE UR.TASK_ID='"+taskId+"' AND SI.SPLIT_LEVEL='"+IrrEnum.SPLIT_LEVEL_CHANNEL.getValue()+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.INDEX_LEVEL='"+IrrEnum.INDEX_LEVEL_BRANCH.getValue()+"'";
		List<IrrChannelResultEntity> channelResult = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrChannelResultEntity>(IrrChannelResultEntity.class));
		channelResult = add(channelResult);
		return channelResult;
	}

	@Override
	public List<IndexValueDTO> sumBranchChannelIndexResult(String taskId,String orgId) {
		String sql = "SELECT CR.INDEX_ID,CR.INDEX_CODE CODE,CR.INDEX_NAME NAME,SUM(NVL(CR.INDEX_RESULT,0)) VALUE FROM T_IRR_CHANNEL_RESULT CR \r\n" + 
				"INNER JOIN T_IRR_INDEX_INFO II ON CR.INDEX_ID=II.ID WHERE II.INDEX_RESULT_TYPE='"+IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue()+"' AND CR.TASK_ID='"+taskId+"' AND CR.ORG_ID='"+orgId+"' GROUP BY CR.INDEX_ID,CR.INDEX_CODE,CR.INDEX_NAME";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IndexValueDTO>(IndexValueDTO.class));
	}

}
