package gbicc.irs.debtRating.debt.service;

import org.wsp.framework.jpa.repository.DaoRepository;
import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.repository.NsPrjProjectRepository;
import gbicc.irs.main.rating.entity.MainScale;
import gbicc.irs.main.rating.repository.MainScaleRepository;


public interface NsPrjProjectService extends DaoService<NsPrjProject, String, NsPrjProjectRepository> {

	
}
