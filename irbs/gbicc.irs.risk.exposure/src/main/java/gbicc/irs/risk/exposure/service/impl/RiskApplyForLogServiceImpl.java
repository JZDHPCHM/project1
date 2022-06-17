package gbicc.irs.risk.exposure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.querydsl.core.BooleanBuilder;

import gbicc.irs.risk.exposure.entity.RiskApplyForLogEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskApplyForLogRepository;
import gbicc.irs.risk.exposure.service.RiskApplyForLogService;
import org.wsp.framework.security.util.SecurityUtil;

import java.util.List;

@Service
public class RiskApplyForLogServiceImpl  extends DaoServiceImpl<RiskApplyForLogEntity, String,RiskApplyForLogRepository> implements RiskApplyForLogService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public Page<RiskApplyForLogEntity> findByRiskExporeId(String riskExporeId, Pageable pageable) {
		String sql = "select l.role_name as roleName,l.fd_last_modifier||'/'||u.fd_username as lastModifier, l.MAINTAIN_DT as maintainDt,\n" +
				"l.MAINTAIN_RESULT as maintainResult,l.reason as reason, \n" +
				" l.operate,b.fd_name as org \n" +
				" from  irs_risk_apply_for_log l \n" +
				"left join fr_aa_user u\n" +
				"on l.fd_creator = u.fd_loginname \n " +
                "left join fr_aa_user_org a\n" +
                "on a.fd_user_id = u.fd_id\n" +
                "left join fr_aa_org b\n" +
                "on a.fd_org_id = b.fd_id \n"+
				" where l.risk_exposure_id = '" +riskExporeId + "' and l.role_name is not null order by l.MAINTAIN_DT desc ";
		List<RiskApplyForLogEntity> riskLst = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RiskApplyForLogEntity>(RiskApplyForLogEntity.class));
		return new PageImpl<RiskApplyForLogEntity>(riskLst,pageable,riskLst.size());
	}

	@Override
	public void deleteByRiskExposureIdAndProcessorNot(String piskExporeId, String processor) {
		repository.deleteByRiskExposureIdAndProcessorNot(piskExporeId, processor);
	}



}
