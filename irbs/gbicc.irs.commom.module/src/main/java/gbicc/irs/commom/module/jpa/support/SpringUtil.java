package gbicc.irs.commom.module.jpa.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring工具类
 * @author koupengyang
 *
 */

@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringUtil.applicationContext==null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	  *通过指定name获取bean
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	/**
	  *通过class获取Bean
	 * @param clazz
	 * @return
	 */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
 
    /**
           *通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
