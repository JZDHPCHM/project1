package com.gbicc.after.warning.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.engine.model.server.jpa.entity.ParameterEntity;
import org.wsp.engine.model.server.jpa.repository.ParameterRepository;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemParameterService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbicc.after.warning.entity.AfterWarnRule;
import com.gbicc.after.warning.entity.QAfterWarnRule;
import com.gbicc.after.warning.repository.AfterWarnRuleRepository;
import com.gbicc.after.warning.service.AfterWarnRuleService;
import com.gbicc.after.warning.util.PageBySql;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class AfterWarnRuleServiceImpl extends DaoServiceImpl<AfterWarnRule, String, AfterWarnRuleRepository>
        implements AfterWarnRuleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SystemParameterService systemParameterService;
    private JPAQueryFactory queryFactory;
    private static final String JSON_QUERY_RULECODE = "query_ruleCode";
    private static final String JSON_QUERY_RULENAME = "query_ruleName";
    private static final String JSON_QUERY_RULETYPE = "query_ruleType";
    private static final String JSON_QUERY_RULESUBTYPE = "query_ruleSubType";
    private static final String JSON_QUERY_WARNLEVEL = "query_warnLevel";
    private static final String JSON_QUERY_RULEFREQUENCY = "query_ruleFrequency";
    private static final String JSON_QUERY_ISVALID = "query_isValid";

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(this.entityManager);
    }

    public Map<String, Object> pageQueryAfterWarnRule(String data, Integer pageIndex, Integer pageSize)
            throws Exception {
        String dataBaseType = this.systemParameterService.getParameter("parameter.me.server.after.rule.database.type");
        StringBuffer buffer = new StringBuffer();
        List args = new ArrayList();
        if (dataBaseType.equals("PostgreSql")) {
            buffer.append("WITH RECURSIVE T (fd_id, fd_name, fd_parent_id, DEPTH)  AS ( ");
            buffer.append(
                    " SELECT fd_id, fd_name, fd_parent_id,1 AS DEPTH FROM me_resource WHERE fd_code='AFTER_RULE' ");
            buffer.append(" UNION ALL ");
            buffer.append(" SELECT mr.fd_id,mr.fd_name,mr.fd_parent_id,T.DEPTH+1 AS DEPTH FROM me_resource mr ");
            buffer.append(" JOIN T ON mr.fd_parent_id= T.fd_id ");
            buffer.append(") ");
            buffer.append(
                    "SELECT awr.id,m.fd_id as model_id,m.fd_code,m.fd_name,awr.rule_type,wrt1.rule_type_name as rule_type_name, ");
            buffer.append("awr.rule_sub_type,wrt2.rule_type_name as rule_sub_type_name,awr.rele_type,awr.warn_level, ");
            buffer.append(
                    "awr.frequency,awr.trig_desc,awr.is_valid,awr.fd_last_modifier,to_char(awr.fd_last_modifydate,'yyyy-mm-dd') as fd_last_modifydate FROM T ");
            buffer.append("LEFT JOIN me_resource m on t.fd_id=m.fd_id ");
            buffer.append("LEFT JOIN t_aft_warn_rule awr on m.fd_code=awr.rule_code ");
            buffer.append("LEFT JOIN t_warn_rule_type wrt1 on awr.rule_type=wrt1.rule_type_code ");
            buffer.append("LEFT JOIN t_warn_rule_type wrt2 on awr.rule_sub_type=wrt2.rule_type_code ");
            buffer.append(
                    "where m.fd_category='RULE' and (awr.id is null or (awr.is_delete is null or awr.is_delete<>'Y')) ");
            if (StringUtils.isNotEmpty(data)) {
                Map map = (Map) this.objectMapper.readValue(data, Map.class);
                if ((null != map.get("query_ruleCode"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleCode").toString()))) {
                    buffer.append(" and m.fd_code like ? ");
                    args.add("%" + map.get("query_ruleCode").toString() + "%");
                }
                if ((null != map.get("query_ruleName"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleName").toString()))) {
                    buffer.append(" and m.fd_name like ? ");
                    args.add("%" + map.get("query_ruleName").toString() + "%");
                }
                if ((null != map.get("query_ruleType"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleType").toString()))) {
                    buffer.append(" and awr.rule_type = ? ");
                    args.add(map.get("query_ruleType").toString());
                }
                if ((null != map.get("query_ruleSubType"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleSubType").toString()))) {
                    buffer.append(" and awr.rule_sub_type = ? ");
                    args.add(map.get("query_ruleSubType").toString());
                }
                if ((null != map.get("query_warnLevel"))
                        && (StringUtils.isNotEmpty(map.get("query_warnLevel").toString()))) {
                    buffer.append(" and awr.warn_level = ? ");
                    args.add(map.get("query_warnLevel").toString());
                }
                if ((null != map.get("query_ruleFrequency"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleFrequency").toString()))) {
                    buffer.append(" and awr.frequency = ?  ");
                    args.add(map.get("query_ruleFrequency").toString());
                }
                if ((null != map.get("query_isValid"))
                        && (StringUtils.isNotEmpty(map.get("query_isValid").toString()))) {
                    buffer.append(" and awr.is_valid = ? ");
                    args.add(map.get("query_isValid").toString());
                }
            }
        } else if (dataBaseType.equals("Oracle")) {
            buffer.append("select * from (");
            buffer.append(
                    "select awr.id as \"id\",r.fd_id as \"model_id\",r.fd_code as \"fd_code\",r.fd_name as \"fd_name\",awr.rule_type as \"rule_type\",awr.rule_type as \"rule_type_name\",");
            buffer.append(
                    "awr.rule_sub_type as \"rule_sub_type\",awr.rule_sub_type as \"rule_sub_type_name\",awr.rele_type as \"rele_type\",awr.warn_level as \"warn_level\",");
            buffer.append(
                    "awr.frequency as \"frequency\",awr.trig_desc as \"trig_desc\",awr.is_valid as \"is_valid\",awr.fd_last_modifier as \"fd_last_modifier\",");
            buffer.append("to_char(awr.fd_last_modifydate,'yyyy-mm-dd') as \"fd_last_modifydate\" ");
            buffer.append("from me_resource r ");
            buffer.append("LEFT JOIN t_aft_warn_rule awr on r.fd_code=awr.rule_code ");
            buffer.append("where fd_category='RULE' ");
            buffer.append("start with r.fd_code='AFTER_RULE' ");
            buffer.append("connect by prior r.fd_id=r.fd_parent_id) t where 1=1 ");
            if (StringUtils.isNotEmpty(data)) {
                Map map = (Map) this.objectMapper.readValue(data, Map.class);
                if ((null != map.get("query_ruleCode"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleCode").toString()))) {
                    buffer.append(" and t.\"fd_code\" like ? ");
                    args.add("%" + map.get("query_ruleCode").toString() + "%");
                }
                if ((null != map.get("query_ruleName"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleName").toString()))) {
                    buffer.append(" and t.\"fd_name\" like ? ");
                    args.add("%" + map.get("query_ruleName").toString() + "%");
                }
                if ((null != map.get("query_ruleType"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleType").toString()))) {
                    buffer.append(" and t.\"rule_type\" = ? ");
                    args.add(map.get("query_ruleType").toString());
                }
                if ((null != map.get("query_ruleSubType"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleSubType").toString()))) {
                    buffer.append(" and t.\"rule_sub_type\" = ? ");
                    args.add(map.get("query_ruleSubType").toString());
                }
                if ((null != map.get("query_warnLevel"))
                        && (StringUtils.isNotEmpty(map.get("query_warnLevel").toString()))) {
                    buffer.append(" and t.\"warn_level\" = ? ");
                    args.add(map.get("query_warnLevel").toString());
                }
                if ((null != map.get("query_ruleFrequency"))
                        && (StringUtils.isNotEmpty(map.get("query_ruleFrequency").toString()))) {
                    buffer.append(" and t.\"frequency\" = ?  ");
                    args.add(map.get("query_ruleFrequency").toString());
                }
                if ((null != map.get("query_isValid"))
                        && (StringUtils.isNotEmpty(map.get("query_isValid").toString()))) {
                    buffer.append(" and t.\"is_valid\" = ? ");
                    args.add(map.get("query_isValid").toString());
                }
            }
            buffer.append(" order by t.\"fd_code\" ");
    }
        String sql = buffer.toString();
        List list = this.jdbcTemplate.queryForList(PageBySql.sqlPage(sql, pageIndex, pageSize), args.toArray());
        Map resultMap = PageBySql.pageData(list, pageIndex, pageSize,
                (Integer) this.jdbcTemplate.queryForObject(PageBySql.sqlCount(sql), args.toArray(), Integer.class));
        return resultMap;
    }

    public List<Map<String, Object>> getRuleParameterList(String modelId) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                "select mp.fd_id as \"fd_id\",mp.fd_code as \"fd_code\",mp.fd_name as \"fd_name\",mp.fd_description as \"fd_description\",mp.fd_type as \"fd_type\",mp.fd_value_type as \"fd_value_type\",mp.fd_default_value as \"fd_default_value\",mpp.fd_script as \"fd_script\",");
        buffer.append(
                "mp.fd_last_modifier as \"fd_last_modifier\",to_char(mp.fd_last_modifydate,'yyyy-mm-dd') as \"fd_last_modifydate\" from me_parameter mp left join me_parameter_processor mpp on mp.fd_id=mpp.fd_parameter_id ");
        buffer.append(" where mp.fd_model_id = ? ");
        String sql = buffer.toString();
        return this.jdbcTemplate.queryForList(sql, new Object[] { modelId });
    }

    public QueryResults<AfterWarnRule> findWarnRule(Predicate predicate) {
        QAfterWarnRule qAfterWarnRule = QAfterWarnRule.afterWarnRule;

        JPAQuery jpaQuery = (JPAQuery) ((JPAQuery) this.queryFactory.select(qAfterWarnRule).from(qAfterWarnRule))
                .where(predicate);

        return jpaQuery.fetchResults();
    }

    public void updateIsValid(String id, String isValid) {
        Optional optional = ((AfterWarnRuleRepository) this.repository).findById(id);
        AfterWarnRule warnRule = (AfterWarnRule) optional.get();
        warnRule.setIsValid(isValid);
        ((AfterWarnRuleRepository) this.repository).save(warnRule);
    }

    public void deleteRule(String ruleCode) {
        QAfterWarnRule qAfterWarnRule = QAfterWarnRule.afterWarnRule;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAfterWarnRule.ruleCode.eq(ruleCode));
        builder.and(qAfterWarnRule.isValid.eq("Y"));
        builder.and(qAfterWarnRule.isDelete.eq("N").or(qAfterWarnRule.isDelete.isNull()));
        QueryResults result = findWarnRule(builder);
        AfterWarnRule warnRule = (AfterWarnRule) result.getResults().get(0);
        warnRule.setIsDelete("Y");
        ((AfterWarnRuleRepository) this.repository).save(warnRule);
    }

    public void updateRuleParameter(String paramId, String defaultValue) throws Exception {
        Optional optional = this.parameterRepository.findById(paramId);
        ParameterEntity pe = (ParameterEntity) optional.get();
        pe.setDefaultValue(defaultValue);
        this.parameterRepository.save(pe);
    }

    public Integer getWarnCount(String ruleCode, String taskSeqno) throws Exception {
        String tableName = this.systemParameterService.getParameter("parameter.me.server.after.rule.warning.table");
        String tableFdTaskSeqno = this.systemParameterService
                .getParameter("parameter.me.server.after.rule.warning.table.taskseqno");
        String tableFdRuleCode = this.systemParameterService
                .getParameter("parameter.me.server.after.rule.warning.table.rulecode");
        Integer count = (Integer) this.jdbcTemplate.queryForObject("select count(1) as ct from " + tableName + " where "
                + tableFdTaskSeqno + "='" + taskSeqno + "' and " + tableFdRuleCode + "='" + ruleCode + "'",
                Integer.class);
        return count;
    }
}