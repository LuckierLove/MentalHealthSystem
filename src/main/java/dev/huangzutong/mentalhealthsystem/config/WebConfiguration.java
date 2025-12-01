package dev.huangzutong.mentalhealthsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 添加静态资源文件，外部可以直接访问地址
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatar/**").
                addResourceLocations("file:avatar");
        registry.addResourceHandler("/**").
                addResourceLocations(new String[]{
                        "classpath:/static",
                        "classpath:/public",
                        "classpath:/resources",
                        "classpath:/META-INF/resources"
                });
    }
}