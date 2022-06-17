package gbicc.irs.main.rating.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.main.rating.entity.MainScale;

public interface MainScaleRepository extends DaoRepository<MainScale, String> {

	MainScale findByScaleLevelAndType(String scaleLevel,String type);
	
	List<MainScale> findByType(String type);
}
