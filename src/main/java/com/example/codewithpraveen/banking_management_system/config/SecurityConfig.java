package com.example.codewithpraveen.banking_management_system.config;


import com.example.codewithpraveen.banking_management_system.Security.EntryPoint;
import com.example.codewithpraveen.banking_management_system.Security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final EntryPoint entryPoint;
	private final JwtAuthFilter jwtAuthFilter;
	private final UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfig(EntryPoint entryPoint, JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		this.entryPoint = entryPoint;
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authz -> authz
				// Public endpoints - only authentication related
				.requestMatchers("/api/users/create").permitAll()
				.requestMatchers("/api/users/login").permitAll()
				.requestMatchers("/api/users/refresh-token").permitAll()
				.requestMatchers("/api/users/logout").permitAll()
				
				// Employee authentication endpoints only
				.requestMatchers(HttpMethod.POST, "/employee/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/employee/refresh-token").permitAll()
				.requestMatchers(HttpMethod.POST, "/employee/logout").permitAll()
				
				// Health check and actuator endpoints
				.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/api/health").permitAll()
				
				// Swagger/OpenAPI endpoints
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers("/v3/api-docs/**").permitAll()
				.requestMatchers("/swagger-resources/**").permitAll()
				.requestMatchers("/webjars/**").permitAll()
				
				// All other requests require authentication
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
