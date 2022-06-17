package gbicc.irs.commom.module.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.core.support.FileDownloader;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import gbicc.irs.commom.module.service.DownFile;

@Service
public class DownFileImpl implements DownFile{

	@Autowired
	private SystemDictionaryService systemDictionaryService;
	@Override
	public void downFile(HttpServletRequest req, HttpServletResponse resp, String name) throws Exception {
		Map<String, String> templateType = systemDictionaryService.getDictionaryMap("DownFile",
				new Locale("zh", "CN"));
		String path = templateType.get(name);
		Resource resource = new DefaultResourceLoader().getResource(path);
		if (resource.exists()) {
			FileDownloader.download(req, resp,path.substring(path.indexOf("/")+1), resource);
		}
	}
	public void uploadFile(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
			Locale locale) {
		try {
			String eorrString = this.fileUpload(multipartFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String fileUpload(MultipartFile file) throws Exception {
		String savePath = "";
		if (file != null && !file.isEmpty()) {
			String randomName = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."))
					+ thisDay();
			if (randomName.indexOf("\\") > 0) {
				randomName = randomName.substring(randomName.lastIndexOf("\\") + 1);
			}
			String uploadFileName = file.getOriginalFilename();
			if (StringUtils.isNotBlank(uploadFileName)) {
				savePath = this.uploadFile( uploadFileName,
						file.getInputStream());
			}
		}
		return savePath;
	}
	@Override
	public String uploadFile(String allFileName, InputStream inputStream) throws Exception {
		//解决IE文件 全路径问题
		String temp[] = allFileName.replaceAll("\\\\","/").split("/");      
		if (temp.length > 1) {      
			allFileName = temp[temp.length - 1];      
		}  
		String saveFileLocalPath = null;
		if(org.springframework.util.StringUtils.hasText(allFileName)) {
			String fileSuffix = allFileName.substring(allFileName.lastIndexOf("."), allFileName.length());
			String fileName = allFileName.substring(0, allFileName.lastIndexOf("."));
			String uploadDir = DirectoryManager.getDirectoryByName("dir.work.web.upload");
			String saveDirectory = uploadDir+File.separator;
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
				throw e;
			}finally {
				//关闭输入输出流
				out.close();
				inputStream.close();
			}
		}
		return saveFileLocalPath;
	}
	public static String thisDay() {
		Calendar c = Calendar.getInstance();
		int year = c.get(1);
		int month = c.get(2);
		int date = c.get(5);
		String months = "";
		String dates = "";
		if (month < 10) {
			month = month + 1;
			months = "0" + String.valueOf(month);
		}
		if (date < 10) {
			dates = "0" + String.valueOf(date);
		} else {
			dates = String.valueOf(date);
		}
		String thisDay = String.valueOf(year) + months + dates;
		return thisDay;
	}

	
	

	
}
