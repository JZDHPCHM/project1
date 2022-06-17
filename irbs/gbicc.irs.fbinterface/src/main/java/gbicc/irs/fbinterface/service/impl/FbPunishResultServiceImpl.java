package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishResultEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishResultRepository;
import gbicc.irs.fbinterface.service.FbPunishResultService;

/**
 * 行政处罚处罚决定相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishResultServiceImpl extends DaoServiceImpl<FbPunishResultEntity, String, FbPunishResultRepository> implements FbPunishResultService{

}
