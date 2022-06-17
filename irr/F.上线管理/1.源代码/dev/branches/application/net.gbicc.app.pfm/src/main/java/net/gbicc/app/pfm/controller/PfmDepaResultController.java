package net.gbicc.app.pfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;
import net.gbicc.app.pfm.service.PfmDepaResultService;

/**
* 部门绩效结果
*
*/
@Controller
@RequestMapping("/pfm/depaResult")
public class PfmDepaResultController extends SmartClientRestfulCrudController<PfmDepaResultEntity, String, PfmDepaResultRepository, PfmDepaResultService> {

}
