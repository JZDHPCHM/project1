package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrLiquriskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrLiquriskResultRepository;
import net.gbicc.app.irr.service.IrrLiquriskResultService;

/**
*流动性风险结果
*/
@Controller
@RequestMapping("/irr/liquriskResult")
public class IrrLiquriskResultController extends BootstrapRestfulCrudController<IrrLiquriskResultEntity, String, IrrLiquriskResultRepository, IrrLiquriskResultService> {

}
