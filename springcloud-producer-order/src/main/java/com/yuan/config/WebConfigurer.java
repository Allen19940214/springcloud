package com.yuan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    private JWTLoginInterceptor jwtLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //InterceptorRegistration interceptorRegistration = registry.addInterceptor(jwtLoginInterceptor);
        //interceptorRegistration.addPathPatterns("/findAll/{pageNum}/{pageSize}");
        //设置不过滤的
       /* List<String> list = new ArrayList<String>();
        list.add("/toUserPage");
        interceptor.excludePathPatterns(list);*/
    }
}
