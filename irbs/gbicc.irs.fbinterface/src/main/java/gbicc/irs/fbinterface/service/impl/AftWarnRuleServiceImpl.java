package gbicc.irs.fbinterface.service.impl;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.gbicc.aicr.system.util.AppUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import gbicc.irs.fbinterface.jpa.entity.AftWarnInfoDistinctEntity;
import gbicc.irs.fbinterface.jpa.entity.AftWarnRuleEntity;
import gbicc.irs.fbinterface.jpa.entity.QAftWarnInfoDistinctEntity;
import gbicc.irs.fbinterface.jpa.entity.QAftWarnRuleEntity;
import gbicc.irs.fbinterface.jpa.repository.AftWarnRuleRepository;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.service.AftWarnRuleService;

/**
 * 预警规则相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月22日
 */
@Service
public class AftWarnRuleServiceImpl extends DaoServiceImpl<AftWarnRuleEntity, String, AftWarnRuleRepository> implements AftWarnRuleService{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    
    private JPAQueryFactory queryFactory;
    
    @PostConstruct
    public void initChild() {
        queryFactory = new JPAQueryFactory(entityManager);
    }
    
    @Override
    public List<String> getRuleType() {
        QAftWarnRuleEntity qAftWarnRule = QAftWarnRuleEntity.aftWarnRuleEntity;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        builder.and(qAftWarnRule.isValid.eq(FbCommonEnums.IS_Y.getValue()));
        builder.and(qAftWarnRule.isDelete.eq(FbCommonEnums.IS_N.getValue()).or(qAftWarnRule.isDelete.isNull()));
        
        List<String> ruleTypeList = queryFactory.selectDistinct(qAftWarnRule.ruleType)
                .from(qAftWarnRule).where(builder).fetch();
        
        return ruleTypeList;
    }

    @Override
    public List<String> getRuleSubType() {
        QAftWarnRuleEntity qAftWarnRule = QAftWarnRuleEntity.aftWarnRuleEntity;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        builder.and(qAftWarnRule.isValid.eq(FbCommonEnums.IS_Y.getValue()));
        builder.and(qAftWarnRule.isDelete.eq(FbCommonEnums.IS_N.getValue()).or(qAftWarnRule.isDelete.isNull()));
        
        List<String> ruleTypeList = queryFactory.selectDistinct(qAftWarnRule.ruleSubType)
                .from(qAftWarnRule).where(builder).fetch();
        
        return ruleTypeList;
    }

    @Override
    public List<String> getRuleCode() {
        QAftWarnRuleEntity qAftWarnRule = QAftWarnRuleEntity.aftWarnRuleEntity;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        builder.and(qAftWarnRule.isValid.eq(FbCommonEnums.IS_Y.getValue()));
        builder.and(qAftWarnRule.isDelete.eq(FbCommonEnums.IS_N.getValue()).or(qAftWarnRule.isDelete.isNull()));
        
        List<String> ruleTypeList = queryFactory.selectDistinct(qAftWarnRule.ruleCode)
                .from(qAftWarnRule).where(builder).fetch();
        
        return ruleTypeList;
    }

    @Override
    public void distinctInDistinct(String taskseqno) throws Exception{
        
        List<String> ruleCodeList = getRuleCode();
        
        //如果没有规则，不再进行操作
        if(CollectionUtils.isEmpty(ruleCodeList)) {
            return;
        }
        
        QAftWarnInfoDistinctEntity qAftWarnInfoDistinct = QAftWarnInfoDistinctEntity.aftWarnInfoDistinctEntity;
        
        //查询原表
        String infoSql = "select rule_code,task_seqno,lessee_id,warn_desc,asso_type,asso_id,warn_time from t_aft_warn_info"
                + " where to_char(warn_time,'yyyyMMdd')='"+taskseqno+"' and rule_code='";
        
        for(String ruleCode:ruleCodeList) {
            List<AftWarnInfoDistinctEntity> aftWarnInfoList = jdbcTemplate.query(infoSql + ruleCode + "'", new Object[]{}, new BeanPropertyRowMapper<AftWarnInfoDistinctEntity>(AftWarnInfoDistinctEntity.class));
            
            if(CollectionUtils.isEmpty(aftWarnInfoList)) {
                continue;
            }
            List<AftWarnInfoDistinctEntity> aftWarnInfoListDistinct = aftWarnInfoList.stream().collect(
                    collectingAndThen(toCollection(() -> new TreeSet<>(
                                    comparing(entity -> entity.getRuleCode()+";"+entity.getLesseeId() + ";" 
                                            + entity.getAssoId()+";"+entity.getAssoType()+";"+entity.getWarnDesc()
                                    ))
                        ), ArrayList::new)
            );
            
            //已经预警规则
            List<AftWarnInfoDistinctEntity> aftWarnInfoDistinctList = queryFactory.select(qAftWarnInfoDistinct)
                    .from(qAftWarnInfoDistinct).where(qAftWarnInfoDistinct.ruleCode.eq(ruleCode)).fetch();
            //去除
            if(CollectionUtils.isNotEmpty(aftWarnInfoDistinctList)) {
                aftWarnInfoListDistinct.removeAll(aftWarnInfoDistinctList);
            }
            if(CollectionUtils.isNotEmpty(aftWarnInfoListDistinct)) {
                saveDataToDB(aftWarnInfoListDistinct);
            }
        }
        
    }

    /**
     * 批量插入数据
     *
     * @param list
     * @param clz
     * @param jdbcTemplate
     * @return
     * @throws Exception
     */
    private Map<String, Object> saveDataToDB(List<AftWarnInfoDistinctEntity> list) throws Exception{
        if(CollectionUtils.isEmpty(list)) {
            return AppUtil.getMap(true);
        }
        
        String sql = "insert into t_aft_warn_info_distinct(id,rule_code,lessee_id,warn_desc,asso_type,asso_id,warn_time) values(?,?,?,?,?,?,?)";
        
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, list.get(i).getRuleCode());
                ps.setString(3, list.get(i).getLesseeId());
                ps.setString(4, list.get(i).getWarnDesc());
                ps.setString(5, list.get(i).getAssoType());
                ps.setString(6, list.get(i).getAssoId());
                ps.setTimestamp(7, new java.sql.Timestamp(list.get(i).getWarnTime().getTime()));
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
        
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }
}
