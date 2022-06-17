package net.gbicc.app.pfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmResultRepository;
import net.gbicc.app.pfm.service.PfmResultService;

/**
 * 绩效结果
 * 
 * @author FC
 * @version v1.0 2019年7月4日
 */
@Controller
@RequestMapping("/pfm/result")
public class PfmResultController
        extends BootstrapRestfulCrudController<PfmResultEntity, String, PfmResultRepository, PfmResultService> {

}
