package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.BranchType;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.Status;
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
	
	
	private String branchName;
	
	private BranchType branchType;
	
	private String branchAddress;
	
	private String branchCity;
	
	private String branchState;
	
	private String branchZipCode;
	
	private String branchPhoneNumber;
	
	private String branchEmail;
	
	private Status branchStatus;
	
	private UserDto user;
}
