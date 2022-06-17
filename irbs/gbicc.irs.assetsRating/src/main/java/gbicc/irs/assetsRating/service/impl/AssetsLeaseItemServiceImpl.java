package gbicc.irs.assetsRating.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.jdbc.util.SqlBatcher;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.assetsRating.entity.AssetsLeaseItem;
import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.assetsRating.repository.AssetsLeaseItemRepository;
import gbicc.irs.assetsRating.service.AssetsLeaseItemService;
import gbicc.irs.assetsRating.service.RatingAssetsStepService;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.service.impl.MainRatingServiceImpl;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.GenerateModel;
import gbicc.irs.main.rating.vo.ScoreDetail;


/**
 * @主体评级相关操作服务类
 * @author wsh
 *
 */
@Service
public class AssetsLeaseItemServiceImpl extends 
DaoServiceImpl<AssetsLeaseItem, String, AssetsLeaseItemRepository> 
implements AssetsLeaseItemService {

	
	
	
	

	
}
