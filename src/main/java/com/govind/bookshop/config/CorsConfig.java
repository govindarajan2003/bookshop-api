package com.govind.bookshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the API.
 *
 * <p>This setup is intentionally permissive for local development so that a
 * Vite/React dev server (typically on ports like 5173) can call the Spring Boot
 * backend without browser CORS errors.</p>
 *
 * <p><strong>Security note:</strong> tighten these origins for production —
 * avoid wildcards and list only the trusted front-end domains.</p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Register CORS mappings for all endpoints.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // convenient for dev across different ports (e.g., http://localhost:5173)
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true);
        // TIP: For production, replace allowedOriginPatterns with .allowedOrigins("https://app.example.com")
    }
}