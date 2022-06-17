package net.gbicc.app.pfm.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;

/**
* 部门绩效结果
*/
public interface PfmDepaResultService extends DaoService<PfmDepaResultEntity, String, PfmDepaResultRepository> {

	/**
	 * 绩效补录页面部门绩效查询
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @param page
	 * @param size
	 * @return
	 */
    public Map<String, Object> queryDepaResult(String taskId, String indexCode, String indexName, Long page,
            Integer size);
}
