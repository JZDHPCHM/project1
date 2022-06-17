package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureSourceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureSourceRepository;

/**
 * 信息披露公告来源相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbInfoDisclosureSourceService extends DaoService<FbInfoDisclosureSourceEntity, String, FbInfoDisclosureSourceRepository>{

}
