package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.Entites.Role;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.RoleEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EmployeeDto {
	
	private long employeeId;
	
	private String employeeName;
	
	private String employeeEmail;
	
	private String employeePhoneNumber;
	
	private String employeeZipCode;
	
//	private RoleEnum employeeRole;
	private RoleDto role;
	
	private BranchDto branch;
	
}
