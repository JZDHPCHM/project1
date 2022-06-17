package gbicc.irs.main.rating.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.engine.model.core.po.Model;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.MainRatingRepository;
import gbicc.irs.main.rating.service.MainRatingService;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.vo.RatingInit;

@Controller
@RequestMapping("/irs/mainRating")
public class MainRatingController extends
		SmartClientRestfulCrudController<MainRating, String,MainRatingRepository, MainRatingService> {

	@Autowired
	private RatingMainStepService ratingStepService;
	@Autowired
	private JdbcTemplate jdbc;
	/**
	 * 主体评级页面
	 * @return
	 */
	@RequestMapping("report")
	public String ratingMain3(){
		return "gbicc/irs/main/rating/mainRating3.html";
	}
	
	/**
	  * 主体评级试算页面
	  * @return
	  */
	 @RequestMapping("mainRatingTrial.html")
	 @MenuContributionItem("menuitem.irs.rating.companyrating")
	 public String mainRatingTrial(){
	  return "gbicc/irs/main/rating/mainRatingTrial.html";
	 }
	/**
	 * 主体评级页面
	 * @return
	 */
	@RequestMapping("mainRating.html")                                          
	public String ratingMain(){
		return "gbicc/irs/main/rating/mainRating.html";
	}
	
	/**
	 * @主体台账查询页面
	 * @return
	 */
	@RequestMapping("mainRatingParameter.html")
	@MenuContributionItem("menuitem.irs.rating.companyrating.task")
	public String ratingMainTask(){
		return "gbicc/irs/main/rating/mainRatingParameter.html";
	}
	
	/**
	 * @主体台账查询页面2.0
	 * @return
	 */
	@RequestMapping("mainRatingParameter2.html")
	@MenuContributionItem("menuitem.irs.rating.companyrating2.task")
	public String ratingMainTask2(){
		return "gbicc/irs/main/rating/mainRatingParameter2.html";
	}
	
	/**
	 * 3.0主体报告页面
	 * @return
	 */
	@RequestMapping("mainRatingReport")
	public String mainRatingReport(String custNo){
		return "gbicc/irs/main/rating/mainRatingReport.html";
	}
	
	/**
	 * 主体评级页面
	 * @return
	 */
	@RequestMapping("ratingSubsidiary")
	public String ratingSubsidiary(){
		return "gbicc/irs/main/rating/ratingSubsidiary.html";
	}
	
	
	/**
	 * 主体评级页面
	 * @return
	 */
	@RequestMapping("scoringDetails")
	public String analysisDlDx(){
		return "gbicc/irs/main/rating/scoringDetails.html";
	}
	
	/**
	 * 主体评级页面
	 * @return
	 * @throws Exception 
	 */
	@Value("${security.user.password}")
	String password;
	
	@RequestMapping("enterReport")
	public String enterReport(String custNo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*String referer=request.getHeader("Referer"); 
			 if(referer!=null){ 
				
			 }else{ 
				 return "";
			 } */
			 return "gbicc/irs/main/rating/mainRatingReport.html";
	}
	
	@RequestMapping("enterReport2")
	public String enterReport2(String custNo,HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*String referer=request.getHeader("Referer"); 
			 if(referer!=null){ 
				
			 }else{ 
				 return "";
			 } */
			 return "gbicc/irs/main/rating/enterReport.html";
	}
	
	/**
	 * 根据评级Id获取评级对象
	 * @param ratingId 评级ID
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingById", method=RequestMethod.GET)
	@ResponseBody
	public MainRating getRatingById(@RequestParam(name="ratingId")String ratingId,String tempType,String version) throws Exception{
		if(StringUtils.isEmpty(ratingId) || "null".equals(ratingId)) {
			return null;
		}
		MainRating companyRating = service.getRepository().findOne(ratingId);
			if(!tempType.equals(companyRating.getType())) {
				return null;
			}
//			if(!version.equals(companyRating.getFdVersion())) {
//				return null;
//			}
		if(companyRating.getSteps().size()>0) {
			RatingMainStep currentStep = companyRating.getSteps().get(0);
			if(StringUtils.hasText(companyRating.getCurrentStepId())) {
				currentStep = ratingStepService.findById(companyRating.getCurrentStepId());
			}
			companyRating.setSteps(ratingStepService.getAdditionalStepByRatingId(ratingId));
			companyRating.setCurrentStep(currentStep);
		}else {
			return null;
		}
		return companyRating;
	}
	
	
	/**
	 * 页面评级测算时，判断"xxx"客户“xxx”编号的项目是否存在初评，
	 * 如果存在初评，则返回 0，不能进行评级测算，如果不存在初评，则返回1，可以进行评级测算
	 * @param proCode  项目编号
	 * @param custCode 客户编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="isExitMainRating", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> isExitAssets(String custCode)
			throws Exception{
    	Map<String,String> data = new HashMap<String, String>();
    	String sql = "select count(1) as countNum From NS_MAIN_RATING where "
    			+ "FD_CUST_CODE=? and fd_rating_status='010' and fd_vaild='1'\n" ;
    	String countNum = jdbc.queryForObject(sql,String.class,custCode);
    	
    	if(Integer.parseInt(countNum)>0) {
    		data.put("isNext","0");// 该客户的“xxx”编号的项目正在进行初评，不能进行评级测算。
    	}else {
    		data.put("isNext","1");
    	}
    	return data;
    }
	
	/**
	 * 评级试算
	 * @param stepId 评级步骤ID
	 * @param paramValue 定性指标值
	 * @return	试算
	 * @throws Exception
	 */
	@RequestMapping(value="testModel", method=RequestMethod.POST)
	@ResponseBody
	public  ResponseWrapper<String> testModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type) throws Exception{
		Map jsons = (Map) JSONObject.parse(map);
		
		service.testmodel(jsons,type);
		return null;
		
	}
	
	
	/**
	 * 评级试算
	 * @param stepId 评级步骤ID
	 * @param paramValue 定性指标值
	 * @return	试算
	 * @throws Exception
	 */
	@RequestMapping(value="startTrial", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<MainRating> startTrial(
			@RequestParam(name="type")String type,
			@RequestParam(name="mainId")String mainId,
			@RequestParam(name="map") String map,
			@RequestParam(name="version") String version) throws Exception{
		Map jsons = (Map) JSONObject.parse(map);
		MainRating trialCalculation =service.startTrial(type,mainId, jsons,"000",version);
		return ResponseWrapperBuilder.crud(trialCalculation);
		
	}
	
	@RequestMapping(value="parameterQuery", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request,HttpServletResponse response,MainRating queryExampleEntity,Integer page,Integer rows) throws Exception{
		ResponseWrapper<RatingInit> result = service.parameterQuery(request, response, queryExampleEntity, page, rows);
		return  result;
	}
	
	@RequestMapping(value="parameterQueryNew", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQueryNew(HttpServletRequest request,HttpServletResponse response,MainRating queryExampleEntity,Integer page,Integer rows) throws Exception{
		ResponseWrapper<RatingInit> result = service.parameterQueryNew(request, response, queryExampleEntity, page, rows);
		return  result;
	}
	
	/**
	 * 查询历史评级
	 *
	 * @param custCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="parameterQueryHistory", method=RequestMethod.GET)
    @ResponseBody
    public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request,HttpServletResponse response,MainRating queryExampleEntity,Integer page,Integer rows) throws Exception{
		ResponseWrapper<RatingInit> result = service.parameterQueryHistory(request, response, queryExampleEntity, page, rows);
		return  result;
    }
	
	/**
	 * 查询历史评级2.0
	 *
	 * @param custCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="parameterQueryHistory2", method=RequestMethod.GET)
    @ResponseBody
    public ResponseWrapper<RatingInit> parameterQueryHistory2(HttpServletRequest request,HttpServletResponse response,MainRating queryExampleEntity,Integer page,Integer rows) throws Exception{
		ResponseWrapper<RatingInit> result = service.parameterQueryHistory2(request, response, queryExampleEntity, page, rows);
		return  result;
    }
	
	/**
     * 判断台账查询是否有数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @return
     */
    @RequestMapping(value="/queryCount.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryCount(String custName,String fdType,String industry,String highprecision,String employee,
            String finalName,String leaseOrganization,String method,String date1,String date2,String vailds){
        
        try {
            Map<String, Object> map = service.queryCount(custName,fdType,industry,highprecision,employee,finalName,leaseOrganization,method,date1,date2,vailds);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return AppUtil.getMap(false);
        }
    }
    /**
     * 导出台账查询数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @param fileName
     * @return
     */
    @RequestMapping(value="/exportData.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> exportData(String custName,String fdType,String industry,String highprecision,String employee,
            String finalName,String leaseOrganization,String method,String date1,String date2,String fileName,String vailds){
    	String loginName = SecurityUtil.getLoginName();
        try {
            Map<String, Object> map = service.exportData(loginName,custName,fdType,industry,highprecision,employee,finalName,leaseOrganization,method,date1,date2,fileName,vailds);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return AppUtil.getMap(false);
        }
    }
	
	@RequestMapping(value="Test", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String,String>> Test(String custNo) throws Exception{
		return null;
	}
	
	
}
