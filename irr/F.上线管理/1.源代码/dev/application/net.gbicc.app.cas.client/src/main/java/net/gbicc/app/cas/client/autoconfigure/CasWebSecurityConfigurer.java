package net.gbicc.app.cas.client.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.wsp.framework.security.contribution.ContributionItemManager;

import net.gbicc.app.cas.client.properties.CasProperties;
import net.gbicc.app.cas.client.service.impl.CasUserDetailsService;

@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class CasWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	protected org.springframework.core.env.Environment environment;

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Autowired
	CasProperties casProperties;

	/** 定义认证用户信息获取来源，密码校验规则等 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.authenticationProvider(casAuthenticationProvider());
	}

	/** 定义安全策略 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        //防止出现 changeSessionId 错误
        http.sessionManagement().sessionFixation().newSession();
        //禁用CSRF
        http.csrf().disable();
        //处理 security.ignored 配置
		List<String> ignored = new ArrayList<String>();
        //合并安全设置贡献项和 security.ignored 配置
		List<String> ignoredContributions = ContributionItemManager.getInstance()
				.getAllIgnoredSecurityControllerUrlMatchers();
		for (String ignoredContribution : ignoredContributions) {
			ignored.add(environment.resolvePlaceholders(ignoredContribution));
		}
		if (ignored != null && !ignored.isEmpty()) {
			http.authorizeRequests().antMatchers(ignored.toArray(new String[] {})).permitAll();
		}
        //设置允许 iframe
        http.headers().frameOptions().sameOrigin();
        //设置 login 表单相关设置
        http.authorizeRequests()// 配置安全策略
                //.antMatchers("/login").permitAll()// 定义/请求不需要验证
                .anyRequest().authenticated()// 其余的所有请求都需要验证
				.and().logout().permitAll()// 定义logout不需要验证
				.and().formLogin();// 使用form表单登录

		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint()).and()
				.addFilter(casAuthenticationFilter()).addFilterBefore(casLogoutFilter(), LogoutFilter.class)
				.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
	}

	/** 认证的入口 */
	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	/** 指定service相关信息 */
	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());
		serviceProperties.setAuthenticateAllArtifacts(true);
		return serviceProperties;
	}

	/** CAS认证过滤器 */
	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
		return casAuthenticationFilter;
	}

	/** cas 认证 Provider */
	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey("casAuthenticationProviderKey");
		return casAuthenticationProvider;
	}

	/** 用户自定义的AuthenticationUserDetailsService */
	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
		return new CasUserDetailsService();
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
	}

	/** 单点登出过滤器 */
	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casProperties.getCasServerUrl());
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	/** 请求单点退出过滤器 */
	@Bean
	public LogoutFilter casLogoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(),
				new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
		return logoutFilter;
	}
}
