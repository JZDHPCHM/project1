package gbicc.irs.code.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.code.library.jpa.entity.CodeLibraryPojo;
import gbicc.irs.code.library.jpa.repository.CodeLibraryRepository;
import gbicc.irs.code.library.service.CodeLibraryService;

@Controller
@RequestMapping("/irs/codeLibrary")
public class CodeLibraryController 
				extends SmartClientRestfulCrudController<CodeLibraryPojo,String,CodeLibraryRepository,CodeLibraryService>{
	
	
	@RequestMapping("queryCodeLibrary.html")
	public String start(){
		return "gbicc/irs/code/library/view/queryCodeLibrary.html";
	}

//	@RequestMapping(value="findByCodeNo", method=RequestMethod.GET)
//	@ResponseBody
//	public CodeLibraryPojo findByCodeNo(@RequestParam(name="codeNo")String codeNo){
//		return service.findByCodeNo(codeNo);
//	}
//	

}
