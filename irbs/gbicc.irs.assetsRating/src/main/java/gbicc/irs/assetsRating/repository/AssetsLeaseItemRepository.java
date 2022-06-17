package gbicc.irs.assetsRating.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.assetsRating.entity.AssetsLeaseItem;
import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.support.RatingStepType;

public interface AssetsLeaseItemRepository extends DaoRepository<AssetsLeaseItem, String> {



}
