package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbPunishTableEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishTableRepository;
/**
 * 行政处罚处罚表格相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
public interface FbPunishTableService extends DaoService<FbPunishTableEntity, String, FbPunishTableRepository>{

}
