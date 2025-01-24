package com.example.codewithpraveen.banking_management_system.payLoad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	private  String username;
	private String password;
}
