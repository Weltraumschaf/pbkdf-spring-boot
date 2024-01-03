package de.weltraumschaf.special.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // https://www.webjars.org/documentation#springmvc
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("/webjars/");
        // https://www.baeldung.com/spring-mvc-static-resources
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }
}
