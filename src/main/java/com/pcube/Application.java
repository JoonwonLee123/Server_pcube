package com.pcube;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/*
 * @author Joonwon Lee
 */

@ComponentScan
@EnableAutoConfiguration
public class Application {
	
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultiPartConfigFactory factory = new MultiPartConfigFactory();
        factory.setMaxFileSize("1024MB");
        factory.setMaxRequestSize("1024MB");
        return factory.createMultipartConfig();
    }
	
    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
        
    }
 }
