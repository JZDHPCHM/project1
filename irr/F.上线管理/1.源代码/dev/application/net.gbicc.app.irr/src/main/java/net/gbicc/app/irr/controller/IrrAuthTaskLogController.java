package net.gbicc.app.irr.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthTaskLogRepository;
import net.gbicc.app.irr.service.IrrAuthTaskLogService;

/**
 * irr授权任务日志
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Controller
@RequestMapping("/irr/authTaskLog")
public class IrrAuthTaskLogController extends
        BootstrapRestfulCrudController<IrrAuthTaskLogEntity, String, IrrAuthTaskLogRepository, IrrAuthTaskLogService> {

    private static final Logger LOG = LogManager.getLogger(IrrAuthTaskLogController.class);

    /**
     * 查看授权日志
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAuthTaskLog.action")
    public ResponseWrapper<IrrAuthTaskLogEntity> findAuthTaskLog(String info, Long page, Integer size) {
        try {
            return service.findAuthTaskLog(info, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return ResponseWrapperBuilder.empty();
        }
    }

}
