package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexOptionRepository;
import net.gbicc.app.irr.service.IrrIndexOptionService;

/**
* 指标选项
*/
@Controller
@RequestMapping("/irr/indexOption")
public class IrrIndexOptionController extends BootstrapRestfulCrudController<IrrIndexOptionEntity, String, IrrIndexOptionRepository, IrrIndexOptionService> {

}
