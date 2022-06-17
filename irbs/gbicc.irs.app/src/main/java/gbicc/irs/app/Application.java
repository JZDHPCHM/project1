package gbicc.irs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.WebApplicationInitializer;
import org.wsp.framework.mvc.SpringBootWebApplicationServletInitializer;

import gbicc.irs.fbinterface.jpa.support.config.FbThreadExecutorConfig;

/**
 * 该类为整个应用的入口，具有多种启动方式：
 * 1. 直接通过 java -jar xxxxxx.war 方式运行，即直接调用 main() 方法，并启动嵌入式 web 容器
 * 2. 可直接部署到支持 servlet 3.0 的容器中，通过 extends SpringBootWebApplicationServletInitializer 实现
 * 3. 为了能够在支持 servlet 3.0 的 weblogic 容器中运行，需要显示实现 WebApplicationInitializer 接口
 * @author wangshaoping
 *ashuo 
 */
@SpringBootApplication
@EnableAsync
@Import(value= {FbThreadExecutorConfig.class})
public class Application{
	
	
	public static void main(String[] args) {
		SpringApplication app =new SpringApplication(Application.class);
		app.run(args);
	}
}
