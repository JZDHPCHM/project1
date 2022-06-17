package net.gbicc.app.irr.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	 * 下载IRR采集模板
	 * @param response
	 */
	@RequestMapping("/downloadIrrCollTemplate.action")
	public void downloadIrrCollTemplate(HttpServletResponse response) {
		OutputStream os = null;
		try {
			Workbook wb = service.createIrrCollTemplate();
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
	 * @param file上传文件
	 * @param taskId 评价计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadData.action")
	public Map<String, Object>  uploadData(MultipartFile file,String taskId){
		Map<String, Object> map = new HashMap<String,Object>();
		InputStream is = null;
		try {
			if(file == null) {
				map.put("flag", false);
				LOG.error("上传的文件为空！");
				return map;
			}
			is = file.getInputStream();
			service.readCollUploadFile(WorkbookFactory.create(is),taskId);
			map.put("flag", true);
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			return map;
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(),e);
					map.put("flag", false);
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
	public Map<String, Object> collTaskSubmit(String taskId){
		try {
			return service.collSubmit(taskId);
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
	public Map<String, Object> loadSysIndexData(String taskId){
		try {
			return service.loadSysIndexDataByTaskId(taskId);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 查询采集数据
	 * @param uploadResult 上传数据结果
	 * @param sourceProjId 评估项目ID
	 * @param size 一页多少记录
	 * @param page 第几页
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findUploadResult.action")
	public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult,String sourceProjId,String examLoginName,Integer size,Integer page){
		try {
			return service.findUploadResult(uploadResult,sourceProjId,examLoginName,size,page);
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
	public Map<String, Object> checkIsData(String id){
		try {
			return service.checkIsData(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, "无法提交:还没有上传模板数据！");
		}
	}
	
}
