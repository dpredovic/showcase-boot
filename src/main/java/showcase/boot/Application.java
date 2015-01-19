package showcase.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
    @Profile("standalone")
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class DummyAuthenticationSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.formLogin().disable();
        }
    }

    @Configuration
    @Profile("production")
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class AuthenticationSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.ldapAuthentication();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.formLogin().disable();
        }
    }

}
