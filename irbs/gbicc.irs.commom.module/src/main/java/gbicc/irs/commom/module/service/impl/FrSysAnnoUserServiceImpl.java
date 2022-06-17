package gbicc.irs.commom.module.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.commom.module.jpa.entity.FrSysAnnoUser;
import gbicc.irs.commom.module.jpa.repository.FrSysAnnoUserRepository;
import gbicc.irs.commom.module.service.FrSysAnnoUserService;

/**
 * 
 * ClassName: FrSysAnnoUserServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月24日 上午9:03:58 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Service
public class FrSysAnnoUserServiceImpl
		extends
		DaoServiceImpl<FrSysAnnoUser, String, FrSysAnnoUserRepository>
		implements FrSysAnnoUserService {
}
