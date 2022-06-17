package com.gbicc.aicr.system.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.security.support.SecurityUser;
import org.wsp.framework.security.util.SecurityUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbicc.aicr.system.entity.FrDownloadFileEntity;
import com.gbicc.aicr.system.entity.QFrDownloadFileEntity;
import com.gbicc.aicr.system.repository.FrDownloadFileRepository;
import com.gbicc.aicr.system.service.FrDownloadFileService;
import com.gbicc.aicr.system.support.enums.DownloadFileEnum;
import com.gbicc.aicr.system.util.AppUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * 文件下载操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月30日
 */
@Service
public class FrDownloadFileServiceImpl extends DaoServiceImpl<FrDownloadFileEntity, String, FrDownloadFileRepository> implements FrDownloadFileService{

    private static final Logger LOGGER = LogManager.getLogger(FrDownloadFileServiceImpl.class);
    
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private EntityManager entityManager;
    
    private JPAQueryFactory queryFactory;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @PostConstruct
    public void initChild() {
        queryFactory = new JPAQueryFactory(entityManager);
    }
    @Value("${spring.datasource.items[primary].url}")
    private String jdbcUrl;
    @Value("${spring.datasource.items[primary].username}")
    private String username;
    @Value("${spring.datasource.items[primary].password}")
    private String password;
    
    
    @Override
    @Async
    public Map<String, Object> exportDataToExcel(String loginName,String sql,String fileName){
        try {
            Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(DownloadFileEnum.DOWNLOAD_PATH.getValue(), Locale.CHINA);
            
            String sqlPath = dictionaryMap.get(DownloadFileEnum.SQL_PATH.getValue());
            String filePath = dictionaryMap.get(DownloadFileEnum.FILE_PATH.getValue());
            //判断服务器路径是否存在，不存在的话进行创建
            File sqlPathFile = new File(sqlPath);
            if(!sqlPathFile.exists()) {
                sqlPathFile.mkdirs();
            }
            File filePathFile = new File(filePath);
            if(!filePathFile.exists()) {
                filePathFile.mkdirs();
            }
            //文件名后缀
            String fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
            //随机生成文件名称
            String fileNamePrefix = UUID.randomUUID().toString();
            
            //插入导出记录
            FrDownloadFileEntity entity = new FrDownloadFileEntity();
            entity.setFileName(fileName);
            entity.setFilePath(filePath+fileNamePrefix+fileNameSuffix);
            entity.setFileType(fileName.substring(fileName.lastIndexOf(".")));
            entity.setExportUser(loginName);
            entity.setExportStatus(DownloadFileEnum.EXPORTING.getValue());
            entity.setExportTime(new Date());
            
            entity = add(entity);
            
            //创建脚本
            String spoolSql = sqlPath+fileNamePrefix+".sql";
            File file = new File(spoolSql);
            file.createNewFile();
            //写入脚本内容
            String charset = System.getProperty("sun.jnu.encoding");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            writer.write(DownloadFileEnum.SPOOL_PREFIX.getValue());
            writer.write("spool " + filePath + fileNamePrefix + fileNameSuffix);
            writer.write("\n");
            writer.write(sql+";");
            writer.write("\n");
            writer.write("spool off");
            writer.write("\n");
            writer.write("exit;");
            writer.flush();
            writer.close();
            //获取环境内容，拼接执行命令
          /*  Environment evn = SpringUtilz.getBean(Environment.class);
            String jdbcUrl = evn.getProperty(DownloadFileEnum.JDBC_URL.getValue());
            String username = evn.getProperty(DownloadFileEnum.JDBC_USERNAME.getValue());
            String password = evn.getProperty(DownloadFileEnum.JDBC_PASSWORD.getValue());*/
            String jdbcIp = jdbcUrl.substring(jdbcUrl.indexOf("@"),jdbcUrl.lastIndexOf(":"));
            String sid = jdbcUrl.substring(jdbcUrl.lastIndexOf(":")+1);
            String command = "sqlplus " + username+"/"+password+jdbcIp+"/"+sid + " \"@"+ spoolSql + "\"";
            
            LOGGER.warn("----"+command);
            
            String osName = System.getProperty("os.name");
            String[] cmds = new String[3];
            if(osName.contains("Windows")) {
                cmds[0] = "cmd";
                cmds[1] = "/c";
            }else {
                cmds[0] = "/bin/sh";
                cmds[1] = "-c";
            }
            cmds[2] = command;
            //执行
            Process process = Runtime.getRuntime().exec(cmds);
            String line = "";
            //读取信息
            StringBuffer infoMsg = new StringBuffer();
            BufferedReader info=new BufferedReader(new InputStreamReader(process.getInputStream()));  
            while((line=info.readLine())!=null) {  
                infoMsg.append(line).append("\n");  
            }
             
            //读取错误执行的返回流  
            StringBuffer errorMsg = new StringBuffer();
            BufferedReader error=new BufferedReader(new InputStreamReader(process.getErrorStream()));  
            while((line=error.readLine())!=null) {  
                errorMsg.append(line).append("\n");  
            }
            
            boolean flag = false;
            //轮询判断process是否结束
            while(!flag) {
                try {
                    Thread.sleep(5000);
                    process.exitValue();
                    flag = true;
                } catch (Exception e) {
                }
            }
            
            //获取导出结果状态
            Integer result = process.exitValue();
            process.destroy();
            //成功
            if(result!=null && result==0) {
                entity.setExportStatus(DownloadFileEnum.SUCCESS.getValue());
            }else {
                entity.setExportStatus(DownloadFileEnum.FAIL.getValue());
                entity.setExportMsg(errorMsg.toString());
            }
            update(entity.getId(), entity);
            return AppUtil.getMap(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }

    @Override
    public Map<String, Object> getDownloadFile(String fileName, String fileType, String exportStatus,Integer pageIndex,Integer pageSize) throws Exception {
        
        Map<String, Object> result = new HashMap<>();
        
        QFrDownloadFileEntity qDownloadFile = QFrDownloadFileEntity.frDownloadFileEntity;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        if(!StringUtils.isBlank(fileName)) {
            builder.and(qDownloadFile.fileName.contains(fileName));
        }
        if(!StringUtils.isBlank(exportStatus)) {
            builder.and(qDownloadFile.exportStatus.equalsIgnoreCase(exportStatus));
        }
        if(!StringUtils.isBlank(fileType)) {
            builder.and(qDownloadFile.fileType.equalsIgnoreCase(fileType));
        }
        builder.and(qDownloadFile.exportUser.equalsIgnoreCase(SecurityUtil.getLoginName()));
        if(pageIndex!=null && pageSize!=0) {
            pageIndex --;
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        
        QueryResults<FrDownloadFileEntity> entityList = queryFactory.select(qDownloadFile)
                .from(qDownloadFile).where(builder).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        
        List<FrDownloadFileEntity> resultList = entityList.getResults();
        
        result.put("data", resultList);
        
        result.put("recordsTotal", entityList.getTotal());
        
        result.put("total", (entityList.getTotal()+pageable.getPageSize()-1)/pageable.getPageSize());
        
        return result;
    }

    @Override
    public Map<String, String> getFileType() {
        Map<String, String> map = new HashMap<>();
        map.put(DownloadFileEnum.EXCEL.getValue(), "EXCEL");
        map.put(DownloadFileEnum.WORD.getValue(), "WORD");
        map.put(DownloadFileEnum.PDF.getValue(), "PDF");
        return map;
    }
    
    @Override
    public String getFileTypeMapJsonString() {
        Map<String, String> map = getFileType();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(),e);
            return "null";
        }
    }

    @Override
    public Map<String, String> getExportStatus() {
        Map<String, String> map = new HashMap<>();
        map.put(DownloadFileEnum.SUCCESS.getValue(), "导出成功");
        map.put(DownloadFileEnum.EXPORTING.getValue(), "正在导出");
        map.put(DownloadFileEnum.FAIL.getValue(), "导出失败");
        return map;
    }
    
    @Override
    public String getExportStatusMapJsonString() {
        Map<String, String> map = getExportStatus();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(),e);
            return "null";
        }
    }
}
