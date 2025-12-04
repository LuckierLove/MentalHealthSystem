package dev.huangzutong.mentalhealthsystem.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 */
@Configuration
public class SatokenConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle->StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/auth/register")
                .excludePathPatterns("/avatar/**")
                .excludePathPatterns("/error");
    }
}
