package gbicc.irs.commom.module.service;

import java.io.File;
import java.io.InputStream;

import gbicc.irs.commom.module.jpa.support.FileModuleType;

/**
 * 汉口银行文件上传下载服务
 * @author koupengyang
 *
 */
public interface HKBFileService {

	/**
	 * 上传保存文件
	 * @param moduleType 文件所属模块
	 * @param fileName 文件名
	 * @param inputStream 输入文件流
	 * @return 文件地址
	 * @throws Exception
	 */
	String uploadFile(FileModuleType moduleType,String allFileName,InputStream inputStream) throws Exception;
	
	/**
	 * 下载文件 
	 * @param fileName
	 * @return	文件输出流
	 * @throws Exception
	 */
	File downloadFile(String fileName) throws Exception;
	
}
