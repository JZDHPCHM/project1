package com.gbicc.aicr.system.support.enums;

/**
 * 下载文件相关内容
 * 
 * @author songxubei
 * @version v1.0 2019年11月1日
 */
public enum DownloadFileEnum {
    
    /**
     * spool脚本前缀
     */
    SPOOL_PREFIX("set echo off\r\n" + 
            "set heading on\r\n" + 
            "set long 10000\r\n" + 
            "set linesize 200\r\n" + 
            "set pagesize 50000\r\n" + 
            "set term on verify off feedback pagesize 50000\r\n" + 
            "set markup html on entmap ON spool on preformat off\r\n" + 
            "set newpage none\r\n"),
    
    /**
     * 数据库地址
     */
    JDBC_URL("spring.datasource.items[primary].url"),
    
    /**
     * 数据库用户名
     */
    JDBC_USERNAME("spring.datasource.items[primary].username"),
    /**
     * 数据库密码
     */
    JDBC_PASSWORD("spring.datasource.items[primary].password"),
    /**
     * excel
     */
    EXCEL(".xls"),
    /**
     * word
     */
    WORD(".doc"),
    /**
     * pdf
     */
    PDF(".pdf"),
    /**
     * 成功状态
     */
    SUCCESS("SUCCESS"),
    /**
     * 导出中
     */
    EXPORTING("EXPORTING"),
    /**
     * 编码
     */
    CHARSET("CHARSET"),
    /**
     * 失败状态
     */
    FAIL("FAIL"),
    /**
     * 字典配置
     */
    DOWNLOAD_PATH("DOWNLOAD_PATH"),
    /**
     * sql执行路径
     */
    SQL_PATH("SQL_PATH"),
    /**
     * 文件保存路径
     */
    FILE_PATH("FILE_PATH"),
    /**
     * 命令行1
     */
    CMD1("CMD1"),
    /**
     * 命令行2
     */
    CMD2("CMD2");
    
    private DownloadFileEnum(String value){
        this.value = value;
    }
    
    private String value;

    public String getValue() {
        return value;
    }
}
