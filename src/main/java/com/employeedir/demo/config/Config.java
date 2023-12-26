package com.employeedir.demo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		Path imageUploadDir = Paths.get("./profile-images/");
		
		String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/profile-images/**").addResourceLocations("file:/" + imageUploadPath + "/");
	}

	
	
}
