package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventPunishSourceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishSourceRepository;
import gbicc.irs.fbinterface.service.FbEventPunishSourceService;
/**
 * 事件检索具体事件条目原文相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventPunishSourceServiceImpl extends DaoServiceImpl<FbEventPunishSourceEntity, String, FbEventPunishSourceRepository> implements FbEventPunishSourceService{

}
