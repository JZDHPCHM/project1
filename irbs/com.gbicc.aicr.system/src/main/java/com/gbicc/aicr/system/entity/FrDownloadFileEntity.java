package com.gbicc.aicr.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 下载文件记录表
 * 
 * @author songxubei
 * @version v1.0 2019年11月1日
 */
@Entity
@Table(name="FR_DOWNLOAD_FILE")
public class FrDownloadFileEntity extends AuditorEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @Column(name="ID")
    private String id;
    /**
     * 文件名称
     */
    @Column(name="FILE_NAME")
    private String fileName;
    /**
     * 文件路径
     */
    @Column(name="FILE_PATH")
    private String filePath;
    /**
     * 文件类型
     */
    @Column(name="FILE_TYPE")
    private String fileType;
    /**
     * 导出状态
     */
    @Column(name="EXPORT_STATUS")
    private String exportStatus;
    /**
     * 导出时间
     */
    @Column(name="EXPORT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exportTime;
    /**
     * 导出用户
     */
    @Column(name="EXPORT_USER")
    private String exportUser;
    /**
     * 导出信息
     */
    @Column(name="EXPORT_MSG")
    private String exportMsg;
    /**
     * 下载状态
     */
    @Column(name="DOWNLOAD_STATUS")
    private String downloadStatus;
    /**
     * 下载信息
     */
    @Column(name="DOWNLOAD_MSG")
    private String downloadMsg;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getExportStatus() {
        return exportStatus;
    }
    public void setExportStatus(String exportStatus) {
        this.exportStatus = exportStatus;
    }
    public Date getExportTime() {
        return exportTime;
    }
    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }
    public String getExportUser() {
        return exportUser;
    }
    public void setExportUser(String exportUser) {
        this.exportUser = exportUser;
    }
    public String getExportMsg() {
        return exportMsg;
    }
    
    public void setExportMsg(String exportMsg) {
        this.exportMsg = exportMsg;
    }
    public String getDownloadMsg() {
        return downloadMsg;
    }
    public void setDownloadMsg(String downloadMsg) {
        this.downloadMsg = downloadMsg;
    }
}
