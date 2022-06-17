//package com.gbicc.aicr.system.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
//
//import com.gbicc.aicr.system.entiry.SystemRole;
//import com.gbicc.aicr.system.repository.SystemRoleRepository;
//import com.gbicc.aicr.system.service.SystemRoleService;
//
//@Controller
//@RequestMapping("/role")
//public class SystemRoleController
//		extends SmartClientRestfulCrudController<SystemRole, String, SystemRoleRepository, SystemRoleService> {
//
//	@RequestMapping(value = "/query_left_action", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//
//	public List<Map<String, Object>> queryRoleActionLeft() throws Exception {
//
//		List<Map<String, Object>> result = service.queryRoleActionLeft();
//
//		return result;
//	}
//
//	@RequestMapping(value = "/query_right_action", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//
//	public List<Map<String, Object>> queryRoleActionRight() throws Exception {
//
//		List<Map<String, Object>> result = service.queryRoleActionRight();
//
//		return result;
//	}
//
//	@RequestMapping(value = "/save_action", method = { RequestMethod.POST, RequestMethod.GET })
//	@ResponseBody
//	public boolean saveRole(String leftId, String rightId) throws Exception {
//		return service.saveRole(leftId, rightId);
//	}
//
//	@RequestMapping(value = "/id_query_action", method = { RequestMethod.GET, RequestMethod.POST })
//	@ResponseBody
//
//	public List<SystemRole> queryRoleById(String id) throws Exception {
//	//	id = "7d490cf4-caed-417c-974d-b30535d571ea";
//		List<SystemRole> result = service.queryRoleById(id);
//
//		return result;
//	}
//
//}
////package com.gbicc.aicr.system.controller;
////
////import java.util.List;
////import java.util.Map;
////
////import org.springframework.stereotype.Controller;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestMethod;
////import org.springframework.web.bind.annotation.ResponseBody;
////import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
////
////import com.gbicc.aicr.system.entiry.SystemRole;
////import com.gbicc.aicr.system.repository.SystemRoleRepository;
////import com.gbicc.aicr.system.service.SystemRoleService;
////
////@Controller
////@RequestMapping("/role")
////public class SystemRoleController
////		extends SmartClientRestfulCrudController<SystemRole, String, SystemRoleRepository, SystemRoleService> {
////
////	@RequestMapping(value = "/query_left_action", method = { RequestMethod.GET, RequestMethod.POST })
////	@ResponseBody
////
////	public List<Map<String, Object>> queryRoleActionLeft() throws Exception {
////
////		List<Map<String, Object>> result = service.queryRoleActionLeft();
////
////		return result;
////	}
////
////	@RequestMapping(value = "/query_right_action", method = { RequestMethod.GET, RequestMethod.POST })
////	@ResponseBody
////
////	public List<Map<String, Object>> queryRoleActionRight() throws Exception {
////
////		List<Map<String, Object>> result = service.queryRoleActionRight();
////
////		return result;
////	}
////
////	@RequestMapping(value = "/save_action", method = { RequestMethod.POST, RequestMethod.GET })
////	@ResponseBody
////	public boolean saveRole(String leftId, String rightId) throws Exception {
////		return service.saveRole(leftId, rightId);
////	}
////
////	@RequestMapping(value = "/id_query_action", method = { RequestMethod.GET, RequestMethod.POST })
////	@ResponseBody
////
////	public List<SystemRole> queryRoleById(String id) throws Exception {
////	//	id = "7d490cf4-caed-417c-974d-b30535d571ea";
////		List<SystemRole> result = service.queryRoleById(id);
////
////		return result;
////	}
////
////}
//
//
//
//
//
//
//
