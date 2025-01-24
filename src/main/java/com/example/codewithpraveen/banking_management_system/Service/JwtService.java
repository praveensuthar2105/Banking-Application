package com.example.codewithpraveen.banking_management_system.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
	private  static  final String SECRET_KEY = "your-256-bit-secret-key-which-is-at-least-32-bytes-long";
	private  static final long ACCESS_TOKEN_VALIDITY = 24*5*60*1000;
//	private static final long REFRESH_TOKEN_VALIDITY = 48*60*60*1000;
	
	private static SecretKey getSignKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	public String generateToken(String username) {
		Map<String , Object> claims = new HashMap<>();
		return createToken( claims, username);
	}
	
	public String createToken(Map<String , Object> claims , String username) {
		 return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 5 minutes expiration time
                .signWith(getSignKey())
                .compact();
	}
	
	public String getUsernameFromToken(String token) {
		return  extractClaim(token , Claims::getSubject);
	}
	
	public boolean validateToken(String token , UserDetails userDetails) {
			final String username = getUsernameFromToken(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
