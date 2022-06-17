package gbicc.irs.reportTemplate.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSONArray;

import gbicc.irs.reportTemplate.jpa.entity.FinAccountNorm;
import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;
import gbicc.irs.reportTemplate.jpa.repository.FinAccountNormRepository;
import gbicc.irs.reportTemplate.service.FinAccountNormService;
import gbicc.irs.reportTemplate.service.FinancialStatementTemplateService;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;

@Service("FinAccountNormService")
public class FinAccountNormServiceImpl extends DaoServiceImpl<FinAccountNorm, String, FinAccountNormRepository>
		implements FinAccountNormService {

	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	FinancialStatementTemplateService FinanService;

	@Override
	public List<FinAccountNorm> queryNorm() {
		List<Map<String, Object>> list = jdbc.queryForList(
				"select norm.FD_NAME,norm.FD_CODE,norm.FD_NORM,norm.FD_NORMCONFIG,norm.FD_NORMID,temp.FD_FS_TYPE from "
						+ "NS_FIN_ACCOUNT_NORM norm left join NS_RT_FINAN_STAT_TEMPLATE temp "
						+ "on norm.FD_NORMID = temp.FD_ID");
		List<FinAccountNorm> listObj = new ArrayList<FinAccountNorm>();
		for (Map<String, Object> map : list) {
			FinAccountNorm finaNorm = new FinAccountNorm();
			finaNorm.setFdCode(map.get("FD_CODE").toString());
			finaNorm.setFdName(map.get("FD_NAME").toString());
			finaNorm.setFdNorm(map.get("FD_OLDNORM").toString());
			finaNorm.setFdNormConfig(map.get("FD_OLDNORMCONFIG").toString());
			listObj.add(finaNorm);
		}
		return listObj;
	}

	@Override
	public Map<Object, Object> resurltSelect() {
		List<Map<String, Object>> list = jdbc
				.queryForList("select fd_fa_code,fd_fa_name from ns_rt_finan_account_mapp");
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Map<String, Object> mapz : list) {
			map.put(mapz.get("FD_FA_CODE"), mapz.get("FD_FA_NAME"));
		}
		return map;
	}

	@Override
	public void initNorm() throws Exception {
		FinancialStatementTemplate temp = new FinancialStatementTemplate();
		QueryParameter parameter = new QueryParameter();
		Page<FinancialStatementTemplate> listTem =FinanService.query(temp,parameter);;
				//jdbc.queryForList("select FD_ID,FD_FS_TYPE from NS_RT_FINAN_STAT_TEMPLATE");
		Map<String, FinancialStatementTemplate> map = new HashMap<>();
		for (FinancialStatementTemplate mapData : listTem) {
			map.put(mapData.getFsType(), mapData);
		}
		jdbc.update("delete from NS_FIN_ACCOUNT_NORM");
		StringBuffer strbuffer = new StringBuffer();
		Resource path =new DefaultResourceLoader().getResource("classpath:/framework/rule/ruleConfig.json");
		try {
			InputStream fis = path.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
			BufferedReader in = new BufferedReader(inputStreamReader);

			String str;
			while ((str = in.readLine()) != null) {
				strbuffer.append(str); // new String(str,"UTF-8")
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		FinAccountNorm finNorm = new FinAccountNorm();
		List<FinAccountNorm> savaList = new ArrayList<FinAccountNorm>();
		List<Object> list = JSONArray.parseArray(strbuffer.toString());
		for (Object object : list) {
			finNorm = new FinAccountNorm();
			Map<String, String> jsontoMap = (Map<String, String>) object;
			finNorm.setFdCode(jsontoMap.get("FD_CODE") == null ? "" : jsontoMap.get("FD_CODE"));
			finNorm.setFdName(jsontoMap.get("FD_NAME") == null ? "" : jsontoMap.get("FD_NAME"));
			finNorm.setFdNorm(jsontoMap.get("FD_NORM") == null ? "" : jsontoMap.get("FD_NORM"));
			finNorm.setFdNormConfig(jsontoMap.get("FD_NORMCONFIG") == null ? "" : jsontoMap.get("FD_NORMCONFIG"));
			finNorm.setFdNormId(map.get(jsontoMap.get("FD_FS_TYPE") == null ? "" : jsontoMap.get("FD_FS_TYPE")));
			savaList.add(finNorm);
		}
		repository.saveAll(savaList);
	}

	public static void main(String[] args) {

	}
}
