package gbicc.irs.reportTemplate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.protocol.smartclient.request.IscRequest;
import org.wsp.framework.mvc.protocol.smartclient.request.Operation;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.entity.IndicatorsCalculationFormula;
import gbicc.irs.reportTemplate.jpa.repository.IndicatorsCalculationFormulaRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountService;
import gbicc.irs.reportTemplate.service.IndicatorsCalculationFormulaService;

@Controller
@RequestMapping("/reportTemplate/indicatorsCalculationFormula")
public class IndicatorsCalculationFormulaController extends SmartClientRestfulCrudController<IndicatorsCalculationFormula, String, IndicatorsCalculationFormulaRepository, IndicatorsCalculationFormulaService> {
	
	@Autowired
	private FinancialAccountService financialAccountService;
	
	@Override
	protected ResponseWrapper<IndicatorsCalculationFormula> iscUpdate(HttpServletRequest request,
			HttpServletResponse response, @Valid IscRequest<IndicatorsCalculationFormula> iscRequest,
			BindingResult bindingResult) throws Exception {
		ResponseWrapper<IndicatorsCalculationFormula> result =ResponseWrapperBuilder.validationError(bindingResult);
		if(result!=null){
			return result;
		}
		if (iscRequest.isQueue()){
			List<Operation<IndicatorsCalculationFormula>> operations =iscRequest.getOperations();
			if(operations!=null && operations.size()>0){
				Map<String,IndicatorsCalculationFormula> updates =new HashMap<String,IndicatorsCalculationFormula>();
				for(Operation<IndicatorsCalculationFormula> operation : operations){
					IndicatorsCalculationFormula _data =operation.getData();
					IndicatorsCalculationFormula _oldData =operation.getOldValues();
					if(_data!=null && _oldData!=null){
						String oldId =service.getRepository().getId(_oldData);
						if(oldId!=null){
							updates.put(oldId, _data);
						}
					}
				}
				List<IndicatorsCalculationFormula> objects =service.update(updates);
				accessLogService.success(request.getRequestURL().toString(), AccessType.UPDATE, null,objects);
				return ResponseWrapperBuilder.queue(objects);
			}
		}else{
			IndicatorsCalculationFormula data =iscRequest.getData();
			if(data!=null){
				IndicatorsCalculationFormula oldData =iscRequest.getOldValues();
				if(oldData==null) {//如果没有 old value,表示只传入了新值,则采用新值更新新值ID对应的 old value
					oldData =data;
				}
				//获取财报模板下科目
				List<FinancialAccount> financialAccounts = financialAccountService.queryAccoutByStatementTemplate(data.getReportTemplate());
				String displayCalculationFormula = data.getDisplayCalculationFormula();
				//获取执行表达式
				String calculationFormula = service.getExecuteFormula(displayCalculationFormula, financialAccounts);
				//验证计算公式正确性
				service.vaildateFormula(calculationFormula, service.initializeVaildateParameters(financialAccounts));
				data.setCalculationFormula(calculationFormula);
				
				IndicatorsCalculationFormula updated =service.update(service.getRepository().getId(oldData), data);
				updated.setDisplayCalculationFormula(displayCalculationFormula);
				result =ResponseWrapperBuilder.crud(updated);
				//result.getResponse().setInvalidateCache(true);
				accessLogService.success(request.getRequestURL().toString(), AccessType.UPDATE, oldData,updated);
				return result;
			}
		}
		return ResponseWrapperBuilder.empty();
	}
	
	
}
