package gbicc.irs.fbinterface.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyEntity;
/**
 * 事件检索公司信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventCompanyRepository extends DaoRepository<FbEventCompanyEntity, String>{

    /**
     * 根据companyId获取以保存的记录，按创建时间倒叙排序
     *
     * @param companyId
     * @return
     */
    List<FbEventCompanyEntity> findByCompanyIdOrderByCreateDateDesc(String companyId);

}
