package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbPunishTableEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishTableRepository;
import gbicc.irs.fbinterface.service.FbPunishTableService;
/**
 * 行政处罚处罚表格相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishTableServiceImpl extends DaoServiceImpl<FbPunishTableEntity, String, FbPunishTableRepository> implements FbPunishTableService{

}
