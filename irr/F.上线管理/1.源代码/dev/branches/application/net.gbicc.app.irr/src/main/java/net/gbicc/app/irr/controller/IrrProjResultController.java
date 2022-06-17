package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;
import net.gbicc.app.irr.service.IrrProjResultService;

/**
* 评估项目得分
*/
@Controller
@RequestMapping("/irr/projResult")
public class IrrProjResultController extends BootstrapRestfulCrudController<IrrProjResultEntity, String, IrrProjResultRepository, IrrProjResultService> {

}
