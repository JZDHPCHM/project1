package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventPunishDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishDetailRepository;
import gbicc.irs.fbinterface.service.FbEventPunishDetailService;
/**
 * 事件检索具体事件条目相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventPunishDetailServiceImpl extends DaoServiceImpl<FbEventPunishDetailEntity, String, FbEventPunishDetailRepository> implements FbEventPunishDetailService{

}
