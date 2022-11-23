package com.b77.leetcodeapi.leetcodeapi.configuration;

import com.b77.leetcodeapi.leetcodeapi.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;


@Configuration
public class SecurityConfiguration {

    @Bean
    LoginSuccessHandler getNewLoginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()
                //.antMatchers("/").permitAll()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(getNewLoginSuccessHandler())
                .and()
            .logout().permitAll();

        return http.build();
    }

}
