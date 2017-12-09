package com.realestate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtFilterBean {
	
	 @Value("${jwt.secret}") private String secret;
	 
	 @Bean public FilterRegistrationBean jwtFiler() { final
	 FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	 registrationBean.setFilter(new JwtFilter(secret));
	 registrationBean.addUrlPatterns("/api/*");
	 
	 return registrationBean; }
	
}
