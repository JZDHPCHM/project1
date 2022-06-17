package gbicc.irs.reportTemplate.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.reportTemplate.jpa.entity.FinancialKiLib;

public interface FinancialKiLibRepository extends DaoRepository<FinancialKiLib, String> {

	List<FinancialKiLib> findByKiCodeOrKiName(String kiCode,String kiName);
	
}
