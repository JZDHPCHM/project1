package gbicc.irs.debtRating.debt.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.repository.NsPrjProjectRepository;
import gbicc.irs.debtRating.debt.service.NsPrjProjectService;
import gbicc.irs.main.rating.utils.Constats;

@Service
public class NsPrjProjectServiceImpl extends DaoServiceImpl<NsPrjProject, String, NsPrjProjectRepository>
		implements  NsPrjProjectService {

	
	/**
	 * 	项目信息数据解析并封装
	 * @param nsPrjProjectMap
	 * @return	NsPrjProject
	 * 
	 */
	public static NsPrjProject setNsPrjProject(Map<String,Object> nsPrjProjectMap) {
		NsPrjProject projectInfo = new NsPrjProject();
		projectInfo.setAssessedValue(nsPrjProjectMap.get("ASSESSED_VALUE")==null?"":nsPrjProjectMap.get("ASSESSED_VALUE").toString().trim());
		projectInfo.setAssessmentMethods(nsPrjProjectMap.get("ASSESSMENT_METHODS")==null?"":nsPrjProjectMap.get("ASSESSMENT_METHODS").toString().trim());
		projectInfo.setAssistingPmA(nsPrjProjectMap.get("ASSISTING_PM_A")==null?"":nsPrjProjectMap.get("ASSISTING_PM_A").toString().trim());
		projectInfo.setBpIdTenant(nsPrjProjectMap.get("BP_ID_TENANT")==null?"":nsPrjProjectMap.get("BP_ID_TENANT").toString().trim());
		projectInfo.setCoreleaseProportion(nsPrjProjectMap.get("CORELEASE_PROPORTION")==null?"0":nsPrjProjectMap.get("CORELEASE_PROPORTION").toString().trim());
		projectInfo.setDocumentType(nsPrjProjectMap.get("DOCUMENT_TYPE")==null?"":nsPrjProjectMap.get("DOCUMENT_TYPE").toString().trim());
		projectInfo.setEmployeeId(nsPrjProjectMap.get("EMPLOYEE_ID")==null?"":nsPrjProjectMap.get("EMPLOYEE_ID").toString().trim());
		projectInfo.setEnterpriseCcale(nsPrjProjectMap.get("ENTERPRISE_SCALE")==null?"":nsPrjProjectMap.get("ENTERPRISE_SCALE").toString().trim());
		projectInfo.setFinanceAmount(nsPrjProjectMap.get("FINANCE_AMOUNT")==null?"":nsPrjProjectMap.get("FINANCE_AMOUNT").toString().trim());
		projectInfo.setIscorelease(nsPrjProjectMap.get("ISCORELEASE")==null?"":nsPrjProjectMap.get("ISCORELEASE").toString().trim());
		projectInfo.setLeaseChannel(nsPrjProjectMap.get("LEASE_CHANNEL")==null?"":nsPrjProjectMap.get("LEASE_CHANNEL").toString().trim());
		projectInfo.setLeaseItemShortName(nsPrjProjectMap.get("LEASE_ITEM_SHORT_NAME")==null?"":nsPrjProjectMap.get("LEASE_ITEM_SHORT_NAME").toString().trim());
		projectInfo.setLeaseOrganization(nsPrjProjectMap.get("LEASE_ORGANIZATION")==null?"":nsPrjProjectMap.get("LEASE_ORGANIZATION").toString().trim());
		projectInfo.setLeaseStartDate(nsPrjProjectMap.get("LEASE_START_DATE")==null?"":nsPrjProjectMap.get("LEASE_START_DATE").toString().trim());
		projectInfo.setLeaseTerm(nsPrjProjectMap.get("LEASE_TERM")==null?"":nsPrjProjectMap.get("LEASE_TERM").toString().trim());
		projectInfo.setMargin(nsPrjProjectMap.get("MARGIN")==null?"":nsPrjProjectMap.get("MARGIN").toString().trim());
		projectInfo.setNetValue(nsPrjProjectMap.get("NET_VALUE")==null?"":nsPrjProjectMap.get("NET_VALUE").toString().trim());
		projectInfo.setOriginalValue(nsPrjProjectMap.get("ORIGINAL_VALUE")==null?"":nsPrjProjectMap.get("ORIGINAL_VALUE").toString().trim());
		projectInfo.setProductType(nsPrjProjectMap.get("PRODUCT_TYPE")==null?"":nsPrjProjectMap.get("PRODUCT_TYPE").toString().trim());
		projectInfo.setProjectName(nsPrjProjectMap.get("PROJECT_NAME")==null?"":nsPrjProjectMap.get("PROJECT_NAME").toString().trim());
		projectInfo.setProjectNumber(nsPrjProjectMap.get("PROJECT_NUMBER")==null?"":nsPrjProjectMap.get("PROJECT_NUMBER").toString().trim());
		projectInfo.setRiskManagerName(nsPrjProjectMap.get("RISK_MANAGER_NAME")==null?"":nsPrjProjectMap.get("RISK_MANAGER_NAME").toString().trim());
		projectInfo.setCreditType(nsPrjProjectMap.get("CREDIT_TYPE")==null?"":nsPrjProjectMap.get("CREDIT_TYPE").toString().trim());
		return projectInfo;
	}
}
