package com.example.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Explicitly NOT mapping `.well-known/**` to static resources
        // All other static paths can stay if needed
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }
}

