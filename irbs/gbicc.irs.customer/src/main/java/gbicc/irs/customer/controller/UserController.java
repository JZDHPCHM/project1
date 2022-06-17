package gbicc.irs.customer.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.customer.service.UserService;

@Controller
@RequestMapping("/irs/user/ctm")
public class UserController  extends SmartClientRestfulCrudController<User,String,UserRepository,UserService> {

	/**
	 * 查询客户信息
	 * @param custNo 客户编号
	 * @throws Exception
	 */
	@RequestMapping(value="queryUserByMap", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> copyCompanyCustomer(@RequestParam(name="orgId")String orgId) {
		return service.findByUser(orgId);
	}
}
