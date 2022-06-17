package gbicc.irs.code.library.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.code.library.jpa.entity.CodeLibraryPojo;
public interface CodeLibraryRepository extends DaoRepository<CodeLibraryPojo,String>{
	
	List<CodeLibraryPojo> findByCodeNo(String codeNo);
	
	List<CodeLibraryPojo> findByItemCode(String itemCode);
	
}
