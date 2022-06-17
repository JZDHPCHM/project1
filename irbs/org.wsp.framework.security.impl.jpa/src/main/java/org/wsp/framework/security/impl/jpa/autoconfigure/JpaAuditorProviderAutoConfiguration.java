package org.wsp.framework.security.impl.jpa.autoconfigure;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.wsp.framework.security.util.SecurityUtil;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class JpaAuditorProviderAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
    public AuditorAware<String> auditorProvider() {
        return new UsernameAuditorAware();
    }
	
	private static class UsernameAuditorAware implements AuditorAware<String>{
		@Override
		public Optional<String> getCurrentAuditor() {
			String loginName =SecurityUtil.getLoginName();
			if(loginName==null) {
				loginName ="system";
			}
			return Optional.of(loginName);
		}
	}
}
