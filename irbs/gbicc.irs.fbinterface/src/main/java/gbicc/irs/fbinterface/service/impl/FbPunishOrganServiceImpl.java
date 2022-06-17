package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishOrganEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishOrganRepository;
import gbicc.irs.fbinterface.service.FbPunishOrganService;
/**
 * 行政处罚处罚决定机关相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishOrganServiceImpl extends DaoServiceImpl<FbPunishOrganEntity, String, FbPunishOrganRepository> implements FbPunishOrganService{

}
