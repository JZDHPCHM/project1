package gbicc.irs.rti.service;

import java.util.Map;

import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.jpa.repository.DaoRepository;
import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.customer.repository.NsBpMasterRepository;

public interface RTINsBpMasterService extends DaoService<NsBpMaster, String, NsBpMasterRepository> {

	Map<String, String> parsingData(Map<String, Object> map);

}

