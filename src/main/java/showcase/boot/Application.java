package showcase.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import showcase.boot.domain.Contact;
import showcase.boot.domain.Customer;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }

    @Configuration
    protected static class IdExposingRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

        @Override
        protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
            config.exposeIdsFor(Customer.class, Contact.class);
        }
    }

    @Configuration
    @Profile("production")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.ldapAuthentication();
        }
    }

}
