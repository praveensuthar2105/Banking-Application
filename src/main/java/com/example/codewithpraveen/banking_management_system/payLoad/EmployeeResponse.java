package com.example.codewithpraveen.banking_management_system.payLoad;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
	private EmployeeDto employee;
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private long expiresIn; // Access token expiration time in seconds
	private LocalDateTime issuedAt;
	
	// Constructor for backward compatibility (without refresh token)
	public EmployeeResponse(EmployeeDto employee, String accessToken) {
		this.employee = employee;
		this.accessToken = accessToken;
		this.tokenType = "Bearer";
		this.issuedAt = LocalDateTime.now();
	}
	
	// Constructor with access and refresh tokens
	public EmployeeResponse(EmployeeDto employee, String accessToken, String refreshToken, long expiresIn) {
		this.employee = employee;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenType = "Bearer";
		this.expiresIn = expiresIn;
		this.issuedAt = LocalDateTime.now();
	}
}
