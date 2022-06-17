package gbicc.irs.fbinterface.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
/**
 * 风报接口service
 * 
 * @author songxubei
 * @version v1.0 2019年10月9日
 */
public interface FbInterfaceService {

    /**
     * 获取增量接口数据（除去年报、股东信息、评级记录）
     *
     * @param companyId
     * @param from
     * @return
     * @throws Exception
     */
    public Map<String, Object> getIncrementalInterfaceData(String companyId, String beginTime, String endTime, Integer from, boolean flag) throws Exception;

    /**
     * 获取关注客户初始化数据
     *
     * @param companyId
     */
    public void getAllDateFirst(String companyId) throws Exception;

    /**
     * 获取关注客户增量数据
     *
     * @param companyId
     */
    public void getIncrementalData(String companyId) throws Exception;
    
    /**
     * 从关注接口获取所有关注客户
     *
     * @return
     * @throws Exception
     */
    public List<String> getAllAttenFromFb() throws Exception;

    /**
     * 预警规则跑批
     * @param url 
     */
    public Future<Map<String,Object>> warnRuleRunner(String url);

}
