package net.gbicc.app.pfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;
import net.gbicc.app.pfm.service.PfmBranchResultService;

/**
* 分公司绩效结果
*
*/
@Controller
@RequestMapping("/pfm/branchResult")
public class PfmBranchResultController extends SmartClientRestfulCrudController<PfmBranchResultEntity, String, PfmBranchResultRepository, PfmBranchResultService> {

}
