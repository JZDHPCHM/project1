package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrCorpgoveResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrCorpgoveResultRepository;
import net.gbicc.app.irr.service.IrrCorpgoveResultService;

/**
* 公司治理结果
*/
@Controller
@RequestMapping("/irr/corpgoveResult")
public class IrrCorpgoveResultController extends BootstrapRestfulCrudController<IrrCorpgoveResultEntity, String, IrrCorpgoveResultRepository, IrrCorpgoveResultService> {

}
