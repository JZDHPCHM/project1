package gbicc.irs.assetsRating.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.assetsRating.entity.AssetsRateItems;
import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.repository.AssetsRateItemsRepository;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;
import gbicc.irs.assetsRating.service.AssetsRateItemService;

@Service("assetsRateItemServiceImpl")
public class AssetsRateItemServiceImpl extends DaoServiceImpl<AssetsRateItems, String, AssetsRateItemsRepository> implements AssetsRateItemService{
	
	
}
