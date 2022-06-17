package gbicc.irs.fbinterface.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import gbicc.irs.fbinterface.jpa.entity.FbAftAttenCustomerEntity;
import gbicc.irs.fbinterface.jpa.entity.QFbAftAttenCustomerEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAftAttenCustomerRepository;
import gbicc.irs.fbinterface.service.FbAftAttenCustomerService;

/**
 * 客户关注相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月18日
 */
@Service
public class FbAftAttenCustomerServiceImpl extends DaoServiceImpl<FbAftAttenCustomerEntity, String, FbAftAttenCustomerRepository> implements FbAftAttenCustomerService{

    @Autowired
    private EntityManager entityManager;
    
    private JPAQueryFactory queryFactory;
    
    @PostConstruct
    public void initChild() {
        queryFactory = new JPAQueryFactory(entityManager);
    }
    
    @Override
    public List<String> queryAttenCutomerByIsAtten(String isAtten) {
        
        QFbAftAttenCustomerEntity qAftAttenCustomer = QFbAftAttenCustomerEntity.fbAftAttenCustomerEntity;
        
        List<String> assoCompanyIds = queryFactory.selectDistinct(qAftAttenCustomer.assoCompanyId)
                .from(qAftAttenCustomer).where(qAftAttenCustomer.isAtten.equalsIgnoreCase(isAtten))
                .fetch();
        
        return assoCompanyIds;
    }

    @Override
    @Transactional
    public void updateIsAttenByCompanyId(String companyId, String isAtten) {
        QFbAftAttenCustomerEntity qAftAttenCustomer = QFbAftAttenCustomerEntity.fbAftAttenCustomerEntity;
        
        queryFactory.update(qAftAttenCustomer).set(qAftAttenCustomer.isAtten, isAtten).where(qAftAttenCustomer.assoCompanyId.eq(companyId))
        .execute();
        
    }

    @Override
    @Transactional
    public List<String> queryAssoCompanyId() {
        QFbAftAttenCustomerEntity qAftAttenCustomer = QFbAftAttenCustomerEntity.fbAftAttenCustomerEntity;
        
        List<String> assoCompanyIds = queryFactory.selectDistinct(qAftAttenCustomer.assoCompanyId)
                .from(qAftAttenCustomer).fetch();
        
        return assoCompanyIds;
    }

}
