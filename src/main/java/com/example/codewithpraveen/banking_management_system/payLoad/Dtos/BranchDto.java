package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.BranchType;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {
	
	private String branchCode;
	
	@Email(message = "Email should be valid")
	private String branchEmail;
	
	@NotEmpty
	private String branchName;
	
	private String branchPhoneNumber;
	
	private BranchType branchType;
	
	private String branchAddress;
	
	private String branchCity;
	
	private String branchZipCode;
	
	private String branchState;
	
	private Status branchStatus;
	
	private UserDto user;
}
