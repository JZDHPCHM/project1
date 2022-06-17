package net.gbicc.app.pfm.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;

/**
* 渠道绩效结果
*/
public interface PfmChannelResultService extends DaoService<PfmChannelResultEntity, String, PfmChannelResultRepository> {
	/**
	 * 绩效补录页面渠道绩效查询
	 * @param taskId
	 * @param indexCode
	 * @param indexName
	 * @param page
	 * @param size
	 * @return
	 */
    public Map<String, Object> queryChannelResult(String taskId, String indexCode, String indexName, Long page,
            Integer size);

}
