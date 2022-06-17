package gbicc.irs.customer.service;

import java.util.Map;

import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.jpa.repository.DaoRepository;
import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.customer.repository.NsBpMasterRepository;

public interface NsBpMasterService extends DaoService<NsBpMaster, String, NsBpMasterRepository> {

	Map<String, String> parsingData(Map<String, Object> map);


	boolean subjects(Map<String, Object> ma, NsBpMaster master);

	//判断组织机构是否为空

}

