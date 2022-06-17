package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureSourceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureSourceRepository;
import gbicc.irs.fbinterface.service.FbInfoDisclosureSourceService;

/**
 * 信息披露公告来源相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbInfoDisclosureSourceServiceImpl extends DaoServiceImpl<FbInfoDisclosureSourceEntity, String, FbInfoDisclosureSourceRepository> implements FbInfoDisclosureSourceService{

}
