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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrDataImplHisEntity;
import net.gbicc.app.irr.jpa.repository.IrrDataImplHisRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrDataImplHisService;

/**
*	irr历史数据导入
*/
@Controller
@RequestMapping("/irr/dataImplHis")
public class IrrDataImplHisController extends
		BootstrapRestfulCrudController<IrrDataImplHisEntity, String, IrrDataImplHisRepository, IrrDataImplHisService> {
	
	private static final Logger LOG = LogManager.getLogger(IrrDataImplHisController.class);
	
	/**
	 * 历史数据导入
	 * @param file 导入zip文件
	 * @param year 年
	 * @param quarter 季度
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadHisData.action")
	public Map<String, Object> uploadHisDataImpl(MultipartFile file,String year,String quarter) {
		try {
			return service.readIrrHisDataImpl(file,year,quarter);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false, e.getMessage());
		}
	}
		
	/**
	 * 下载历史数据
	 * @param response
	 * @param taskBatch 期次
	 */
	@RequestMapping("/downloadHisData.action")
	public void downloadHisData(HttpServletResponse response,String taskBatch){
		OutputStream os = null;
		ZipOutputStream zos = null;
		try {
			String fileName = taskBatch + IrrTemplateEnum.IRR_INDEX_RESULT_FILE_NAME.getValue()+IrrTemplateEnum.IRR_INDEX_RESULT_ZIP_FILE_SUFFIX.getValue();
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			zos = new ZipOutputStream(os);
			Map<String, Workbook> wbMap = service.createReportAllExcel(taskBatch);
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
     * 查询历史数据
     * 
     * @param data
     *            历史数据实体
     * @return
     */
	@ResponseBody
	@RequestMapping("/findHisData.action")
    public Map<String, Object> findHisData(String data, Long page, Integer size) {
        try {
            return service.findHisData(data, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);
            return IrrUtil.getMap(false, e.getMessage());
        }
	}

}
