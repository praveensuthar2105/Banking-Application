package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.Entites.Role;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.RoleEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
public class EmployeeDto {
	
	private long employeeId;
	
	private String employeeName;
	
	private String employeeEmail;
	
	private String employeePhoneNumber;
	
	private String employeeZipCode;
	
	private String password;
//	private RoleEnum employeeRole;
	private Set<RoleDto> roles = new HashSet<>();
	
	private BranchDto branch;
	
}
