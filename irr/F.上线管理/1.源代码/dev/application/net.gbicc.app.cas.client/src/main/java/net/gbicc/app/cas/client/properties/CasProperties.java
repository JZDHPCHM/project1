package net.gbicc.app.cas.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
* cas客户端类型安全配置类
*/
@Component
@PropertySource("file:${dir.config}/casclient/casclient.properties")
@ConfigurationProperties(prefix = "cas")
public class CasProperties {

	/**
	 * CAS服务地址
	 */
	private String casServerUrl;
	/**
	 * CAS服务登录地址
	 */
	private String casServerLoginUrl;
	/**
	 * CAS服务登出地址
	 */
	private String casServerLogoutUrl;
	/**
	 * 应用访问地址
	 */
	private String appServerUrl;
	/**
	 * 应用登录地址
	 */
	private String appLoginUrl;
	/**
	 * 应用登出地址
	 */
	private String appLogoutUrl;

	public String getCasServerUrl() {
		return casServerUrl;
	}

	public void setCasServerUrl(String casServerUrl) {
		this.casServerUrl = casServerUrl;
	}

	public String getCasServerLoginUrl() {
		return casServerLoginUrl;
	}

	public void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public String getCasServerLogoutUrl() {
		return casServerLogoutUrl;
	}

	public void setCasServerLogoutUrl(String casServerLogoutUrl) {
		this.casServerLogoutUrl = casServerLogoutUrl;
	}

	public String getAppServerUrl() {
		return appServerUrl;
	}

	public void setAppServerUrl(String appServerUrl) {
		this.appServerUrl = appServerUrl;
	}

	public String getAppLoginUrl() {
		return appLoginUrl;
	}

	public void setAppLoginUrl(String appLoginUrl) {
		this.appLoginUrl = appLoginUrl;
	}

	public String getAppLogoutUrl() {
		return appLogoutUrl;
	}

	public void setAppLogoutUrl(String appLogoutUrl) {
		this.appLogoutUrl = appLogoutUrl;
	}
}
