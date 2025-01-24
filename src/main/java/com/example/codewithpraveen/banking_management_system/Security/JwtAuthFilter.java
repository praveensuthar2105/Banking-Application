package com.example.codewithpraveen.banking_management_system.Security;

import com.example.codewithpraveen.banking_management_system.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private  UserServices userServices;
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String username = null;
		String token = null;
		String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			 token = authorizationHeader.substring(7);
			 username = jwtService.getUsernameFromToken(token);
			
		}
		if(username != null &&
		SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userServices.loadUserByUsername(username);
			
			if(jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
