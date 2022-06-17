package net.gbicc.app.pfm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrChannelResultService;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;
import net.gbicc.app.pfm.jpa.support.enums.PfmEnum;
import net.gbicc.app.pfm.service.PfmBranchResultService;

/**
 * 分公司绩效结果
 *
 */
@Controller
@RequestMapping("/pfm/branchResult")
public class PfmBranchResultController extends
		BootstrapRestfulCrudController<PfmBranchResultEntity, String, PfmBranchResultRepository, PfmBranchResultService> {

	private static final Logger LOG = LogManager.getLogger(PfmBranchResultController.class);
	@Autowired
    private IrrOrgResultService irrOrgResultService;
	@Autowired
    private IrrTaskService irrTaskService;
    @Autowired
    private IrrChannelResultService irrChannelResultService;

	/**
	 * 绩效补录页面分公司绩效查询
	 * 
	 * @param searchProjTypeId
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBranchResult.action")
	public Map<String, Object> queryBranchResult(String taskId, String indexCode, String indexName, Long page,
            Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
            map = service.queryBranchResult(taskId, indexCode, indexName, page, size);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return (Map<String, Object>) map.put("flag", false);
		}
	}

	/**
	 * 下载绩效补录模板
	 * 
	 * @param response
	 */
	@RequestMapping("/downloadPfmDataSupp.action")
	public void downloadDataSuppTemplate(HttpServletResponse response, String taskId) {
		OutputStream os = null;
		try {
            String templateName = PfmEnum.TEMPLATE_PFM_DATA.getValue() + PfmEnum.TEMPLATE_PFM_SUFFIX.getValue();
            IrrTaskEntity task = irrTaskService.findById(taskId);
            //设置基础数据
            Workbook workbook = irrOrgResultService.summaryExportIndexResultAll(task, templateName);
            //设置渠道值
            workbook = irrChannelResultService.exportChannelIndexResult(task, workbook);
            //计算信息系统、计算五大指标
            workbook = service.setOtherData(task, workbook);
            //计算
            Workbook wb = service.createPfmDataSuppWb(workbook);
			response.setContentType("application/vnd.ms-excel");
			String fileName = PfmEnum.TEMPLATE_PFM_DATA_NAME.getValue()
                    + PfmEnum.TEMPLATE_PFM_SUFFIX.getValue();
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 绩效数据补录
	 */
	@ResponseBody
	@RequestMapping("/readPfmDataSupp.action")
	public Map<String, Object> readPfmDataSupp(MultipartFile file, String taskId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream is = null;
		try {
			if (file == null) {
				map.put("flag", false);
				LOG.error("上传的文件为空！");
				return map;
			}
			is = file.getInputStream();
            IrrTaskEntity task = irrTaskService.findById(taskId);
            service.readPfmDataSupp(WorkbookFactory.create(is), taskId, task.getTaskBatch());
			map.put("flag", true);
			map.put("mes", "上传成功！");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("flag", false);
			map.put("mes", "上传失败！");
			return map;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
					map.put("flag", false);
				}
			}
		}

	}

    /**
     * 查询绩效结果
     *
     * @param id
     *            评估计划ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/findPfmResult.action")
    public Map<String, Object> findPfmResult(String id, Long page, Integer size) {
        try {
            return service.findPfmResult(id, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return IrrUtil.getMap(false, e.getMessage());
        }
    }

    /**
     * 导出绩效得分
     *
     * @param id
     *            计划ID
     * @return
     */
    @RequestMapping("/downloadPfmScore.action")
    public void downloadPfmScore(String id, HttpServletResponse response) {
        try {
            service.downloadPfmScore(id, response);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }
    }
}
