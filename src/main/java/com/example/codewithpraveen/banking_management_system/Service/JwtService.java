package com.example.codewithpraveen.banking_management_system.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	
	// Use environment variable or application.properties for secret key
	@Value("${jwt.secret:your-default-256-bit-secret-key-which-is-at-least-32-bytes-long-for-security}")
	private String secretKey;
	
	@Value("${jwt.access-token.expiration:3600000}") // 1 hour in milliseconds
	private long accessTokenValidity;
	
	@Value("${jwt.refresh-token.expiration:604800000}") // 7 days in milliseconds
	private long refreshTokenValidity;
	
	// In-memory blacklist for revoked tokens (consider using Redis in production)
	private final Set<String> blacklistedTokens = new HashSet<>();
	
	private SecretKey getSignKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	/**
	 * Generate access token with user details and roles
	 */
	public String generateAccessToken(String username, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);
		claims.put("type", "access");
		return createToken(claims, username, accessTokenValidity);
	}
	
	/**
	 * Generate refresh token
	 */
	public String generateRefreshToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", "refresh");
		return createToken(claims, username, refreshTokenValidity);
	}
	
	/**
	 * Create JWT token with specified claims and expiration
	 */
	private String createToken(Map<String, Object> claims, String username, long validity) {
		try {
			return Jwts.builder()
					.claims(claims)
					.subject(username)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + validity))
					.signWith(getSignKey())
					.compact();
		} catch (Exception e) {
			logger.error("Error creating JWT token for user: {}", username, e);
			throw new RuntimeException("Failed to create JWT token", e);
		}
	}
	
	/**
	 * Extract username from token with proper error handling
	 */
	public String getUsernameFromToken(String token) {
		try {
			if (isTokenBlacklisted(token)) {
				throw new JwtException("Token has been revoked");
			}
			return extractClaim(token, Claims::getSubject);
		} catch (ExpiredJwtException e) {
			logger.warn("JWT token expired: {}", e.getMessage());
			throw new JwtException("Token expired");
		} catch (MalformedJwtException e) {
			logger.warn("Invalid JWT token: {}", e.getMessage());
			throw new JwtException("Invalid token format");
		} catch (SignatureException e) {
			logger.warn("JWT signature validation failed: {}", e.getMessage());
			throw new JwtException("Invalid token signature");
		} catch (UnsupportedJwtException e) {
			logger.warn("Unsupported JWT token: {}", e.getMessage());
			throw new JwtException("Unsupported token");
		} catch (IllegalArgumentException e) {
			logger.warn("JWT token compact of handler are invalid: {}", e.getMessage());
			throw new JwtException("Invalid token");
		}
	}
	
	/**
	 * Extract role from token
	 */
	public String getRoleFromToken(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}
	
	/**
	 * Extract token type (access/refresh)
	 */
	public String getTokenType(String token) {
		return extractClaim(token, claims -> claims.get("type", String.class));
	}
	
	/**
	 * Validate token with enhanced security checks
	 */
	public boolean validateToken(String token, UserDetails userDetails) {
		try {
			if (isTokenBlacklisted(token)) {
				logger.warn("Attempted to use blacklisted token for user: {}", userDetails.getUsername());
				return false;
			}
			
			final String username = getUsernameFromToken(token);
			final String tokenType = getTokenType(token);
			
			// Ensure it's an access token
			if (!"access".equals(tokenType)) {
				logger.warn("Invalid token type used for authentication: {}", tokenType);
				return false;
			}
			
			boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
			
			if (!isValid) {
				logger.warn("Token validation failed for user: {}", userDetails.getUsername());
			}
			
			return isValid;
		} catch (Exception e) {
			logger.error("Error validating token for user: {}", userDetails.getUsername(), e);
			return false;
		}
	}
	
	/**
	 * Validate refresh token
	 */
	public boolean validateRefreshToken(String token, String username) {
		try {
			if (isTokenBlacklisted(token)) {
				return false;
			}
			
			final String tokenUsername = getUsernameFromToken(token);
			final String tokenType = getTokenType(token);
			
			return "refresh".equals(tokenType) &&
				   username.equals(tokenUsername) &&
				   !isTokenExpired(token);
		} catch (Exception e) {
			logger.error("Error validating refresh token for user: {}", username, e);
			return false;
		}
	}
	
	/**
	 * Check if token is expired
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	/**
	 * Extract expiration date from token
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	/**
	 * Get token expiration time in milliseconds
	 */
	public long getExpirationTime(String token) {
		return extractExpiration(token).getTime();
	}
	
	/**
	 * Generic method to extract claims
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * Extract all claims from token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	/**
	 * Revoke/blacklist a token
	 */
	public void revokeToken(String token) {
		try {
			blacklistedTokens.add(token);
			logger.info("Token revoked successfully");
		} catch (Exception e) {
			logger.error("Error revoking token", e);
		}
	}
	
	/**
	 * Check if token is blacklisted
	 */
	public boolean isTokenBlacklisted(String token) {
		return blacklistedTokens.contains(token);
	}
	
	/**
	 * Clean up expired tokens from blacklist (should be called periodically)
	 */
	public void cleanupExpiredBlacklistedTokens() {
		blacklistedTokens.removeIf(token -> {
			try {
				return isTokenExpired(token);
			} catch (Exception e) {
				// If we can't parse the token, remove it from blacklist
				return true;
			}
		});
	}
}
