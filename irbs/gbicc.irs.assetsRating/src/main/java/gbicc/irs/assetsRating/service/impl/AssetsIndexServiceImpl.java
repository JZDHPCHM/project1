package gbicc.irs.assetsRating.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import gbicc.irs.assetsRating.entity.AssetsIndex;
import gbicc.irs.assetsRating.repository.AssetsIndexRepository;
import gbicc.irs.assetsRating.service.AssetsIndexService;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;
import gbicc.irs.main.rating.service.RatingSelectValuesConfigService;
import gbicc.irs.main.rating.support.RatingStepType;

@Service("AssetsIndexServiceImpl")
public class AssetsIndexServiceImpl extends DaoServiceImpl<AssetsIndex, String, AssetsIndexRepository>
		implements AssetsIndexService {

	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	RatingSelectValuesConfigService ratingSelectValuesConfigService;
	@Override
	public List<AssetsIndex> getRatingIndexsByStepId(String stepId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	/*	List<RatingIndex> indexs =  jdbc.query("select ratinginde0_.fd_id as \"id\", ratinginde0_.fd_create_date as \"create_date\", ratinginde0_.fd_creator as \"creator\", \r\n" + 
				"ratinginde0_.fd_last_modifier as \"last_modifier\", ratinginde0_.fd_last_modifydate as \"last_modifydate\", \r\n" + 
				"ratinginde0_.fd_dx_text as \"dx_text\", ratinginde0_.fd_indexcategory as \"indexcategory\", ratinginde0_.fd_indexcode as \"indexcode\", \r\n" + 
				"ratinginde0_.fd_configid as \"configid\", ratinginde0_.fd_indexid as \"indexid\", ratinginde0_.fd_indexname as \"indexname\", \r\n" + 
				"ratinginde0_.fd_score as \"score\", ratinginde0_.fd_indextype as \"indextype\", ratinginde0_.fd_indexvalue as \"indexvalue\", \r\n" + 
				"ratinginde0_.fd_weight as \"weight\", ratinginde0_.fd_level as \"level\", ratinginde0_.fd_parent_id as \"parent_id\", \r\n" + 
				"ratinginde0_.fd_qualitative_options as \"qualitative_op18\", ratinginde0_.fd_stepid as \"stepid\" from \r\n" + 
				"ns_rating_indexes ratinginde0_ left outer join ns_rating_step ratingmain1_ on ratinginde0_.fd_stepid=ratingmain1_.fd_id where ratingmain1_.fd_id=?  order by \r\n" + 
				"                     decode(fd_indexcode,'TYZB003',1),\r\n" + 
				"                     decode(fd_indexcode,'TYZB004',2),\r\n" + 
				"					           decode(fd_indexcode,'TYZB005',3),\r\n" + 
				"								decode(fd_indexcode, 'XJZT003', 3),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT002', 1),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT001', 2),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT004', 4),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT006', 5),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT007', 6),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT008', 7),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT009', 8),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT0010', 9),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT005', 10), + \r\n" + 
				"					           decode(fd_indexcode,'TYZB006',4),\r\n" + 
				"					           decode(fd_indexcode,'TYZB002',5),\r\n" + 
				"					           decode(fd_indexcode,'TYZB001',6),\r\n" + 
				"					           decode(fd_indexcode,'TYZB007',7),\r\n" + 
				"					           decode(fd_indexcode,'TYZB009',8),\r\n" + 
				"					           decode(fd_indexcode,'TYZB010',9),\r\n" + 
				"					           decode(fd_indexcode,'TYZB011',10),\r\n" + 
				"					           decode(fd_indexcode,'TYZB012',11),\r\n" + 
				"					           decode(fd_indexcode,'TYZB013',12),\r\n" + 
				"					           decode(fd_indexcode,'TYZB008',13)", new BeanPropertyRowMapper<RatingIndex>(RatingIndex.class),stepId);*/
				//repository.findByRatingStep_Id(stepId);
		List<AssetsIndex> indexs = repository.findByRatingStep_Id(stepId);
		List<AssetsIndex> resultIndexs =  new ArrayList<AssetsIndex>();;
		Map<String,String> optionsMap;
		for(AssetsIndex ri : indexs) {
			if(!(ri.getIndexCode().indexOf("TY")>-1&&ri.getIndexType().equals(RatingStepType.QUANTITATIVE))) {
				optionsMap= new HashMap<String,String>();
				List<RatingSelectValuesConfig> listOptions = ratingSelectValuesConfigService.getSelectValuesByDefId(ri.getIndexConfigId());
				for(RatingSelectValuesConfig va : listOptions) {
					optionsMap.put(va.getRealValue(), va.getDisplayValue());
				}
				ri.setOptions(mapper.writeValueAsString(optionsMap));
				resultIndexs.add(ri);
			}
		}
		return resultIndexs;
	}

	
	

}
