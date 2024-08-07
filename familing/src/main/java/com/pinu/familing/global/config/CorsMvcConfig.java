package com.pinu.familing.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        //mvc에서 cors 해결
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true); // 필요에 따라 쿠키 포함
        }
}