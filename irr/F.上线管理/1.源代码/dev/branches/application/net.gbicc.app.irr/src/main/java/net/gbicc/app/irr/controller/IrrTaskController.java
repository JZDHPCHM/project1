package net.gbicc.app.irr.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrTaskRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrTaskService;

/**
* 任务
*
*/
@Controller
@RequestMapping("/irr/task")
public class IrrTaskController extends BootstrapRestfulCrudController<IrrTaskEntity, String, IrrTaskRepository, IrrTaskService> {

	private static final Logger LOG = LogManager.getLogger(IrrTaskController.class);
	
	/**
	 * 创建任务
	 * @param taskName 任务名称
	 * @param taskBatch 任务期次
	 * @param deadPlanDate 计划截止时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/startTask.action")
	public Map<String, Object> startTask(String taskName,String taskBatch,String deadPlanDate){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map = service.startTask(taskName, taskBatch, deadPlanDate);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			map.put("data", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据评估计划ID删除评估计划
	 * @param taskId 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deletePlan.action")
	public Map<String, Object> deleteTask(String taskId){
		try {
			return service.deleteTask(taskId);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 审核人退回
	 * @param taskId 流程任务ID
	 * @param collUser 采集人账号
	 * @param comment 意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/examBack.action")
	public Map<String, Object> examBack(String taskId,String collUser,String comment){
		try {
			return service.examBack(taskId,collUser,comment);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 审核人提交
	 * @param taskId 	流程任务ID
	 * @param collUser  采集人账号
	 * @param comment   意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/examSubmit.action")
	public Map<String, Object> examSubmit(String taskId,String collUser,String comment){
		try {
			return service.examSubmit(taskId,collUser,comment);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 汇总节点退回
	 * @param taskId 	流程任务ID
	 * @param loginNames  	采集人账号集合
	 * @param comment   意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/summBack.action")
	public Map<String, Object> summBack(String taskId,String loginNames,String comment){
		try {
			return service.summBack(taskId,loginNames,comment);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 指标汇总
	 * @param id 评估项目ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/indexSumm.action")
	public Map<String, Object> indexSumm(String id){
		try {
			return service.indexSumm(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false,e.getMessage());
		}
	}
	
	/**
	 * 评估项目汇总
	 * @param id 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/projSumm.action")
	public Map<String,Object> projSumm(String id){
		try {
			return service.projSumm(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 汇总全部
	 * @param id 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/summAll.action")
	public Map<String,Object> summAll(String id){
		try {
			return service.summAll(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
	
	/**
	 * 根据评估计划ID下载审批记录
	 * @param id 评估计划ID
	 */
	@ResponseBody
	@RequestMapping("/downloadProcessComment.action")
	public void downloadProcessComment(String id,HttpServletResponse response) {
		OutputStream os = null;
		try {
			IrrTaskEntity irrTask = service.findById(id);
			Workbook wb = service.createProcessCommentById(id);
			String fileName = irrTask.getTaskName()+IrrTemplateEnum.IRR_TEMPLATE_FILE_SUFFIX.getValue();
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
	 * 查询汇总节点退回信息
	 * @param id 评估计划ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findSummBackInfo.action")
	public Map<String, Object> findSummBackInfo(String id){
		try {
			return service.findSummBackInfo(id);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false);
		}
	}
	
	/**
	 * 根据评估计划ID查询审批意见
	 * @param id 评估计划ID
	 * @param size 每页大小
	 * @param page 当前页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findProcessComment.action")
	public Map<String, Object> findProcessComment(String id,Integer size,Long page){
		try {
			return service.findProcessComment(id,size,page);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false);
		}
	}
	
}
