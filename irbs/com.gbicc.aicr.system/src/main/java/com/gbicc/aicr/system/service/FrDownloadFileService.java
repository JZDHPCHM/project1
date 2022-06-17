package com.gbicc.aicr.system.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import com.gbicc.aicr.system.entity.FrDownloadFileEntity;
import com.gbicc.aicr.system.repository.FrDownloadFileRepository;

/**
 * 文件下载
 * 
 * @author songxubei
 * @version v1.0 2019年10月30日
 */
public interface FrDownloadFileService extends DaoService<FrDownloadFileEntity, String, FrDownloadFileRepository>{

    /**
     * 执行查询sql，并将数据导出成excel文件
     *
     * @param sql
     * @param fileName
     * @return
     */
   // public Map<String, Object> exportDataToExcel(String sql,String fileName);
	public Map<String, Object> exportDataToExcel(String loginName, String sql, String fileName);
    /**
     * 获取下载列表
     *
     * @param fileName
     * @param fileType
     * @param exportStatus
     * @param pageSize 
     * @param pageIndex 
     * @return
     */
    public Map<String, Object> getDownloadFile(String fileName, String fileType, String exportStatus, Integer pageIndex, Integer pageSize) throws Exception;
    
    /**
     * 获取文件类型
     *
     * @return
     */
    public Map<String, String> getFileType();
    
    /**
     * 获取文件类型
     *
     * @return
     */
    public String getFileTypeMapJsonString();
    
    /**
     * 获取文件状态
     *
     * @return
     */
    public Map<String, String> getExportStatus();
    
    /**
     * 获取文件状态
     *
     * @return
     */
    public String getExportStatusMapJsonString();


}
