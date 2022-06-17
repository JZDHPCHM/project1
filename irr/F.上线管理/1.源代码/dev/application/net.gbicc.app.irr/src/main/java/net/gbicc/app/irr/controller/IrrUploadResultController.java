package net.gbicc.app.irr.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.mvc.protocol.response.Response;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrUploadResultRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrUploadResultService;

/**
* 上传结果
*
*/
@Controller
@RequestMapping("/irr/uploadResult")
public class IrrUploadResultController extends BootstrapRestfulCrudController<IrrUploadResultEntity, String, IrrUploadResultRepository, IrrUploadResultService> {

	private static final Logger LOG = LogManager.getLogger(IrrUploadResultController.class);
	
	/**
	 * 查询操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 响应封装对象(查询结果)
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<IrrUploadResultEntity> iscFetch(HttpServletRequest request,HttpServletResponse response,IrrUploadResultEntity queryExampleEntity,QueryParameter queryParameter) throws Exception{
		if(queryParameter.getPage()>0){
			queryParameter.setPage(queryParameter.getPage()-1);
		}
		Page<IrrUploadResultEntity> page =service.query(queryExampleEntity, queryParameter);
		List<IrrUploadResultEntity> content = page.getContent();
		if(CollectionUtils.isNotEmpty(content)) {
			Map<String, String> indexDescMap = service.findIndexDesc(null);
			for(IrrUploadResultEntity entity : content) {
				entity.setIndexDesc(indexDescMap.get(entity.getSplitId()));
			}
		}
		accessLogService.success(request.getRequestURL().toString(), AccessType.QUERY, queryExampleEntity, queryParameter);
		ResponseWrapper<IrrUploadResultEntity> rw=ResponseWrapperBuilder.query(page);
		if(null!=rw){
			Response<IrrUploadResultEntity> reponse = rw.getResponse();
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
	 * 下载IRR采集模板
	 * @param response
	 */
	@RequestMapping("/downloadIrrCollTemplate.action")
    public void downloadIrrCollTemplate(HttpServletResponse response, String processTaskId) {
		OutputStream os = null;
		try {
            Workbook wb = service.createIrrCollTemplate(processTaskId);
			String fileName = IrrTemplateEnum.IRR_TEMPLATE_FILE_NAME.getValue()+IrrTemplateEnum.IRR_TEMPLATE_FILE_SUFFIX.getValue();
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			wb.write(os);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			try {
				response.getWriter().print("{flag:false}");
			} catch (IOException e1) {
				e1.printStackTrace();
				LOG.error(e1.getMessage(),e1);
			}
		}finally {
			try {
				if(os != null) {
					os.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(),e);
			}
		}
	}
	
	/**
     * 上传数据
     * 
     * @param file
     *            上传文件
     * @param taskId
     *            评价计划ID
     * @param processTaskId
     *            流程任务ID
     * @return
     */
	@ResponseBody
	@RequestMapping("/uploadData.action")
    public Map<String, Object> uploadData(MultipartFile file, String taskId, String processTaskId) {
		Map<String, Object> map = new HashMap<String,Object>();
		InputStream is = null;
		try {
			if(file == null) {
				map.put("flag", false);
				LOG.error("上传的文件为空！");
				return map;
			}
			is = file.getInputStream();
            service.readCollUploadFile(WorkbookFactory.create(is), taskId, processTaskId);
            service.getBranchIndexValue(taskId);
			map.put("flag", true);
			map.put("mes", "上传成功！");
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			map.put("mes", e.getMessage());
			return map;
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(),e);
					map.put("flag", false);
					map.put("mes", e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 编辑上传结果
	 * @param id
	 * @param splitResultQ2 当期指标结果
	 * @param dataDesc 数据说明
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editUploadResult.action")
	public Map<String, Object> editUploadResult(String id,String splitResultQ2,String dataDesc){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			IrrUploadResultEntity upload = service.editUploadResult(id, splitResultQ2, dataDesc);
			map.put("data", upload);
			map.put("flag",true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag",false);
		}
		return map;
	}
	
	/**
	 * 采集人提交任务
	 * @param taskId 流程任务ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/collSubmit.action")
    public Map<String, Object> collTaskSubmit(String taskId, String id) {
		try {
            return service.collSubmit(taskId, id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 加载系统数据
	 * @param taskId 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadSysData.action")
    public Map<String, Object> loadSysIndexData(String taskId, String processTaskId) {
		try {
            return service.loadSysIndexDataByTaskId(taskId, processTaskId);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
     * 查询采集数据
     * 
     * @param uploadResult
     *            上传数据结果
     * @param sourceProjId
     *            评估项目ID
     * @param size
     *            一页多少记录
     * @param page
     *            第几页
     * @param processTaskId
     *            流程任务ID
     * @param processNode
     *            流程节点
     * @return
     */
	@ResponseBody
	@RequestMapping("/findUploadResult.action")
    public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult, String sourceProjId,
            String sourceOrgId, Integer size, Integer page, String processTaskId, String processNode) {
		try {
            return service.findUploadResult(uploadResult, sourceProjId, sourceOrgId, size, page,
                    processTaskId, processNode);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 检查采集人是否已经上传数据
	 * @param id 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkIsData.action")
    public Map<String, Object> checkIsData(String id, String processTaskId) {
		try {
            return service.checkIsData(id, processTaskId);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, "无法提交:还没有上传模板数据！");
		}
	}
	/**
	 * 查询当前人上传指标
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/searchIndexByPresentLogin.action")
	public Map<String, Object> searchIndexByPresentLogin(HttpServletRequest request,HttpServletResponse response,Integer size,Integer page) {
		String splitCode=request.getParameter("splitCode");
		String splitName=request.getParameter("splitName");
		String collOrgName=request.getParameter("collOrgName");
		String collUserName=request.getParameter("collUserName");
		String isCommit=request.getParameter("isCommit");
        String id = request.getParameter("taskId");
        String sourceProjId = request.getParameter("sourceProjId");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
            map = service.searchIndexByPresentLogin(id, splitCode, splitName, collOrgName, collUserName, isCommit,
                    sourceProjId, size, page);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return (Map<String, Object>) map.put("flag", false);
		}
	}

    /**
     * 查询采集指标
     *
     * @param taskId
     *            评估计划ID
     * @param splitCode
     *            拆分指标编码
     * @param splitName
     *            拆分指标名称
     * @param collectionWay
     *            采集方式
     * @param sourceProjId
     *            评估项目ID
     * @param sourceOrgId
     *            采集机构ID
     * @param collName
     *            采集人姓名
     * @param size
     *            每页多少
     * @param page
     *            当前页
     * @return
     */
    @ResponseBody
    @RequestMapping("/searCollchIndex.action")
    public Map<String, Object> searchCollIndex(String taskId, String splitCode, String splitName, String collectionWay,
            String sourceProjId, String sourceOrgId, String collName, Integer size, Integer page) {
        try {
            return service.searchCollIndex(taskId, splitCode, splitName, collectionWay, sourceProjId, sourceOrgId,
                    collName, size, page);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return IrrUtil.getMap(false, e.getMessage());
        }
    }

    /**
     * 导出采集结果
     *
     * @param param
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/exportCollResult.action")
    public void exportCollIndex(String param, HttpServletResponse response) {
        OutputStream os = null;
        try {
            Workbook wb = service.exportCollResult(param);
            os = response.getOutputStream();
            String fileName = SecurityUtil.getUserName() + IrrTemplateEnum.IRR_TEMPLATE_FILE_SUFFIX.getValue();
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            try {
                response.getWriter().println(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
