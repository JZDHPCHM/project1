package gbicc.irs.risk.exposure.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.risk.exposure.entity.RiskApplyForLogEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskApplyForLogRepository;
import gbicc.irs.risk.exposure.service.RiskApplyForLogService;

@Controller
@RequestMapping("/irs/risk/ApplyLog")
public class RiskApplyForLogController extends SmartClientRestfulCrudController<RiskApplyForLogEntity, String, RiskApplyForLogRepository, RiskApplyForLogService> {
	
	@RequestMapping(value="findHisEventByRiskExporeId", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RiskApplyForLogEntity> findHisEventByRiskExporeId(@RequestParam(name="riskExporeId") String riskExporeId,Pageable pageable) throws Exception{
		return ResponseWrapperBuilder.query(service.findByRiskExporeId(riskExporeId, pageable));			
	}
}
