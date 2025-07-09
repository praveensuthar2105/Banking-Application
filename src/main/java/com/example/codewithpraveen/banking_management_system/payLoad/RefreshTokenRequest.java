package com.example.codewithpraveen.banking_management_system.payLoad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
	
	@NotBlank(message = "Refresh token is required")
	private String refreshToken;
}
