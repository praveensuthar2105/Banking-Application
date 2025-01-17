package com.example.codewithpraveen.banking_management_system.payLoad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class ApiResponse {
	private  String message;
	private  Boolean success;
	

	
	public ApiResponse(String message , Boolean success) {
		this.message = message;
		this.success = success;
	}
	
	
}
