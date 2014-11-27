package showcase.boot;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String... args) {
        new SpringApplicationBuilder(Application.class).run(args);
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
