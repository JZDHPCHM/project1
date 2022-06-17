package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrInfosysResultRepository;
import net.gbicc.app.irr.service.IrrInfosysResultService;

/**
*	信息系统结果
*/
@Controller
@RequestMapping("/irr/infosysResult")
public class IrrInfosysResultController extends BootstrapRestfulCrudController<IrrInfosysResultEntity, String, IrrInfosysResultRepository, IrrInfosysResultService> {

}
