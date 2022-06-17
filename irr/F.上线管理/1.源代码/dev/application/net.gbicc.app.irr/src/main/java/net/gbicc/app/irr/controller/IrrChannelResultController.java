package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrChannelResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrChannelResultRepository;
import net.gbicc.app.irr.service.IrrChannelResultService;

/**
* 渠道结果
*/
@Controller
@RequestMapping("/irr/channelResult")
public class IrrChannelResultController extends BootstrapRestfulCrudController<IrrChannelResultEntity, String, IrrChannelResultRepository, IrrChannelResultService> {

}
