package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrStrariskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrStrariskResultRepository;
import net.gbicc.app.irr.service.IrrStrariskResultService;

/**
* 战略风险结果
*
*/
@Controller
@RequestMapping("/irr/strariskResult")
public class IrrStrariskResultController extends BootstrapRestfulCrudController<IrrStrariskResultEntity, String, IrrStrariskResultRepository, IrrStrariskResultService> {

}
