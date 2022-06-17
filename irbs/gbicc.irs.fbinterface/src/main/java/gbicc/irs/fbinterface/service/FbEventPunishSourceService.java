package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventPunishSourceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishSourceRepository;
/**
 * 事件检索具体事件条目原文
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventPunishSourceService extends DaoService<FbEventPunishSourceEntity, String, FbEventPunishSourceRepository>{

}
