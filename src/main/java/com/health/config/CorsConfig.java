package com.health.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端地址（Vue默认端口8081）
        config.addAllowedOrigin("http://localhost:8081");
        // 允许所有请求方法（GET、POST、PUT等）
        config.addAllowedMethod("*");
        // 允许所有请求头（如Content-Type）
        config.addAllowedHeader("*");
        // 允许携带Cookie（可选）
        config.setAllowCredentials(true);

        // 配置哪些接口生效（/**表示所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
