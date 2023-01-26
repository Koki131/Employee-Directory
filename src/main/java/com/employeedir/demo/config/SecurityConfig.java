package com.employeedir.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.employeedir.demo.securityservice.UserService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserService service;
	
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(provider());
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http.authorizeRequests(configure -> configure.antMatchers("/").hasRole("EMPLOYEE")
															 .antMatchers("/register/**").permitAll()
															 .antMatchers("/leaders/**").hasRole("MANAGER")
															 .antMatchers("/systems/**").hasRole("ADMIN").anyRequest().authenticated())
				
					.formLogin(configure -> configure.loginPage("/showLoginPage").loginProcessingUrl("/authenticateTheUser").permitAll())
					.logout(configure -> configure.invalidateHttpSession(true).clearAuthentication(true).permitAll())
					.exceptionHandling(configure -> configure.accessDeniedPage("/accessDenied")).build();
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider provider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
}

