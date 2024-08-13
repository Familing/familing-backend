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
                .allowedOrigins("*")  // 모든 도메인 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .exposedHeaders("Set-Cookie")  // 노출할 헤더
                .allowCredentials(true)  // 자격 증명 허용
                .maxAge(3600);  // preflight 요청 캐싱 시간 (초)
    }
}