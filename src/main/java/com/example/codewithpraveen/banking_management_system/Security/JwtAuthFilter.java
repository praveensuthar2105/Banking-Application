package com.example.codewithpraveen.banking_management_system.Security;

import com.example.codewithpraveen.banking_management_system.Service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserServices userServices;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		
		String token = extractTokenFromRequest(request);
		
		if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			authenticateUser(request, token);
		}
		
		filterChain.doFilter(request, response);
	}
	
	/**
	 * Extract JWT token from Authorization header
	 */
	private String extractTokenFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		
		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
			return authorizationHeader.substring(BEARER_PREFIX.length());
		}
		
		return null;
	}
	
	/**
	 * Authenticate user based on JWT token
	 */
	private void authenticateUser(HttpServletRequest request, String token) {
		try {
			String username = jwtService.getUsernameFromToken(token);
			
			if (username != null) {
				UserDetails userDetails = loadUserDetails(username);
				
				if (userDetails != null && jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
						);
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
					logger.debug("Successfully authenticated user: {}", username);
				} else {
					logger.warn("Token validation failed for user: {}", username);
				}
			}
		} catch (JwtException e) {
			logger.warn("JWT authentication failed: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error during JWT authentication", e);
		}
	}
	
	/**
	 * Load user details with error handling
	 */
	private UserDetails loadUserDetails(String username) {
		try {
			return userServices.loadUserByUsername(username);
		} catch (UsernameNotFoundException e) {
			logger.warn("User not found: {}", username);
			return null;
		} catch (Exception e) {
			logger.error("Error loading user details for: {}", username, e);
			return null;
		}
	}
	
	/**
	 * Skip JWT authentication for public endpoints
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		
		// Skip authentication for public endpoints
		return path.startsWith("/api/auth/") ||
			   path.startsWith("/api/public/") ||
			   path.equals("/api/health") ||
			   path.startsWith("/swagger-") ||
			   path.startsWith("/v3/api-docs");
	}
}
