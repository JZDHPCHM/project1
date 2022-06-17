package net.gbicc.app.pfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;
import net.gbicc.app.pfm.service.PfmChannelResultService;

/**
* 渠道绩效结果
*
*/
@Controller
@RequestMapping("/pfm/channelResult")
public class PfmChannelResultController extends SmartClientRestfulCrudController<PfmChannelResultEntity, String, PfmChannelResultRepository, PfmChannelResultService> {

}
