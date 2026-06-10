package com.computer.config;

import com.computer.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 目录型 file: 资源位置必须以 / 结尾，否则在部分环境下无法正确映射静态文件
        String resourceLocation = Paths.get(uploadPath).toAbsolutePath().normalize().toUri().toString();
        if (!resourceLocation.endsWith("/")) {
            resourceLocation += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(resourceLocation);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/user/login", "/api/user/admin/login", "/api/user/register",
                        "/uploads/**", "/api/goods/list", "/api/goods/detail/**",
                        "/api/goods/type/list", "/api/slides/list",
                        "/api/article/list", "/api/article/detail/**",
                        "/api/forum/list", "/api/forum/detail/**",
                        "/api/notice/list", "/api/notice/detail/**",
                        "/api/comment/list/**", "/api/score/list/**"
                );
    }
}
