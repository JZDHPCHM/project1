package gbicc.irs.debtRating.debt.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.commom.module.service.DownFile;
import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.repository.CreditRatingRepository;
import gbicc.irs.debtRating.debt.service.CreditRatingService;
import gbicc.irs.main.rating.vo.RatingInit;

@Controller
@RequestMapping("/irs/creditRating")
public class CreditRatingController extends 
SmartClientRestfulCrudController<CreditRating, String,CreditRatingRepository, CreditRatingService>{
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	DownFile down;
	
	@RequestMapping(value="insertModel", method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,String> insertModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type) throws Exception{
		Map<String,String> jsons = JSONObject.parseObject(map, Map.class);
		Map testmodel = service.insertModel(jsons,type,"000");
		return testmodel;
	}
	/**
	 * 测试债项模型
	 * @param map
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="insertFacilityModel", method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,String> insertFacilityModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type) throws Exception{
		Map<String,String> jsons = JSONObject.parseObject(map, Map.class);
		Map testmodel = service.insertFacilityModel(jsons,type,"000");
		return testmodel;
	}

	/**
	 * 增信模型测试
	 * @param map
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="testModel", method=RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> testModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type) throws Exception{
		Map<String,Object> jsons = JSONObject.parseObject(map, Map.class);
		Map testmodel = service.testmodel(jsons,type,"000");
		return testmodel;
	}
	/**
	 * 增信模型页面测算
	 * @param map
	 * @param type
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="startModel", method=RequestMethod.GET)
	@ResponseBody
	public CreditRating startModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type,@RequestParam(name="mainId") String mainId) throws Exception{
		
		Map<String,Object> jsons = JSONObject.parseObject(map, Map.class);
		CreditRating startModel = service.startModel(jsons,type,"000",mainId);
		return startModel;
	}
	
	/**
	 * 页面评级测算时，判断"xxx"客户“xxx”编号的项目是否存在初评，
	 * 如果存在初评，则返回 0，不能进行评级测算，如果不存在初评，则返回1，可以进行评级测算
	 * @param proCode	项目编号
	 * @param custCode	客户编号
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="isExitCredit", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> isExitAssets(String proCode,String custCode)
			throws Exception{
    	Map<String,String> data = new HashMap<String, String>();
    	String sql = "select count(1) as countNum From NS_CREDIT_RATING where "
    			+ "FD_CUST_CODE=? and FD_PROJECT_CODE=? and fd_rating_status='010' and fd_vaild='1'\n" ;
    	String countNum = jdbc.queryForObject(sql,String.class,custCode,proCode);
    	
    	if(Integer.parseInt(countNum)>0) {
    		data.put("isNext","0");// 该客户的“xxx”编号的项目正在进行初评，不能进行评级测算。
    	}else {
    		data.put("isNext","1");
    	}
    	return data;
    }
	
	/**
	 * 获取债项页面列表
	 * @param request
	 * @param response
	 * @param queryExampleEntity
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "parameterQuery", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			CreditRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQuery(request, response, queryExampleEntity, page, rows);
		return result;
	}
	/**
	 * 获取增信页面步骤输入框
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getCreditIndexInput", method=RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object> getCreditIndexInput() throws Exception{
		Map<String, Object> data = service.getCreditIndexInput();
		
		
		return data;
	}
	/**
	 * 创建增信评级记录
	 * @param bpCode
	 * @param proCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="creatCreditRating", method=RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object> creatCreditRating(String bpCode,String proCode) throws Exception{
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
		
		
		String ratingid = service.creatCreditRating(bpCode, proCode);
		data.put("status",true);
		data.put("data",ratingid);
		return data;
		
		
		
	}
	
	
	
		
}
