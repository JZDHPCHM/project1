package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticeOtherEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticeOtherRepository;
import gbicc.irs.fbinterface.service.FbOpeningNoticeOtherService;

/**
 * 开庭公告其他角色相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbOpeningNoticeOtherServiceImpl extends DaoServiceImpl<FbOpeningNoticeOtherEntity, String, FbOpeningNoticeOtherRepository> implements FbOpeningNoticeOtherService{

}
