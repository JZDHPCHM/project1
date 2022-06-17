package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrCasemanaResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrCasemanaResultRepository;
import net.gbicc.app.irr.service.IrrCasemanaResultService;

/**
* 案件管理结果
*/
@Controller
@RequestMapping("/irr/caseManaResult")
public class IrrCasemanaResultController extends BootstrapRestfulCrudController<IrrCasemanaResultEntity, String, IrrCasemanaResultRepository, IrrCasemanaResultService> {

}
