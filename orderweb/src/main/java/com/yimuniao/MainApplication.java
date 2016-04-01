package com.yimuniao;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// importResource can init the beans defined in xml.
//@ImportResource({ "spring-context1.xml", "spring-context2.xml" })
@SpringBootApplication
public class MainApplication {

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new MySecurityConfigurer();
    }

    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    public static class MySecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.inMemoryAuthentication()
                   .withUser("user")
                   .password("user")
                   .roles("USER").and()
                   .withUser("admin")
                   .password("admin")
                   .roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        }

    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(MainApplication.class);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);

    }
}
