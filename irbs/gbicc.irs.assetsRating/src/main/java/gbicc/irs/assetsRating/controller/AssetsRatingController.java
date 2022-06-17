package gbicc.irs.assetsRating.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.assetsRating.entity.AssetsLeaseItem;
import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;
import gbicc.irs.assetsRating.service.AssetsLeaseItemService;
import gbicc.irs.assetsRating.service.AssetsRatingService;
import gbicc.irs.assetsRating.service.RatingAssetsStepService;
import gbicc.irs.commom.module.service.DownFile;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.vo.RatingInit;

@Controller
@RequestMapping("/irs/assetsRating")
public class AssetsRatingController extends 
SmartClientRestfulCrudController<AssetsRating, String,AssetsRatingRepository, AssetsRatingService>{
	@Autowired
	private RatingMainStepService ratingStepService;
	@Autowired
	private RatingAssetsStepService assetsStepService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	DownFile down;
	@Autowired
	private AssetsLeaseItemService assetsLeaseItemService;
	
	/**
	 * @资产评级发起页面
	 * @return
	 */
	@RequestMapping("assetsRatingLaunch.html")
	@MenuContributionItem("menuitem.irs.assetsRating.assetsRatingLaunch")
	public String ratingMainTask(){
		return "gbicc/irs/assetsRating/view/assetsRatingLaunch.html";
	}
	
	/**
	 * @资产评级台账发起页面
	 * @return
	 */
	@RequestMapping("assetsRatingStanding.html")
	@MenuContributionItem("menuitem.irs.assetsRating.assetsRatingStanding")
	public String assetsRatingStanding(){
		return "gbicc/irs/assetsRating/view/assetsParameter.html";
	}
	
	/**
	 * @资产评级步骤页面
	 * @return
	 */
	@RequestMapping("assesRatingStep")
	public String assetsRatingLaunchRatingSteps(){
		return "gbicc/irs/assetsRating/view/assetsRatingLaunchRatingSteps.html";
	}
	
	@RequestMapping("enterReport")
	public String enterReport(String custNo) {
		return "gbicc/irs/assetsRating/view/assetsRatingReport.html";
	}
	
	/**
     * 资产3.0报告页面
     *
     * @return
     */
    @RequestMapping("/assetsRatingReport")
    public String msgPush(String custNo) {
        return "gbicc/irs/assetsRating/view/assetsRatingReport.html";
    }
	/**
	 * 页面评级测算时，判断"xxx"客户“xxx”编号的项目是否存在初评，
	 * 如果存在初评，则返回 0，不能进行评级测算，如果不存在初评，则返回1，可以进行评级测算
	 * @param proCode	项目编号
	 * @param custCode	客户编号
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="isExitAssets", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> isExitAssets(String proCode,String custCode)
			throws Exception{
    	Map<String,String> data = new HashMap<String, String>();
    	String sql = "select count(1) as countNum From ns_assets_rating where "
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
	 * 评级试算
	 * @param stepId 评级步骤ID
	 * @param paramValue 定性指标值
	 * @return	试算
	 * @throws Exception
	 */
	@RequestMapping(value="testAssetsModel", method=RequestMethod.GET)
	@ResponseBody
	public  ResponseWrapper<AssetsRating> testAssetsModel(@RequestParam(name="map") String map,@RequestParam(name="type") String type,@RequestParam(name="mainId")String mainId) throws Exception{
		Map jsons = (Map) JSONObject.parse(map);
		Optional<AssetsRating> assetsRating = service.getRepository().findById(mainId);
		AssetsRating mainG = assetsRating.get();
		AssetsRating rating = service.testmodel(jsons,type,mainG,"000");
		System.out.println("1");
		return ResponseWrapperBuilder.crud(rating);
		
	}
	/**
	 * 根据客户编号、项目编号、模型类型创建评级
	 * @param bpCode  客户编号
	 * @param tempType 模型类型
	 * @param version
	 * @param proCode 项目编号
	 * @param ratingid 资产评级id（该id由上传excel之后接口返回的id）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="createAssetsRating", method=RequestMethod.GET)
	@ResponseBody
	public String createAssetsRating(String bpCode,String tempType,String version,String proCode,String ratingid) throws Exception{
		return service.createAssetsRating(bpCode, tempType, version,proCode,ratingid);
	}
	
	/**
	 * 根据评级Id获取评级对象
	 * @param ratingId 评级ID
	 * @throws Exception
	 */
	@RequestMapping(value="getAssetsRatingById", method=RequestMethod.GET)
	@ResponseBody
	public AssetsRating getAssetsRatingById(@RequestParam(name="ratingId")String ratingId,String tempType,String version) throws Exception{
		try {
			if(StringUtils.isEmpty(ratingId)) {
				return null;
			}
			AssetsRating companyRating = service.getRepository().findOne(ratingId);
			if(companyRating==null) {
				return null;
			}
				if(!tempType.equals(companyRating.getType())) {
					return null;
				}
//				if(!version.equals(companyRating.getFdVersion())) {
//					return null;
//				}
			if(companyRating.getSteps().size()>0) {
				 RatingAssetsStep ratingAssetsStep = companyRating.getSteps().get(0);
				if(StringUtils.hasText(companyRating.getCurrentStepId())) {
					ratingAssetsStep = assetsStepService.findById(companyRating.getCurrentStepId());
				}
				List<RatingAssetsStep> additionalStepByRatingId = assetsStepService.getAdditionalStepByRatingId(ratingId);
				companyRating.setSteps(additionalStepByRatingId);
				companyRating.setCurrentStep(ratingAssetsStep);
			}else {
				return null;
			}
			return companyRating;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
	}
	/**
	 * 资产发起评级列表数据接口
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
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQuery(request, response, queryExampleEntity, page, rows);
		return result;
	}
	/**
	 * 资产评级台账列表数据接口
	 * @param request
	 * @param response
	 * @param queryExampleEntity
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "parameterQueryHistory", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQueryHistory(request, response, queryExampleEntity, page, rows);
		return result;
	
	}
	
	/**
	 * @文件下载
	 * @param req
	 * @param resp
	 * @param name
	 * @throws Exception
	 */

	@RequestMapping(value="downFile", method=RequestMethod.GET)
	@ResponseBody
	public void downFile(HttpServletRequest req, HttpServletResponse resp,String name)
			throws Exception{
		name="assets";
		down.downFile(req, resp,name);
	}
	
	/**
	 * @文件上传
	 * @param file
	 * @param proCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="fileUpload", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> fileUpload(@RequestParam(name="file1",required=false)MultipartFile file,String proCode,HttpServletRequest request)
			throws Exception{
		String id = request.getParameter("id");//获取mainratingid
		String custCode = request.getParameter("custCode");//客户编号
		String proCode1 = request.getParameter("proCode");//项目编号
		String proName = request.getParameter("proName");//项目名称
		String custName = request.getParameter("custName");//客户名称
		String modelSteps = request.getParameter("modelSteps");//模型名称
		Map<String,String> result = new HashMap<String,String>();
		String path=down.fileUpload(file);//上传租赁物数据，保存文件，返回文件保存地址。
		InputStream is = new FileInputStream(new File(path));
		//POIFSFileSystem fs=newPOIFSFileSystem(new FileInputStream("d:/test.xls"));   
		//得到Excel工作簿对象    
		Workbook hssfWorkbook = null;
		if (path.endsWith("xlsx")) {
			hssfWorkbook = new XSSFWorkbook(is);
		} else if (path.endsWith("xls")) {
			hssfWorkbook = new HSSFWorkbook(is);
		}
		Sheet hssfSheet = hssfWorkbook.getSheetAt(0);// 获取下标为0的sheet也，该页面为项目基础信息
		//得到Excel工作表对象
		//HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		int lastRowNum = hssfSheet.getLastRowNum();
		
		//判断租赁物是否为空
		if(lastRowNum<1) {
			result.put("msg", "租赁物清单不能为空！");
 			result.put("flag", "false");
 			return result;
		}
		System.out.println("lastRowNum:"+lastRowNum);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		BigDecimal totalprice = new BigDecimal("0.00");//初始化总价
		BigDecimal isYprice = new BigDecimal("0.00");
		for(int i = 1; i<lastRowNum; i++) {
			Row row = hssfSheet.getRow(i);
			if(row!=null) {
				HashMap<String,Object> map = new HashMap<String, Object>();
				//String cell0 = proCode;
				String cell1 = row.getCell(0) == null ? "" : row.getCell(0).toString().replaceAll("\\s","");// 序号
				String cell2 = row.getCell(1) == null ? "" : row.getCell(1).toString().replaceAll("\\s","");// 名称
				String cell3 = row.getCell(2) == null ? "" : row.getCell(2).toString().replaceAll("\\s","");// 型号
				String cell4 = row.getCell(3) == null ? "" : row.getCell(3).toString().replaceAll("\\s","");// 计量单位
				String cell5 = row.getCell(4) == null ? "" : row.getCell(4).toString().replaceAll("\\s","");// 数量
				String cell6 = row.getCell(5) == null ? "" : row.getCell(5).toString().replaceAll("\\s","");// 合计
				String cell7 = row.getCell(6) == null ? "" : row.getCell(6).toString().replaceAll("\\s","");// 账面价值（原值）
				String cell8 = row.getCell(7) == null ? "" : row.getCell(7).toString().replaceAll("\\s","");// 账面价值（净值）
				// String cell9 = row.getCell(8) == null ? "" : row.getCell(8).toString().replace(" ","");// 是否核心租赁物
				Cell cell = row.getCell(8); // 是否核心租赁物
				if(StringUtils.isEmpty(cell)) {
					result.put("msg", "模板文件中\"是否核心租赁物\"列为空, 请检查模板");
		 			result.put("flag", "false");
		 			return result;
				}
				String cell9 = cell.toString();
				String cell10 = row.getCell(9) == null ? "" : row.getCell(9).toString().replaceAll("\\s","");// 价值评估方法
				String cell11 = row.getCell(10) == null ? "" : row.getCell(10).toString().replaceAll("\\s","");// 租赁物价值
				if(cell11.isEmpty()) {
					BigDecimal cell111 = new BigDecimal(0);
					totalprice = totalprice.add(cell111);//将总价加起来
					if("是".equals(cell9.trim())) { 
						isYprice = isYprice.add(new BigDecimal(0));
					}
				} else {
					BigDecimal cell111 = new BigDecimal(cell11);
					totalprice = totalprice.add(cell111);//将总价加起来
					if("是".equals(cell9.trim())) { 
						isYprice = isYprice.add(new BigDecimal(cell11));
					}
				}
				String cell12 = row.getCell(11) == null ? "" : row.getCell(11).toString();// 备注
				map.put("cell1", cell1);
				map.put("cell2", cell2);
				map.put("cell3", cell3);
				map.put("cell4", cell4);
				map.put("cell5", cell5);
				map.put("cell6", cell6);
				map.put("cell7", cell7);
				map.put("cell8", cell8);
				map.put("cell10", cell10);
				map.put("cell9", cell9);
				map.put("cell11", cell11);
				map.put("cell12", cell12);
				list.add(map);
				
			}
		}
		System.out.println("list:"+JSONObject.toJSON(list));
		//得到Excel工作表的行    
		  BigDecimal divide = isYprice.divide(totalprice,2,BigDecimal.ROUND_HALF_UP);
		  //如果小于0.6 结果为-1,如果等于0.6 结果为0 如果大于0.6 结果为1
		  int compareTo = divide.compareTo(new BigDecimal("0.6"));
		  System.out.println("是否大于60%："+compareTo);
		//得到Excel工作表指定行的单元格    
		//HSSFCell cell = row.getCell((short) j);  
		//cell.getCellStyle();//得到单元格样式 
		  
		String ratingid = null;
		if(compareTo<0) {
 			AssetsRating assetsRating =  new AssetsRating();
 			assetsRating.setId(UUID.randomUUID().toString());		
 			assetsRating.setCustCode(custCode);
 			assetsRating.setCustName(custName);
 			assetsRating.setProjectCode(proCode1);
 			assetsRating.setProjectName(proName);
 			assetsRating.setType(modelSteps);
 			assetsRating.setVaild("1");//将状态设置为已生效
 			assetsRating.setRatingVaild("1");//将状态设置为已发起。
 			assetsRating.setRatingStatus("000");
 			assetsRating.setFinalLevel("CⅣ");//将他的等级设置为最低等级
 			assetsRating.setFinalSco(new BigDecimal("0"));
 			assetsRating.setFinalPd(new BigDecimal("0"));
 			AssetsRating add = service.add(assetsRating);
 			ratingid = add.getId();
 			for (HashMap<String, Object> hashMap : list) {
				AssetsLeaseItem item = new AssetsLeaseItem();
				item.setId(UUID.randomUUID().toString());
				item.setAssetsRatingId(ratingid);
				item.setSerialNumber(valueOf(hashMap.get("cell1")));//序号
				item.setLeaseitemName(valueOf(hashMap.get("cell2")));
				item.setModel(valueOf(hashMap.get("cell3")));
				item.setMeasuringUnit(valueOf(hashMap.get("cell4")));
				item.setAmount(valueOf(hashMap.get("cell5")));
				item.setPurchasePrice(StringUtils.isEmpty(hashMap.get("cell6")) ? "0" : valueOf(hashMap.get("cell6")));
				item.setOriginalValue(StringUtils.isEmpty(hashMap.get("cell7")) ? "0" : valueOf(hashMap.get("cell7")));
				item.setNetWorth(valueOf(hashMap.get("cell8")));
				String isCoreLease = "";
				if("是".equals(valueOf(hashMap.get("cell9")))) {
					isCoreLease="Y";
				}else if("否".equals(valueOf(hashMap.get("cell9")))) {
					isCoreLease="N";
				}
				item.setIsCoreLease(isCoreLease);
				item.setValuationMethod(valueOf(hashMap.get("cell10")));
				item.setLeaseValue(valueOf(hashMap.get("cell11")));
				item.setComment(valueOf(hashMap.get("cell12")));
				assetsLeaseItemService.add(item);
			}
 			//将相同模型，相同项目的评级记录设置 已失效，已挂起。
 			if(!StringUtils.isEmpty(id)) {
 				jdbc.update("update ns_assets_rating set fd_vaild='0',fd_rating_vaild='0' where fd_id=? and fd_rating_status='000'", id);
 			}else {
 				jdbc.update("update ns_assets_rating set fd_vaild='0',fd_rating_vaild='0' where fd_project_code=? and fd_cust_code=? and fd_rating_status='000' and fd_id!=?",proCode1,custCode,ratingid);
 			}
 			result.put("msg", "核心租赁物占比不足60%”，评分为0，等级为CIV!");
 			result.put("flag", "false");
 			return result;
		}else {
			AssetsRating findById = null;
			/**
			 * 逻辑验证： 评级发起 先判断之前是否有过评级
			 * 如果有评级：先判断是否有等级如果有等级 说明之前 发起过评级 也进行了模型测算，如果用户点击发起评级之后，没有到模型测算哪一步那么该条评级 不需要覆盖，还需要保留
			 */
//			if(!StringUtils.isEmpty(id)) {
//				//如果id不为空，则修改租赁物清单表中的ratingid
//				findById = service.findById(id);
//				//1.如果评级记录有等级，将该条评级记录vaild设置为0，在重新生成评级记录
//				//2.如果评级没有等级，则直接删除，在重新生成评级记录
//				if(!StringUtils.isEmpty(findById.getInternLevel())) {
//					//等级不为空，将vaild设置为0，并重新发起评级
//					//findById.setVaild("0");
//					findById.setRatingVaild("0");//将状态设置为已挂起
//					service.update(findById.getId(), findById);
//				}else {
//					//等级为空，删除该条记录，并重新发起评级
//					service.getRepository().delete(findById);
//				}
//			}
				//如果为空，则先新增一条资产评级记录在
				findById = new AssetsRating();
				String uuid = UUID.randomUUID().toString();
				findById.setId(uuid);		
//				findById.setCustCode(custCode);
//				findById.setCustName(custName);
//				findById.setProjectCode(proCode1);
//				findById.setProjectName(proName);
//				findById.setType(modelSteps);
//				findById.setVaild("0");//将状态设置为未生效
//				findById.setRatingVaild("1");//将状态设置为已发起。
				
			
			//如果核心租赁物占比超过60%，则将租赁物清单保存入库
			//AssetsRating add = service.add(findById);//先将评级记录保存入库
				ratingid = uuid;
			for (HashMap<String, Object> hashMap : list) {
				AssetsLeaseItem item = new AssetsLeaseItem();
				item.setId(UUID.randomUUID().toString());
				item.setAssetsRatingId(ratingid);
				item.setSerialNumber(valueOf(hashMap.get("cell1")));//序号
				item.setLeaseitemName(valueOf(hashMap.get("cell2")));
				item.setModel(valueOf(hashMap.get("cell3")));
				item.setMeasuringUnit(valueOf(hashMap.get("cell4")));
				item.setAmount(valueOf(hashMap.get("cell5")));
				item.setPurchasePrice(StringUtils.isEmpty(hashMap.get("cell6")) ? "0" : valueOf(hashMap.get("cell6")));
				item.setOriginalValue(StringUtils.isEmpty(hashMap.get("cell7")) ? "0" : valueOf(hashMap.get("cell7")));
				item.setNetWorth(valueOf(hashMap.get("cell8")));
				String isCoreLease = "";
				if("是".equals(valueOf(hashMap.get("cell9")))) {
					isCoreLease="Y";
				}else if("否".equals(valueOf(hashMap.get("cell9")))) {
					isCoreLease="N";
				}
				item.setIsCoreLease(isCoreLease);
				item.setValuationMethod(valueOf(hashMap.get("cell10")));
				item.setLeaseValue(valueOf(hashMap.get("cell11")));
				item.setComment(valueOf(hashMap.get("cell12")));
				assetsLeaseItemService.add(item);
			}
		}
		result.put("id", ratingid);
		result.put("flag", "true");
		return result;
	}
	
	
	private String valueOf(Object obj) {
		return obj == null ? null : obj.toString();
	}

	/**
	 * @return 生效的客户评级
	 * @throws Exception
	 */
	@RequestMapping(value="findCustRating", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> findCustRating(String custCode) throws Exception{
		List<Map<String,Object>> map = jdbc.queryForList("select FD_ID,FD_FINAL_LEVEl,FD_TYPE from ns_main_rating where fd_vaild = '1' and FD_CUST_CODE=?",custCode);
		Map<String,String> mapStr = new HashMap<String, String>();
		if(map.size()>0) {
			mapStr.put("id", map.get(0).get("FD_ID")==null?"":map.get(0).get("FD_ID").toString());
			mapStr.put("level", map.get(0).get("FD_FINAL_LEVEl")==null?"":map.get(0).get("FD_FINAL_LEVEl").toString());
			mapStr.put("FD_TYPE", map.get(0).get("FD_TYPE")==null?"":map.get(0).get("FD_TYPE").toString());
		}
		return mapStr;
	}
	
	/**
	 * 获取项目编号或者项目名称
	 * @param col PROJECT_NUMBER  PROJECT_NAME 
	 * @return
	 */
	@RequestMapping(value = "SearchpRrojectNameOrCode", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> SearchBpMasterName(String col) {
		String sql = "select " + col + " from NS_PRJ_PROJECT ";
		List<Map<String, Object>> map = jdbc.queryForList(sql);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map2 : map) {
			for (String key : map2.keySet()) {
				Map<String, String> res = new HashMap<String, String>();
				if (map2.get(key) != null) {
					res.put("name", map2.get(key).toString());
					result.add(res);
				}
			}
		}
		return result;
		
		
		
	}
	
}
