package gbicc.irs.commom.module.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface DownFile {


	void downFile(HttpServletRequest req, HttpServletResponse resp, String name) throws Exception;

	String uploadFile(String allFileName, InputStream inputStream) throws Exception;

	String fileUpload(MultipartFile file) throws Exception;

}
