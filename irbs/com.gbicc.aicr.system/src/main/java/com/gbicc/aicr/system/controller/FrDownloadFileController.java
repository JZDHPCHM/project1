package com.gbicc.aicr.system.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;

import com.gbicc.aicr.system.entity.FrDownloadFileEntity;
import com.gbicc.aicr.system.repository.FrDownloadFileRepository;
import com.gbicc.aicr.system.service.FrDownloadFileService;
import com.gbicc.aicr.system.support.enums.DownloadFileEnum;
import com.gbicc.aicr.system.util.AppUtil;

/**
 * 文件下载中心操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月31日
 */
@Controller
@RequestMapping("/downloadFile")
public class FrDownloadFileController extends BootstrapRestfulCrudController<FrDownloadFileEntity, String, FrDownloadFileRepository, FrDownloadFileService>{

    private static final Logger LOGGER = LogManager.getLogger(FrDownloadFileController.class);
    
    /**
     * 下载中心首页
     *
     * @return
     */
    @RequestMapping("/index.action")
    @MenuContributionItem("(#{menuitem.system.downloadcenter})")
    public String downloadFileIndex() {
        return "gbicc/aicr/view/download.html";
    }

    /**
     * 获取下载列表
     *
     * @param fileName
     * @param fileStatus
     * @param fileType
     * @return
     */
    @RequestMapping(value="/getDownloadFile.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getDownloadFile(String fileName,String fileType,String exportStatus,
            @RequestParam(name = "page", required = false) Integer pageIndex,
            @RequestParam(name = "rows", required = false) Integer pageSize){
        
        try {
            Map<String, Object> map = new HashMap<>();
            map = service.getDownloadFile(fileName,fileType,exportStatus,pageIndex,pageSize);
            map.put("page", pageIndex);
            return map;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return null;
        }
    }
    /**
     * 下载文件
     */
    @RequestMapping(value="/download.action",method={RequestMethod.POST,RequestMethod.GET})
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        
        
        OutputStream os = null;
        InputStream in = null;
        try {
            FrDownloadFileEntity entity = service.findById(id);
            if(entity==null) {
                os = response.getOutputStream();
                os.write("未找到该记录".getBytes());
            }
            
            String fileName = new String(entity.getFileName().getBytes("UTF-8"), "ISO8859-1");
            in = new FileInputStream(new File(entity.getFilePath()));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            if(os==null) {
                os = response.getOutputStream();
            }
            byte[] b = new byte[100];
            int len;
            while ((len = in.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
            entity.setDownloadMsg(DownloadFileEnum.SUCCESS.getValue());
            service.update(id, entity);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(),e);
            }
        }
    }
    
    /**
     * 下载文件
     */
    @RequestMapping(value="/delete.action",method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> delete(String paramIds, HttpServletRequest request, HttpServletResponse response) {
        
        if(StringUtils.isBlank(paramIds)) {
            return AppUtil.getMap(true);
        }
        
        try {
            String[] ids = paramIds.split(",");
            for(String id:ids) {
                FrDownloadFileEntity entity = service.findById(id);
                //删除文件
                File file = new File(entity.getFilePath());
                if(file.exists()&&file.isFile()) {
                    file.delete();
                }
                //删除记录
                service.remove(id);
            }
            return AppUtil.getMap(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }
    
}
