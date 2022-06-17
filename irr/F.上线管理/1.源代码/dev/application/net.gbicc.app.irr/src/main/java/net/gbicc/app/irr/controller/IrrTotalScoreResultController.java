package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrTotalScoreResultRepository;
import net.gbicc.app.irr.service.IrrTotalScoreResultService;

/**
* 总分多维度结果
*
*/
@Controller
@RequestMapping("/irr/totalScoreResult")
public class IrrTotalScoreResultController extends BootstrapRestfulCrudController<IrrTotalScoreResultEntity, String, IrrTotalScoreResultRepository, IrrTotalScoreResultService> {

}
