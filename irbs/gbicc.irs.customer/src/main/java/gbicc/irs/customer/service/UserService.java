package gbicc.irs.customer.service;

import java.util.Map;

import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.jpa.service.DaoService;

public interface UserService extends DaoService<User, String, UserRepository> {
	//判断组织机构是否为空
	public Map<String, String> findByUser(String orgId);
}
