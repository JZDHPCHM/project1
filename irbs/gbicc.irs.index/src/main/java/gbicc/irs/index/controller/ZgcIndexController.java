package gbicc.irs.index.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wsp.framework.security.service.OuthLoginService;

@Controller
public class ZgcIndexController {

	@Autowired OuthLoginService outhLoginService;
	
    @RequestMapping(value = "/enterIndex.action", method = RequestMethod.GET)
    public String enterNewIndex(HttpServletRequest request, HttpServletResponse response) {
        return "gbicc/irs/index/view/index.html";
    }
}
