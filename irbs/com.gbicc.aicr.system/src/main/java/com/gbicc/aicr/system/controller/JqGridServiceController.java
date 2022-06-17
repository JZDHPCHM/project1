package com.gbicc.aicr.system.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.core.util.ObjectUtil;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.service.impl.UserServiceImpl;

import com.gbicc.aicr.system.flowable.entity.JqGrid;

import io.swagger.models.Model;

import com.gbicc.aicr.system.flowable.entity.ColModel;

@RestController
@RequestMapping(value="/JqGrid")
public class JqGridServiceController {
	@Autowired 
	private UserServiceImpl user;
	@Autowired
	JdbcTemplate jdbc;
	@Autowired private ApplicationContext applicationContext;

	@RequestMapping("test")
	public void cs() throws Exception{
		List<Map<String,Object>> list =jdbc.queryForList("select * from fr_aa_user where fd_loginname <>'admin'");
		for (Map<String, Object> map : list) {
			String oldPass =  map.get("FD_LOGINNAME").toString();
			String newPass=	oldPass.substring(0,1).toUpperCase() + oldPass.substring(1);
			Object passwordEncoder =applicationContext.getBean("passwordEncoder");
			String newPassJm = ObjectUtil.callMethod(String.class, passwordEncoder, "encode", new Class<?>[]{CharSequence.class}, newPass);
			jdbc.update("update fr_aa_user set fd_password=? where fd_id=?", newPassJm,map.get("FD_ID").toString());
		}
		
	}
	/**
	 * 主体-生产型-底表
	 * @return
	 */
	@RequestMapping("principalProductionType")
	public String principalProductionType(){
		jqGrid();
		ModelAndView mv = new ModelAndView();
		mv.addObject(jqGrid());
		return "gbicc/aicr/view/principalProductionType.html";
	}
	
	public String warningList(Integer start, Integer end, String sql) {
		sql = "select * from (select t.*,rownum as rn from (" + sql + ") t where rownum<=" + end + ") where rn>"
				+ start;
		return sql;
	}
	
	public Long count(String sql) {
		sql = "select count(*) from(" + sql + ")";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}
	public List<String> columnName() {
		String sql = "SELECT COLUMN_NAME FROM user_tab_columns where table_name = upper('T_NS_MAIN_RATING_PARTITION_SUM')";
		List<String> colName = jdbc.queryForList(sql, String.class);
		return colName;
	}
	
	
	/**
	 * 主体-事业部维度
	 * @return
	 */
	public ResponseWrapper<Map<String,Object>> queryReportMain(Integer page,Integer rows,String date1,String date2) throws Exception{
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(date2,date1, "data_date"));
			String sql = "select * from T_NS_MAIN_RATING_PARTITION_SUM where 1=1 "+sqlQuery;
			Integer size = rows;
			Integer number = page;
			Integer start = size * number - size;
			Integer end = size * number;
			String sqlpage = warningList(start, end, sql);
			List<Map<String,Object>> list = jdbc.queryForList(sqlpage);	
			Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
			ResponseWrapper<Map<String,Object>> re = ResponseWrapperBuilder.query(list);
			re.getResponse().setTotalPages((long) totalpager);
			re.getResponse().setTotalRows(count(sql));
			return re;
	}
	
	public static String sqlParDate(String date1,String date2,String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(date1)) {
			sqlQuery+= " and " + sqlCol + ">='" + date1.replace("-","").substring(0,6) + "'";
		}
		if (StringUtils.isNotBlank(date2)) {
			sqlQuery+= " and " + sqlCol + "<'" + date2.replace("-","").substring(0,6) + "'";
		}
		return sqlQuery;
	}
	@RequestMapping(value="queryReportMain", method=RequestMethod.GET)
	@ResponseBody
	public JqGrid jqGrid() {
		JqGrid jqGrid = new JqGrid();
		jqGrid.setColNames(columnName());
		List<ColModel> colArr = new ArrayList<ColModel>();
		for (int i = 0; i < columnName().size(); i++) {
			ColModel col = new ColModel();
			col.setName(columnName().get(i));
			col.setIndex(columnName().get(i));
			colArr.add(col);
		}
		jqGrid.setColModel(colArr);
		jqGrid.setUrl("/irs/reportMain/queryReportMain");
		
		//jqGrid.setData(queryReportMain());
		return jqGrid;
	}
}
