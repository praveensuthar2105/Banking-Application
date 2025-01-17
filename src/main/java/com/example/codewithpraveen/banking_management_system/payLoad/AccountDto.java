package com.example.codewithpraveen.banking_management_system.payLoad;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
//@JsonIgnoreProperties({"user"})
public class AccountDto {
	private long accountNumber;
	
	private AccountType accountType;
	
	private double balance;
	
	
//	private User user;
	
	private String status;
	
	private double interestRate;
	
	private String branch;
	
	private UserDto user;
	
}
