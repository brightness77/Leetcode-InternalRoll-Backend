package com.b77.leetcodeapi.leetcodeapi.configuration;

import com.b77.leetcodeapi.leetcodeapi.handler.LoginSuccessHandler;
import com.b77.leetcodeapi.leetcodeapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    private UserService userService;


    @Bean
    LoginSuccessHandler getNewLoginSuccessHandler() {
        return new LoginSuccessHandler();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/helloworld/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/billboard/**").permitAll()
                .anyRequest().authenticated()
        .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(getNewLoginSuccessHandler())
        .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("rollcode-session")
                .permitAll()
        .and()
            .rememberMe()
                .alwaysRemember(true)
                .useSecureCookie(true)
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("rollcode-session")
                .userDetailsService(userService)
        .and()
            .csrf().disable();

        return http.build();
    }


}
