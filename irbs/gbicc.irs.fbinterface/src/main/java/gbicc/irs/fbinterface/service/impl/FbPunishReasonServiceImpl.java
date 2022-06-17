package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishReasonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishReasonRepository;
import gbicc.irs.fbinterface.service.FbPunishReasonService;

/**
 * 行政处罚处罚事由相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishReasonServiceImpl extends DaoServiceImpl<FbPunishReasonEntity, String, FbPunishReasonRepository> implements FbPunishReasonService{

}
