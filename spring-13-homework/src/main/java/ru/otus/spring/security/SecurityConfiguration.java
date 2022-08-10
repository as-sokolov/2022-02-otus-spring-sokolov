package ru.otus.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.spring.service.UserAuthorizationServiceImpl;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${remembermekey}")
    private String remembermekey;

    private final UserAuthorizationServiceImpl userAuthorizationServiceImpl;

    public SecurityConfiguration(UserAuthorizationServiceImpl userAuthorizationServiceImpl) {
        this.userAuthorizationServiceImpl = userAuthorizationServiceImpl;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/")
                .antMatchers( "/static/**" );
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/books/**", "/authors/**", "/genres/**", "/comments/**").hasAnyRole("ADMIN", "USER", "PUSHKIN")
                .and()
                .formLogin()
                .and()
                .anonymous()
                .principal("anonymous")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .key(remembermekey)
        ;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthorizationServiceImpl);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
