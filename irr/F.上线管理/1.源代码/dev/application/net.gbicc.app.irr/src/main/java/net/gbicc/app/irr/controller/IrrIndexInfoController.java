package net.gbicc.app.irr.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.mvc.protocol.response.Response;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexInfoRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.dto.JsTreeNodeDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.sf.json.JSONObject;

/**
* 指标信息
*/
@Controller
@RequestMapping("/irr/indexInfo")
public class IrrIndexInfoController extends BootstrapRestfulCrudController<IrrIndexInfoEntity, String, IrrIndexInfoRepository, IrrIndexInfoService> {

	private static final Logger LOG = LogManager.getLogger(IrrIndexInfoController.class);
	
	@Autowired private IrrSplitIndexService irrSplitIndexService;
	@Autowired private IrrIndexInfoService irrIndexInfoService;
	
	/**
	 * 保存指标信息
	 * @param request
	 * @param indexInfo 指标信息
	 * @param projTypeId 评估项目编码
	 * @param projTypeName 评估项目名称
	 * @return
	 */
	@RequestMapping(value="/saveIndexInfo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveOptionBeforeIndexInfo(HttpServletRequest request,String indexInfo){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(StringUtils.isBlank(indexInfo)) {
				map.put("flag", false);
				map.put("data", "指标信息为空！");
				return map;
			}
			JSONObject object = JSONObject.fromObject(indexInfo);
			IrrIndexInfoEntity res = null;
			IrrIndexInfoEntity tempEntity = (IrrIndexInfoEntity) JSONObject.toBean(object, IrrIndexInfoEntity.class);
			if(object.has("id") && StringUtils.isNotBlank(object.getString("id"))) {//修改
				String id = object.getString("id");
				IrrIndexInfoEntity dbEntity = service.findById(id);
				tempEntity.setCreateDate(dbEntity.getCreateDate());
				tempEntity.setCreator(dbEntity.getCreator());
				IrrSplitIndexEntity examSplitIndex = new IrrSplitIndexEntity();
				examSplitIndex.setSourceIndexId(id);
				List<IrrSplitIndexEntity> splitList = irrSplitIndexService.fetch(examSplitIndex, IrrQueryParameter.getIrrDefaultQP());
				if(CollectionUtils.isNotEmpty(splitList)) {
					for(IrrSplitIndexEntity sp : splitList) {
						sp.setResultType(tempEntity.getIndexResultType());
						irrSplitIndexService.update(sp.getId(), sp);
					}
				}
				res = service.update(id, tempEntity);
			}else {//新增
				List<IrrIndexInfoEntity> list = service.getRepository().findByIndexCodeAndIndexStatus(tempEntity.getIndexCode(), tempEntity.getIndexStatus());
				if(CollectionUtils.isNotEmpty(list)) {
					LOG.error("指标已经存在，无法保存");
					map.put("flag", false);
					return map;
				}
				res = service.add(tempEntity);
			}
			map.put("data", res);
			map.put("flag", true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			map.put("data", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 查询操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 响应封装对象(查询结果)
	 * @throws Exception 违例
	 */
	@Override
	protected ResponseWrapper<IrrIndexInfoEntity> iscFetch(HttpServletRequest request,HttpServletResponse response,IrrIndexInfoEntity queryExampleEntity,QueryParameter queryParameter) throws Exception{
		if(queryParameter.getPage()>0){
			queryParameter.setPage(queryParameter.getPage()-1);
		}
		Page<IrrIndexInfoEntity> page =service.query(queryExampleEntity, queryParameter);
		List<IrrIndexInfoEntity> content = page.getContent();
		for(int i=0;i < content.size();i ++) {
			IrrIndexInfoEntity index = content.get(i);
			index.setSplitNum(irrSplitIndexService.getRepository().countBySourceIndexId(index.getId()).doubleValue());
		}
		accessLogService.success(request.getRequestURL().toString(), AccessType.QUERY, queryExampleEntity, queryParameter);
		ResponseWrapper<IrrIndexInfoEntity> rw=ResponseWrapperBuilder.query(page);
		if(null!=rw){
			Response<IrrIndexInfoEntity> reponse = rw.getResponse();
			if(reponse.getNumber() == null) {
				reponse.setNumber(1L);
			}else {
				reponse.setNumber(reponse.getNumber() + 1);
			}
			if(reponse.getTotalElements() == null) {
				reponse.setTotalElements(0L);
			}
			if(reponse.getTotalPages() == null) {
				reponse.setTotalPages(0L);
			}
		}
		return rw; 
	}
	
	/**
	 * 根据树的根ID查询机构树
	 * @param id 树的唯一ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOrg.action")
	public List<JsTreeNodeDTO> findOrg(String id){
		List<JsTreeNodeDTO> list = new ArrayList<JsTreeNodeDTO>();
		try {
			if(StringUtils.isBlank(id)) {
				return list;
			}
			List<Map<String, Object>> orgs = service.findOrgByParentId(IrrEnum.JSTREE_ROOT.getValue().equals(id) ? null : id);
			if(orgs == null || orgs.size() <= 0) {
				return list;
			}
			for(Map<String, Object> org : orgs) {
				JsTreeNodeDTO dto = new JsTreeNodeDTO();
				dto.setId(org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
				dto.setText(org.get("FD_NAME") == null ? null : org.get("FD_NAME").toString());
				dto.setParent(org.get("FD_PARENT_ID") == null ? IrrEnum.JSTREE_ROOT.getValue() : org.get("FD_PARENT_ID").toString());
				JSONObject json = new JSONObject();
				json.put("orgCode", org.get("FD_CODE") == null ? null : org.get("FD_CODE").toString());
				json.put("orgId", org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
				json.put("orgName", org.get("FD_NAME") == null ? null : org.get("FD_NAME").toString());
				dto.setLi_attr(json);
				List<Map<String,Object>> children = service.findOrgByParentId(org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
				if(children != null && children.size() > 0) {
					dto.setChildren(true);
				}else {
					dto.setChildren(false);
				}
				list.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
		return list;
	}
	
	/**
	 * 获取Excle模板
	 * @param
	 * @return
	 */
    @RequestMapping(value = "/downloadTemplate.action")
    @ResponseBody
    public void  getUserTemplateTC (HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
        // 读到流中
        InputStream inStream;
        String fileName = "";
		try {
			String templateId = request.getParameter("templateId");
			if (templateId == "OR11" || templateId.equals("OR11")) {
				fileName = "公司治理结果模板";
			} else if (templateId == "OR16" || templateId.equals("OR16")) {
				fileName = "信息系统结果模板";
			} else if (templateId == "OR17" || templateId.equals("OR17")) {
				fileName = "案件管理结果模板";
			} else if (templateId == "LR01" || templateId.equals("LR01")) {
				fileName = "流动性风险结果模板";
			} else if (templateId == "SR01" || templateId.equals("SR01")) {
				fileName = "战略风险结果模板";
			}
			// 获取classPath路径，第一个必须是"/"
			String downloadPath = "/"+IrrTemplateEnum.DOWNLOAD_TEMPLATE_URL.getValue()+File.separator+templateId +".xls";
			inStream = IrrIndexInfoController.class.getResourceAsStream(downloadPath);//文件的存放路径
			
			// 设置输出的格式
	        response.reset();
	        response.setContentType("bin");
	        response.addHeader("Content-Disposition",
	                "attachment;filename=" + URLEncoder.encode(fileName +".xls", "UTF-8"));
	        // 循环取出流中的数据
	        byte[] b = new byte[200];
	        int len;
 
	        while ((len = inStream.read(b)) > 0){
	            response.getOutputStream().write(b, 0, len);
	        }
	        inStream.close();
 
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}
    	
    /**
	 * 上传文件
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/importFile.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importFileInfo(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mes", "导入失败");
		map.put("flag", false);
		
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;

		try {
			String templateId = request.getParameter("templateId");
			String projTypeId = request.getParameter("projTypeId");
			String importYear = request.getParameter("importYear");
			String importQuar = request.getParameter("importQuar");
			String taskBatch = IrrUtil.getFiveClassTaskBatch(importYear, importQuar);
			// 转换成输入流
			input = file.getInputStream();
			// 创建文档
			wb = new HSSFWorkbook(input);
			//根据模板ID导入文件数据
			map = irrIndexInfoService.importFileInfoBytempId(wb, templateId, projTypeId,taskBatch);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("mes", e.getMessage());
		}
		return map;
	}
    	
}


