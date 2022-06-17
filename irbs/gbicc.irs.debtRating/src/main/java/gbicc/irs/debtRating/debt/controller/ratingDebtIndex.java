package gbicc.irs.debtRating.debt.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jruby.ir.operands.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.commom.module.service.DownFile;
import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.repository.DebtIndexRepository;
import gbicc.irs.debtRating.debt.service.DebtIndexService;
import gbicc.irs.debtRating.debt.service.DebtRatingService;
import gbicc.irs.debtRating.debt.service.impl.DebtRatingStepServiceImpl;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.RatingStepType;

/**
 * 
 * @author wsh
 * @债项评级相关操作
 */
@Controller
@RequestMapping("/irs/ratingDebtIndex")
public class ratingDebtIndex extends
		SmartClientRestfulCrudController<RatingDebtIndex, String, DebtIndexRepository, DebtIndexService> {
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	DownFile down;
	
	@Autowired
	DebtRatingService debtRting;
	
	@Autowired
	DebtRatingStepServiceImpl debt;
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
		String path=down.fileUpload(file);//上传租赁物数据，保存文件，返回文件保存地址。
		Map<String,String> result = new HashMap<String,String>();
 		if(!StringUtils.isNotBlank(request.getParameter("ratingLevel"))) {
 			result.put("msg", "该客户未进行主体评级，无法发起债项评级！");
 			result.put("flag", "false");
 			return result;
 		}
		//String path ="C:\\Users\\wsh\\Desktop\\债项接口文档\\111\\债项评级录入模板.xlsx";
		String id=debt.prjInfo(path,proCode,request.getParameter("ratingLevel"));//获取债项评级的基础信息id
		if(!id.equals("")) {
			//备注:每一步都需要加异常处理
			debt.prjRealEstate(path, id);
			debt.prjOperatingFlow(path, id);
			debt.prjRaiseSexual(path, id);
			debt.prjLease(path, id);
			debt.leaseItem(path, id);
			debt.prjLegalPerson(path, id);
			debt.prjEquity(path, id);
			debt.prjAccount(path, id);
			debt.prjNaturalPerson(path, id);
		}else {
			//项目基础信息保存异常
		}
		String custName = request.getParameter("custName");
		String proName = request.getParameter("proName");
		String custCode = request.getParameter("custCode");
		DebtRating rating = new DebtRating();
		rating.setFdVersion("1.0");
		rating.setProjectCode(proCode);
		rating.setCustCode(custCode);
		rating.setCustName(custName);
		rating.setProjectName(proName);
		debtRting.getRepository().save(rating);
		//组装数据
   		Map<String,String> map = debt.assemblyData(id,rating);
   		if(map.size()>0) {
   			result.put("id", rating.getId());
 			result.put("flag", "true");
 			return result;
   		}
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
		name="debt";
		down.downFile(req, resp,name);
	}
	
	/**
	 * @根据评级步骤获取指标数据
	 * @param stepId 评级步骤ID
	 * @return 评级指标列表
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingDebtIndexsByStepId", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingDebtIndex> getRatingDebtIndexsByStepId(String stepId)
			throws Exception{
		List<RatingDebtIndex> indexs = service.getRatingIndexsByStepId(stepId);
		
		indexs = indexs.stream().filter(p -> 
		!p.getIndexCode().equals("CZX001")
		&&!p.getIndexCode().equals("P037")
		&&!p.getIndexCode().equals("P025")
				).
				collect(Collectors.toList());
		indexs = indexs.stream().filter(p -> 
		(p.getIndexType().toString().equals("INIT")||p.getParentId()!=null)||p.getIndexCode().equals("P036")).
				collect(Collectors.toList());


		
		indexs = indexs.stream().sorted(Comparator.comparing(RatingDebtIndex::getIndexCode,Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
		
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	
	
	/**
	 * 根据评级步骤获取指标数据
	 * @param stepId 评级步骤ID
	 * @return 评级指标列表
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByStepIdCheck", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingDebtIndex> getRatingIndexsByStepIdCheck(String rating)
			throws Exception{
		String sql = "select fd_id from ns_debt_step where fd_rateid = ? and fd_sno in('2','3','4','5')";
		List<String> step = jdbc.queryForList(sql,String.class,rating);
		List<RatingDebtIndex> indexsAll = new ArrayList<RatingDebtIndex>();
		List<RatingDebtIndex> indexs = new ArrayList<RatingDebtIndex>();
		for (String string : step) {
			indexs = service.getRatingIndexsByStepId(string);
			for (RatingDebtIndex ratingDebtIndex : indexs) {
				indexsAll.add(ratingDebtIndex);
			}
		}
		
		indexsAll = indexsAll.stream().filter(p -> 
		!p.getIndexCode().equals("CZX001")
		&&!p.getIndexCode().equals("P037")
		&&!p.getIndexCode().equals("P025")
				).
				collect(Collectors.toList());
		indexsAll = indexsAll.stream().filter(p -> 
		(p.getIndexType().toString().equals("INIT")||p.getParentId()!=null)||p.getIndexCode().equals("P036")).
				collect(Collectors.toList());


		
		indexsAll = indexsAll.stream().sorted(Comparator.comparing(RatingDebtIndex::getIndexCode,Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
		
		return  ResponseWrapperBuilder.query(indexsAll);
	}
	
	
	/**
	 * 根据评级步骤类型获取指标数据
	 * @param indexType 评级步骤类型 
	 * @return 模型指标
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingDebtIndexsByIndexType", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingDebtIndex> getRatingDebtIndexsByIndexType(RatingStepType indexType) throws Exception{
		List<RatingDebtIndex> indexs = service.getRatingIndexsByIndexType(indexType);
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	
	 
	/**
	 * @return 生效的客户评级
	 * @throws Exception
	 */
	@RequestMapping(value="findCustRating", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> findCustRating(String custCode,String proCode) throws Exception{
		List<Map<String,Object>> map = jdbc.queryForList("select FD_ID,FD_FINAL_LEVEl,FD_TYPE,cast(FD_SCO as DECIMAL(10,2)) FD_SCO from ns_main_rating where fd_vaild = '1' and fd_version='3.0' and FD_RATING_STATUS in('000','020') and FD_CUST_CODE=?",custCode);
		Map<String,String> mapStr = new HashMap<String, String>();
		if(map.size()>0) {
			mapStr.put("mainId", map.get(0).get("FD_ID")==null?"":map.get(0).get("FD_ID").toString());
			mapStr.put("mainLevel", map.get(0).get("FD_FINAL_LEVEl")==null?"":map.get(0).get("FD_FINAL_LEVEl").toString());
			mapStr.put("mainType", map.get(0).get("FD_TYPE")==null?"":map.get(0).get("FD_TYPE").toString());
			mapStr.put("mainSco", map.get(0).get("FD_SCO")==null?"":map.get(0).get("FD_SCO").toString());
		}
		if(StringUtils.isEmpty(proCode)) {
			mapStr.put("assetsId", "");
			mapStr.put("assetsLevel", "");
			mapStr.put("assetsSco", "");
			mapStr.put("assetsPd", "");
		}else {
			String sql="select FD_ID,FD_FINAL_LEVEl,cast(FD_FINAL_SCO as DECIMAL(10,2)) FD_FINAL_SCO,FD_FINAL_PD from ns_assets_rating where FD_PROJECT_CODE=? and fd_vaild='1' and FD_RATING_STATUS in('000','020')";
			List<Map<String,Object>> assetsRating = jdbc.queryForList(sql, proCode);
			if(assetsRating.size()>0) {
				mapStr.put("assetsId", assetsRating.get(0).get("FD_ID")==null?"":assetsRating.get(0).get("FD_ID").toString());
				mapStr.put("assetsLevel", assetsRating.get(0).get("FD_FINAL_LEVEl")==null?"":assetsRating.get(0).get("FD_FINAL_LEVEl").toString());
				mapStr.put("assetsSco", assetsRating.get(0).get("FD_FINAL_SCO")==null?"":assetsRating.get(0).get("FD_FINAL_SCO").toString());
				mapStr.put("assetsPd", assetsRating.get(0).get("FD_FINAL_PD")==null?"":assetsRating.get(0).get("FD_FINAL_PD").toString());
			}
			String prosql = "select nvl(FINANCE_AMOUNT,'0')-nvl(MARGIN,'0') DL_FXCK from NS_PRJ_PROJECT  where PROJECT_NUMBER=?";
			Map<String, Object> proMap = jdbc.queryForMap(prosql, proCode);
			if(MapUtils.isNotEmpty(proMap)) {
				mapStr.put("DL_FXCK", proMap.get("DL_FXCK").toString());
			}else {
				mapStr.put("DL_FXCK", "0");
			}
		}
		
		return mapStr;
	}
	
	
}
