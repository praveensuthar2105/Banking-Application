package com.example.codewithpraveen.banking_management_system.config;


import com.example.codewithpraveen.banking_management_system.Security.EntryPoint;
import com.example.codewithpraveen.banking_management_system.Security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig {
	
	
	private final EntryPoint entryPoint;
	
	private final JwtAuthFilter jwtAuthFilter;
	
	private final UserDetailsService userDetailsService;
	
	public SecurityConfig(EntryPoint entryPoint, JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		this.entryPoint = entryPoint;
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests( auth ->
						auth
						.requestMatchers(  "/api/users/login" , "/api/users/create" , "/employee/addEmployee"  , "/employee/login"  ).permitAll()
								.requestMatchers("/api/users/**", "api/account/**" , "/branch/**" , "/employee/**" , "/loan/**" ).authenticated()
								.requestMatchers(
//										"/api/account/**"
								  "/api/users/**",
										"/loan/**"
								).hasRole("USER")
								.requestMatchers("/api/account/**" , "/loan/**" , "/employee/**").hasRole("Manager")
								.requestMatchers("/branch/**" , "/employee/**").hasRole("RegionManager")
								.requestMatchers( "/loan/**" ).hasRole("LoanOffier")
						
				)
				.exceptionHandling( exception -> exception.authenticationEntryPoint(entryPoint))
				.sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
}
