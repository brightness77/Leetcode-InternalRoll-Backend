package com.b77.leetcodeapi.leetcodeapi.configuration;

import com.b77.leetcodeapi.leetcodeapi.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new LogInterceptor());
        WebMvcConfigurer.super.addInterceptors(interceptorRegistry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
