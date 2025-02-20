package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Data
@Setter
@Getter
//@JsonIgnoreProperties({"user"})
public class AccountDto {
	private long accountNumber;
	
	@NotEmpty
	private AccountType accountType;
	
	@NotEmpty
	private double balance;
	
	
//	private User user;
	
	@NotEmpty
	private String status;
	
	@NotEmpty
	private double interestRate;
	
//	@NotEmpty
//	private String branch;
	
	
	private UserDto user;
	
	private BranchDto branch;
	
}
