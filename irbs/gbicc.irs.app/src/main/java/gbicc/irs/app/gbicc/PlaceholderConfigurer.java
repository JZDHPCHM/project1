package gbicc.irs.app.gbicc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.WebApplicationInitializer;
import org.wsp.framework.mvc.SpringBootWebApplicationServletInitializer;

/**
 * @author admin
 */
@Configurable
public class PlaceholderConfigurer extends SpringBootWebApplicationServletInitializer  implements WebApplicationInitializer {
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {

        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();

        c.setIgnoreUnresolvablePlaceholders(true);

        return c;
    }
}
