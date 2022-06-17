package gbicc.irs.reportTemplate.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinAccountNorm;
import gbicc.irs.reportTemplate.jpa.repository.FinAccountNormRepository;

public interface FinAccountNormService extends
DaoService<FinAccountNorm, String, FinAccountNormRepository> {
	List<FinAccountNorm> queryNorm();
	
	Map<Object,Object> resurltSelect();
	
	void initNorm() throws Exception;
}
