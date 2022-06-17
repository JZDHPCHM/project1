package gbicc.irs.code.library.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.code.library.jpa.entity.CodeLibraryPojo;
import gbicc.irs.code.library.jpa.repository.CodeLibraryRepository;

public interface CodeLibraryService extends DaoService<CodeLibraryPojo,String,CodeLibraryRepository>{
	
	/**
	 * 根据代码编号查询
	 * @param codeNo
	 * @return
	 */
	List<CodeLibraryPojo> findByCodeNo(String codeNo) throws Exception;
	
	/**
	 * 根据项目编号查询
	 * @param itemNo
	 * @return
	 */
	String findByItemCode(String itemCode)throws Exception ;


}
