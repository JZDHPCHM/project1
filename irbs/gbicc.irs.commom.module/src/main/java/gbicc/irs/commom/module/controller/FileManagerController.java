package gbicc.irs.commom.module.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.core.support.FileDownloader;

import gbicc.irs.commom.module.service.HKBFileService;

@Controller
@RequestMapping("/commom/fileManager")
public class FileManagerController {

	@Autowired
	private HKBFileService HKBFileService;
	
	@RequestMapping(value="downloadFile") 
	@ResponseBody
	public void downloadFile(String fileName,String filePath,HttpServletRequest request, HttpServletResponse response) throws Exception{
		File file = HKBFileService.downloadFile(filePath);
		if(file!=null) {
			FileDownloader.download(request, response, fileName, new FileInputStream(file));
		}
	}
	
	@RequestMapping(value="downloadChromeBorwser_64") 
	@ResponseBody
	public void downloadChromeBorwser_64(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fileName = "75.0.3770.90_chrome_installer_64bit.exe";
		String filePath =DirectoryManager.getDirectoryByName("dir.work.web.anonymous-download")+File.separator+fileName;
		File file = HKBFileService.downloadFile(filePath);
		if(file!=null) {
			FileDownloader.download(request, response, fileName, new FileInputStream(file));
		}
	}
	
	@RequestMapping(value="downloadChromeBorwser_32") 
	@ResponseBody
	public void downloadChromeBorwser_32(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fileName = "75.0.3770.90_chrome_installer_32bit.exe";
		String filePath =DirectoryManager.getDirectoryByName("dir.work.web.anonymous-download")+File.separator+fileName;
		File file = HKBFileService.downloadFile(filePath);
		if(file!=null) {
			FileDownloader.download(request, response, fileName, new FileInputStream(file));
		}
	}
}
