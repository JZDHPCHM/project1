package gbicc.irs.reportTemplate.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.reportTemplate.jpa.entity.FinancialBasicsLib;

public interface FinancialBasicsLibRepository extends DaoRepository<FinancialBasicsLib, String> {

	List<FinancialBasicsLib> findByBasicsCodeOrBasicsName(String basicsCode,String basicsName);
	
	List<FinancialBasicsLib> findByBasicsCodeIn(List<String> basicsCode);
	
}
