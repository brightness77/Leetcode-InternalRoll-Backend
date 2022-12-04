package com.b77.leetcodeapi.leetcodeapi;

import com.b77.leetcodeapi.leetcodeapi.provider.KeywordProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LeetcodeApiApplication {

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public KeywordProvider keywordProvider(){
		return new KeywordProvider();
	}

	public static void main(String[] args) {
		SpringApplication.run(LeetcodeApiApplication.class, args);
	}



}
