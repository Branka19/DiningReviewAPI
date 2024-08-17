package com.codecademy.diningreviewapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.codecademy.diningreviewapi.service.DiningReviewUserService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class DiningSecurityConfiguration {
	
	@Autowired
	private final DiningReviewUserService userService;
	
	@Bean
	public UserDetailsService userDetailService()
	{
		return userService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		return httpSecurity
		.csrf(AbstractHttpConfigurer::disable)
		
		.formLogin(httpForm -> {
			httpForm.loginPage("/login").permitAll();
			})
		
		.authorizeHttpRequests(registry -> {
			registry.requestMatchers("/", "/addNew", "/static/**", "/css/**", "js/**").permitAll().anyRequest().anonymous();
		
			})
		
		.build();
	}

}
