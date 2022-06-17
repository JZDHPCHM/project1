package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexResultRepository;
import net.gbicc.app.irr.service.IrrIndexResultService;

/**
* 指标结果
*/
@Controller
@RequestMapping("/irr/indexResult")
public class IrrIndexResultController extends BootstrapRestfulCrudController<IrrIndexResultEntity, String, IrrIndexResultRepository, IrrIndexResultService> {

}
