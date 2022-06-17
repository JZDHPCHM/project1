package gbicc.irs.commom.module.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.core.DirectoryManager;

import gbicc.irs.commom.module.jpa.support.FileModuleType;
import gbicc.irs.commom.module.service.HKBFileService;

@Service
public class HKBFileServiceImpl implements HKBFileService{

	private static Log log = LogFactory.getLog(HKBFileServiceImpl.class);
	
	@Override
	public String uploadFile(FileModuleType moduleType, String allFileName, InputStream inputStream) throws Exception {
		String typeName = moduleType.name();
		//解决IE文件 全路径问题
		String temp[] = allFileName.replaceAll("\\\\","/").split("/");      
		if (temp.length > 1) {      
			allFileName = temp[temp.length - 1];      
		}  
		String saveFileLocalPath = null;
		if(StringUtils.hasText(allFileName)) {
			String fileSuffix = allFileName.substring(allFileName.lastIndexOf("."), allFileName.length());
			String fileName = allFileName.substring(0, allFileName.lastIndexOf("."));
			String uploadDir = DirectoryManager.getDirectoryByName("dir.work.web.upload");
			String saveDirectory = uploadDir+File.separator+typeName;
			File folder = new File(saveDirectory);
			if(!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			}
			saveFileLocalPath =saveDirectory + File.separator + fileName+"_"+System.currentTimeMillis()+fileSuffix;
			File file = new File(saveFileLocalPath);
			saveFileLocalPath = file.getPath();
			//获取输出流
			FileOutputStream out = null;
			try {
				//获取输出流
				out = new FileOutputStream(file);// 指定文件
				int n = 0;// 每次读取的字节长度
				byte[] bb = new byte[1024];// 存储每次读取的内容
				while ((n = inputStream.read(bb)) != -1) {
				    out.write(bb, 0, n);// 将读取的内容，写入到输出流当中
				}
			} catch (Exception e) {
				log.error(e);
				throw e;
			}finally {
				//关闭输入输出流
				out.close();
				inputStream.close();
			}
		}
		return saveFileLocalPath;
	}

	@Override
	public File downloadFile(String fileName) throws Exception {
		if(StringUtils.hasText(fileName)) {
			File file = new File(fileName);
			if(file.exists()) {
				return file;
			}
		}
		return null;
	}
}
