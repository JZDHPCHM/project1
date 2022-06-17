package net.gbicc.app.irr.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexOptionRepository;

/**
* 指标选项
*/
public interface IrrIndexOptionService extends DaoService<IrrIndexOptionEntity, String, IrrIndexOptionRepository> {

	/**
	 * 查询每个选项评估项目扣分最低的选项
	 * @return
	 */
	public List<IrrIndexOptionEntity> findAllMaxPoints();
	
}
