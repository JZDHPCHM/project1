package gbicc.irs.code.library.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import gbicc.irs.code.library.jpa.entity.CodeLibraryPojo;
import gbicc.irs.code.library.jpa.repository.CodeLibraryRepository;
import gbicc.irs.code.library.service.CodeLibraryService;

@Service("CodeLibraryService")
public class CodeLibraryServiceImpl extends DaoServiceImpl<CodeLibraryPojo,String,CodeLibraryRepository> 
	implements CodeLibraryService{
	
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public List<CodeLibraryPojo> findByCodeNo(String codeNo) throws Exception{
		return repository.findByCodeNo(codeNo);
	}

	@Override
	public String findByItemCode(String itemCode) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String,String>();
		if(StringUtils.isEmpty(itemCode)) return mapper.writeValueAsString(map);
		String sql = "select  FD_ITEM_CODE itemCode, FD_ITEM_NAME itemName"
				+ " from FR_SYS_FIX_CODE"
				+ " where FD_CODE_NO = '" + itemCode + "' order by FD_ITEM_NAME";
		if("IndustryType".contains(itemCode)){
			sql = "select  FD_ITEM_CODE itemCode, FD_ITEM_NAME||'--'||FD_ITEM_CODE itemName"
					+ " from FR_SYS_FIX_CODE"
					+ " where FD_CODE_NO = '" + itemCode + "' order by FD_ITEM_NAME";
		}
		
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		
		if(CollectionUtils.isEmpty(list)) return mapper.writeValueAsString(map);
//		list.stream().forEach(item -> {
//			json.put(item.getItemCode(),item.getItemName());
//		});
		
//		return mapper.writeValueAsString(map);
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map1 = list.get(i);
			if(i == list.size() - 1) {
				sb.append("\"").append(map1.get("itemCode").toString()).append("\"").append(":").append("\"").append(map1.get("itemName").toString()).append("\"");
				continue;
			}
			sb.append("\"").append(map1.get("itemCode").toString()).append("\"").append(":").append("\"").append(map1.get("itemName").toString()).append("\"").append(",");
			
		}
		
		sb.append("}");
		
		return sb.toString();
	}

	
}
