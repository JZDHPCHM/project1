package net.gbicc.app.irr.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrOrgResultRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrTaskService;

/**
* 机构结果
*/
@Controller
@RequestMapping("/irr/orgResult")
public class IrrOrgResultController extends BootstrapRestfulCrudController<IrrOrgResultEntity, String, IrrOrgResultRepository, IrrOrgResultService> {

	private static final Logger LOG = LogManager.getLogger(IrrOrgResultController.class);
	@Autowired IrrTaskService irrTaskService;
	
	/**
	 * 汇总页指标tab查询
	 * @param orgResult 查询对象
	 * @param size		每页多少记录
	 * @param page		当前页数
	 * @param projTypeId	评估项目ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOrgResult.action")
	public Map<String, Object> findOrgResult(IrrOrgResultEntity orgResult,Integer size,Integer page,String projTypeId){
		try {
			return service.findOrgResult(orgResult,size,page,projTypeId);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false);
		}
	}
	
	/**
	 * 导出指标结果
	 * @param orgId 机构ID
	 * @param projTypeCode 评估项目编码
	 * @param taskId 评估任务ID
	 */
	@RequestMapping("/exportIndexResult.action")
	public void exportIndexResult(HttpServletResponse response,String orgId,String projTypeCode,String taskId) {
		OutputStream os = null;
		try {
			IrrTaskEntity task = irrTaskService.findById(taskId);
			Workbook wb = service.summaryExportIndexResult(orgId,projTypeCode,task);
			String fileName = task.getTaskBatch() + IrrTemplateEnum.IRR_INDEX_RESULT_FILE_NAME.getValue()+IrrTemplateEnum.IRR_TEMPLATE_FILE_SUFFIX.getValue();
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			wb.write(os);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			try {
				response.getWriter().print("导出失败!");
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
	 * 导出上报结果（excel格式）
	 * @param reponse
	 * @param id 评估计划ID
	 */
	@RequestMapping("/exportExcelReport.action")
	public void exportExcelReport(HttpServletResponse response,String id) {
		OutputStream os = null;
		ZipOutputStream zos = null;
		try {
			IrrTaskEntity task = irrTaskService.findById(id);
			String fileName = task.getTaskBatch() + IrrTemplateEnum.IRR_INDEX_RESULT_FILE_NAME.getValue()+IrrTemplateEnum.IRR_INDEX_RESULT_ZIP_FILE_SUFFIX.getValue();
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			zos = new ZipOutputStream(os);
			Map<String, Workbook> wbMap = service.createReportAllExcel(task);
			for(String str : wbMap.keySet()) {
				zos.putNextEntry(new ZipEntry(str+IrrTemplateEnum.IRR_TEMPLATE_FILE_SUFFIX.getValue()));
				wbMap.get(str).write(zos);
			}
			zos.flush();
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			try {
				response.getWriter().print(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
				LOG.error(e1.getMessage(),e1);
			}
		}finally {
			try {
				if(zos != null) {
					zos.close();
				}
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
	 * 编辑指标结果
	 * @param id 指标结果ID
	 * @param indexResultQ2 当期指标结果
	 * @param dataDesc 数据说明
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editOrgResult.action")
	public Map<String, Object> editOrgResult(String id,String indexResultQ2,String dataDesc){
		try {
			return service.editOrgResult(id,indexResultQ2,dataDesc);
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false,e.getMessage());
		}
	}
	
}
