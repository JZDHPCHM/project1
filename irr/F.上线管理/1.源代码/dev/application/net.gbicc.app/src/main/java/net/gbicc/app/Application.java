package net.gbicc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.WebApplicationInitializer;
import org.wsp.framework.mvc.SpringBootWebApplicationServletInitializer;
import org.wsp.framework.security.impl.jpa.autoconfigure.WebSecurityAutoConfiguration;


/**
 * 该类为整个应用的入口，具有多种启动方式：
 * 1. 直接通过 java -jar xxxxxx.war 方式运行，即直接调用 main() 方法，并启动嵌入式 web 容器
 * 2. 可直接部署到支持 servlet 3.0 的容器中，通过 extends SpringBootWebApplicationServletInitializer 实现
 * 3. 为了能够在支持 servlet 3.0 的 weblogic 容器中运行，需要显示实现 WebApplicationInitializer 接口
 * @author wangshaoping
 *
 */
@SpringBootApplication(exclude = { WebSecurityAutoConfiguration.class })
public class Application extends SpringBootWebApplicationServletInitializer implements WebApplicationInitializer{
	public static void main(String[] args) {
		SpringApplication app =new SpringApplication(Application.class);
		app.run(args);
	}
}
