package com.example.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author qtx
 * @since 2023/10/10
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8888", "http://172.30.12.178:8888"));
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("POST","GET","OPTIONS","DELETE","HEAD"));
        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
