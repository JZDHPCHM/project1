package gbicc.irs.main.rating.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import gbicc.irs.main.rating.service.impl.BpMasterServiceImpl;
import gbicc.irs.main.rating.support.MD5Util;

@Controller
@RequestMapping("/bpMaster")
public class BpMasterController {

	@Autowired
	BpMasterServiceImpl bpMaster;
	

	@Autowired
	private SystemDictionaryService systemDictionaryService;
	
	
	
	@RequestMapping(value = "fuzzySearch", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> fuzzySearch() {
		return bpMaster.fuzzySearch("LESSEE_NAME");
	}

	@RequestMapping(value = "fuzzyAssoName", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> fuzzyAssoName() {
		return bpMaster.fuzzySearch("ASSO_NAME");
	}

	@RequestMapping(value = "SearchBpMasterCode", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> SearchBpMasterCode() {
		return bpMaster.fuzzySearchBpMaster("FD_BP_CODE");
	}

	@RequestMapping(value = "userSearch", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> userSearch() {
		return bpMaster.userSearch();
	}

	@RequestMapping(value = "dicIndustry", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> dicIndustry() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = bpMaster.dicIndustry("01");
		for (String key : map.keySet()) {
			Map<String, String> resu = new HashMap<String, String>();
			resu.put("name", map.get(key));
			result.add(resu);
		}
		return result;
	}

	@RequestMapping(value = "SearchBpMasterName", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> SearchBpMasterName() {
		return bpMaster.fuzzySearchBpMaster("FD_BP_NAME");
	}

	@RequestMapping(value = "projectSearch", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> projectSearch() {
		return bpMaster.projectSearch();
	}

	// select * from ns_prj_project

	@RequestMapping(value = "fuzzySearchRule", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> fuzzySearchRule() {
		return bpMaster.fuzzySearchRule("RULE_NAME");
	}

	@RequestMapping(value = "fuzzySearchRuleCode", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> fuzzySearchRuleCode() {
		return bpMaster.fuzzySearchRule("RULE_CODE");
	}

	@RequestMapping(value = "fuzzySearchSmallClass", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> fuzzySearchSmallClass() throws Exception {

		Map<String, String> R001 = systemDictionaryService.getDictionaryMap("R001", new Locale("zh", "CN"));
		Map<String, String> R002 = systemDictionaryService.getDictionaryMap("R002", new Locale("zh", "CN"));
		Map<String, String> R003 = systemDictionaryService.getDictionaryMap("R003", new Locale("zh", "CN"));

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (String key : R001.keySet()) {
			Map<String, String> res = new HashMap<String, String>();
			res.put("name", R001.get(key).toString());
			result.add(res);
		}

		for (String key : R002.keySet()) {
			Map<String, String> res = new HashMap<String, String>();
			res.put("name", R002.get(key).toString());
			result.add(res);
		}

		for (String key : R003.keySet()) {
			Map<String, String> res = new HashMap<String, String>();
			res.put("name", R003.get(key).toString());
			result.add(res);
		}

		return result;
	}

	@RequestMapping(value = "parsingData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> parsingData() {
		// bpMaster.parsingData(null)
		return null;
	}

	@Value("${security.user.password}")
	String password;

	@RequestMapping(value = "encryption", method = RequestMethod.POST)
	@ResponseBody
	public String encryption(HttpServletRequest request) throws Exception {
		return MD5Util.encrypt(password);
	}
}
