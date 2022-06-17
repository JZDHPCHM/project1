package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishBasisEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishBasisRepository;
import gbicc.irs.fbinterface.service.FbPunishBasisService;

/**
 * 行政处罚处罚依据相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishBasisServiceImpl extends DaoServiceImpl<FbPunishBasisEntity, String, FbPunishBasisRepository> implements FbPunishBasisService{

}
