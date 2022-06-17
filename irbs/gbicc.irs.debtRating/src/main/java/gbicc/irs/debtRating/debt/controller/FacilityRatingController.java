package gbicc.irs.debtRating.debt.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.service.impl.UserServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.gbicc.aicr.system.dto.UserInfoDTO;

import gbicc.irs.debtRating.debt.entity.FacilityRating;
import gbicc.irs.debtRating.debt.repository.FacilityRatingRepository;
import gbicc.irs.debtRating.debt.service.FacilityRatingService;

@Controller
@RequestMapping("/irs/facilityRating")
public class FacilityRatingController extends 
SmartClientRestfulCrudController<FacilityRating, String,FacilityRatingRepository, FacilityRatingService>{
	@Autowired
	private JdbcTemplate jdbc;
	
	
	@RequestMapping(value="testModel", method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,String> testModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type) throws Exception{
		Map<String,String> jsons = JSONObject.parseObject(map, Map.class);
		Map testmodel = service.testmodel(jsons,type,"000");
		return testmodel;
	}
	
	/**
	 * 页面评级测算时，判断"xxx"客户“xxx”编号的项目是否存在初评，
	 * 如果存在初评，则返回 0，不能进行评级测算，如果不存在初评，则返回1，可以进行评级测算
	 * @param proCode	项目编号
	 * @param custCode	客户编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="isExitFacility", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> isExitAssets(String proCode,String custCode)
			throws Exception{
    	Map<String,String> data = new HashMap<String, String>();
    	String sql = "select count(1) as countNum From NS_facility_RATING where "
    			+ "FD_CUST_CODE=? and FD_PROJECT_CODE=? and fd_rating_status='010' and fd_vaild='1'\n" ;
    	String countNum = jdbc.queryForObject(sql,String.class,custCode,proCode);
    	
    	if(Integer.parseInt(countNum)>0) {
    		data.put("isNext","0");// 该客户的“xxx”编号的项目正在进行初评，不能进行评级测算。
    	}else {
    		data.put("isNext","1");
    	}
    	return data;
    }
	
	@RequestMapping(value="startFacilityModel", method=RequestMethod.GET)
	@ResponseBody
	public  FacilityRating startFacilityModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type,String facilityId) throws Exception{
		Map<String,String> jsons = JSONObject.parseObject(map, Map.class);
		FacilityRating facilityRating = service.startFacilityModel(jsons,type,"000",facilityId);
		return facilityRating;
	}
	
	
	/**
	 * 创建债项评级记录
	 * @param bpCode
	 * @param proCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="creatFacilityRating", method=RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object> creatFacilityRating(String bpCode,String proCode,String mainId,String assetsId,String creditId) throws Exception{
		HashMap<String,Object> data = new HashMap<String,Object>();
		if(StringUtils.isEmpty(bpCode)) {
			data.put("status",false);
			data.put("msg","客户编号不能为空！");
			return data;
		}
		if(StringUtils.isEmpty(proCode)) {
			data.put("status",false);
			data.put("msg","项目编号不能为空！");
			return data;
		}
		
		
		String ratingid = service.creatFacilityRating(bpCode, proCode,mainId,assetsId,creditId);
		data.put("status",true);
		data.put("data",ratingid);
		return data;
		
		
		
	}
	
		
}
