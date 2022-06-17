package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventPersonDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPersonDetailRepository;
/**
 * 事件检索人名实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventPersonDetailService extends DaoService<FbEventPersonDetailEntity, String, FbEventPersonDetailRepository>{

}
