package com.example.codewithpraveen.banking_management_system.payLoad;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private User user;
	private String token;
}
