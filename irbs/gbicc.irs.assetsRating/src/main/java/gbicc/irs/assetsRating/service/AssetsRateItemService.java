package gbicc.irs.assetsRating.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.assetsRating.entity.AssetsRateItems;
import gbicc.irs.assetsRating.repository.AssetsRateItemsRepository;

public interface AssetsRateItemService extends DaoService<AssetsRateItems, String, AssetsRateItemsRepository> {
	
}
