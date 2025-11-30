package dev.huangzutong.mentalhealthsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    /**
     * 添加静态资源文件，外部可以直接访问地址
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatar/**").
                addResourceLocations("file:avatar");
        registry.addResourceHandler("/**").
                addResourceLocations(new String[]{
                        "classpath:/static",
                        "classpath:/public",
                        "classpath:/resources",
                        "classpath:/META-INF/resources"
                });
        super.addResourceHandlers(registry);
    }
}
