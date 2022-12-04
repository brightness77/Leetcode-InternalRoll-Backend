package com.b77.leetcodeapi.leetcodeapi.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        log.info("Pre handle, request type: {}", request.getDispatcherType());

        //always check before force type cast
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String methodClass = handlerMethod.getMethod().getDeclaringClass().getName();
            String methodName = handlerMethod.getMethod().getName();

            log.info("Handler: {}, {}", methodClass, methodName);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{

        log.info("Post Handle");

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{

        log.info("After Completion");

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
