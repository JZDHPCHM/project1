package org.wsp.framework.security.impl.jpa.autoconfigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.wsp.framework.core.Environment;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.liquibase.service.DatabaseSchemaUpdateService;
import org.wsp.framework.security.contribution.ContributionItemManager;
import org.wsp.framework.security.impl.jpa.component.FrameworkDaoAuthenticationProvider;
import org.wsp.framework.security.impl.jpa.filter.TokenAuthenticationFilter;
import org.wsp.framework.security.impl.jpa.filter.UsernamePasswordWithIpAuthenticationFilter;
import org.wsp.framework.security.impl.jpa.service.impl.JpaUserWithIpDetailsServiceImpl;

@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class WebSecurityAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public UserDetailsService userDetailsService(){
		return new JpaUserWithIpDetailsServiceImpl();
	}
	
	@Bean
	public WebSecurityConfigurerAdapter commonWebSecurityConfigurerAdapter(){
		return new CommonWebSecurityConfigurerAdapter();
	}
	
	private static class CommonWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
		@Autowired protected org.springframework.core.env.Environment environment;
		@Autowired private UserDetailsService userDetailsService;
		@Autowired private PasswordEncoder passwordEncoder;
		@Autowired private DatabaseSchemaUpdateService databaseSchemaUpdateService;
		@Autowired private AccessLogService accessLogService;
		
		@Autowired
		protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}

		@Override
		protected AuthenticationManager authenticationManager() throws Exception {
			FrameworkDaoAuthenticationProvider provider =new FrameworkDaoAuthenticationProvider();
			provider.setPasswordEncoder(passwordEncoder);
			provider.setUserDetailsService(userDetailsService);
			provider.setAccessLogService(accessLogService);
			ProviderManager authenticationManager = new ProviderManager(Arrays.asList(provider));
			//不擦除认证密码，擦除会导致TokenBasedRememberMeServices因为找不到Credentials再调用UserDetailsService而抛出UsernameNotFoundException
			authenticationManager.setEraseCredentialsAfterAuthentication(false);
			return authenticationManager;
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//防止出现 changeSessionId 错误
			http.sessionManagement().sessionFixation().newSession();
			//系统需要安装但还没安装时，不进行安全控制
			if(Environment.isInstallerNeeded() && !Environment.isInstalled()){
				http.authorizeRequests().anyRequest().permitAll();
				http.csrf().disable();
				return;
			}
			
			if(Environment.isEnableDbSchemaUpdateCheck()){
				//系统需要对数据库表结构进行更新时，不进行安全控制
				if(databaseSchemaUpdateService.checkIsNeedMigration()){
					http.authorizeRequests().anyRequest().permitAll();
					http.csrf().disable();
					return;
				}
			}
			
			//处理 security.require-ssl 配置
			//if (this.security.isRequireSsl()) {
			//	http.requiresChannel().anyRequest().requiresSecure();
			//}
			//处理 security.enable-csrf 配置
			//if (this.security.isEnableCsrf()) {
			//	CsrfProtectionMatcher csrfProtectionMatcher =new CsrfProtectionMatcher(environment);
			//	http.csrf().requireCsrfProtectionMatcher(csrfProtectionMatcher);
			//}else{
				http.csrf().disable();
			//}
			
			//如果应用采用多法人模式，需要根据客户端请求的IP来判断所属法人,采用自定义认证 Filter 代替系统默认用户名密码认证 Filter
			UsernamePasswordWithIpAuthenticationFilter usernamePasswordWithIpAuthenticationFilter =new UsernamePasswordWithIpAuthenticationFilter();
			usernamePasswordWithIpAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
			usernamePasswordWithIpAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(environment.getProperty("security.http.formLogin.failureUrl","/login-error.html")));
			http.addFilterAt(usernamePasswordWithIpAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			
			//添加基于加密 url token 的认证模式
			TokenAuthenticationFilter tokenAuthenticationFilter =new TokenAuthenticationFilter();
			http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
			
			//处理 security.sessions 配置
			//http.sessionManagement().sessionCreationPolicy(this.security.getSessions());
			
			//处理 
			//    security.headers.xss
			//    security.headers.cache
			//    security.headers.frame
			//    security.headers.content-type
			//    security.headers.hsts
			//配置
			//SpringBootWebSecurityConfiguration.configureHeaders(http.headers(),this.security.getHeaders());
			
			//处理 security.ignored 配置
			//List<String> ignored = new ArrayList<String>(security.getIgnored());
			List<String> ignored = new ArrayList<String>();
			//合并安全设置贡献项和 security.ignored 配置
			List<String> ignoredContributions =ContributionItemManager.getInstance().getAllIgnoredSecurityControllerUrlMatchers();
			for(String ignoredContribution : ignoredContributions){
				ignored.add(environment.resolvePlaceholders(ignoredContribution));
			}
			
			if(ignored!=null && !ignored.isEmpty()){
				http.authorizeRequests().antMatchers(ignored.toArray(new String[]{})).permitAll();
			}
			
			//设置其他 url 模式都需要进行安全认证
			http.authorizeRequests().anyRequest().authenticated();
			
			/*
			FrameworkVoter frameworkVoter =new FrameworkVoter(environment,ignored);
			UnanimousBased unanimousBased =new UnanimousBased(Collections.singletonList(frameworkVoter));
			http.authorizeRequests().accessDecisionManager(unanimousBased);
			*/
			//http.authorizeRequests().anyRequest().access("@frUrlAutorization.vote(authentication,request)");
			
			//覆盖默认的重定向入口点，以便对于不同的请求进行不同的处理，具体说明请参见 LoginUrlAuthenticationEntryPoint 类
			http.exceptionHandling().authenticationEntryPoint(new AjaxLoginUrlAuthenticationEntryPoint(environment.getProperty("security.http.formLogin.loginPage", "/login")));
			
			//设置允许 iframe
			http.headers().frameOptions().sameOrigin();
			
			//设置 login 表单相关设置
			http.formLogin()
					.loginPage(environment.getProperty("security.http.formLogin.loginPage", "/login"))
					.loginProcessingUrl("/login")
					.failureUrl(environment.getProperty("security.http.formLogin.failureUrl","/login-error"))
					.permitAll()
					.and()
				//.httpBasic()
					//.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl(environment.getProperty("security.http.logout.logoutSuccessUrl","/"));
				
		}
	}
	
	static class CsrfProtectionMatcher implements RequestMatcher{
		private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
		private List<AntPathRequestMatcher> excludeMatchers =new ArrayList<AntPathRequestMatcher>();

		public CsrfProtectionMatcher(org.springframework.core.env.Environment environment){
			List<String> ignoreds =ContributionItemManager.getInstance().getAllIgnoredCsrfProtectedUrlMatchers();
			for(String ignored : ignoreds){
				excludeMatchers.add(new AntPathRequestMatcher(environment.resolvePlaceholders(ignored)));
			}
		}
		
        public boolean matches(HttpServletRequest request) {
        	boolean allowed =allowedMethods.matcher(request.getMethod()).matches();
        	if(allowed){
        		return false;
        	}else{
	        	for(AntPathRequestMatcher matcher : excludeMatchers){
	        		if(matcher.matches(request)){
	        			return false;
	        		}
	        	}
	        	return true;
        	}
        }
	}
}
