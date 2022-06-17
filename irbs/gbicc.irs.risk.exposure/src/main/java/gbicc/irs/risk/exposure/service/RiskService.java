package gbicc.irs.risk.exposure.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wsp.framework.flowable.controller.support.ProcessProperties;
import org.wsp.framework.jpa.service.DaoService;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.risk.exposure.Wrapper.RiskWrapper;
import gbicc.irs.risk.exposure.entity.RiskEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskRepository;


public interface RiskService extends
DaoService<RiskEntity, String,RiskRepository> {

	public Page<RiskWrapper> fetchRisk(RiskWrapper riskWrapper,Pageable pageable) throws Exception;
	
	public Page<RiskWrapper> fetchRiskApprove(RiskWrapper riskWrapper,Pageable pageable) throws Exception;
	//风险申请
	public void applyfor(ProcessProperties properties)throws  Exception;
	
	public void approve(ProcessProperties properties)throws  Exception;
	//撤回申请
	public void removeRiskApplyfor(String id,String taskId) throws Exception;
	
	public void riskPeopleDone(String defaultId,String taskId,String doneType)throws  Exception;
	
	public Page<RiskWrapper> fetchEnableRisk(RiskWrapper riskWrapper,Pageable pageable) throws Exception;

	JSONObject findOrgs();
}
