package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbEventPersonDetailEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventPersonDetailRepository;
import gbicc.irs.fbinterface.service.FbEventPersonDetailService;
/**
 * 事件检索人名实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventPersonDetailServiceImpl extends DaoServiceImpl<FbEventPersonDetailEntity, String, FbEventPersonDetailRepository> implements FbEventPersonDetailService{

}
