package gbicc.irs.fbinterface.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.AftWarnRuleEntity;
import gbicc.irs.fbinterface.jpa.repository.AftWarnRuleRepository;

/**
 * 预警规则相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月22日
 */
public interface AftWarnRuleService extends DaoService<AftWarnRuleEntity, String, AftWarnRuleRepository>{

    /**
     * 获取所有可用预警大类
     *
     * @return
     */
    List<String> getRuleType();
    /**
     * 获取所有可用预警小类
     *
     * @return
     */
    List<String> getRuleSubType();
    /**
     * 获取所有可用预警code
     *
     * @return
     */
    List<String> getRuleCode();
    /**
     * info表去重入distinct表
     *
     * @param taskseqno
     */
    void distinctInDistinct(String taskseqno) throws Exception;

}
