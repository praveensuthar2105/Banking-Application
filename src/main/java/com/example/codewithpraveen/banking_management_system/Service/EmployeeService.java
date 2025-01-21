package com.example.codewithpraveen.banking_management_system.Service;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.EmployeeDto;

import java.util.List;

public interface EmployeeService {
	
	 EmployeeDto addEmployee(EmployeeDto employeeDto);
	
	EmployeeDto updateEmployee(EmployeeDto employeeDto, long employeeId);
	
	EmployeeDto assignEmployeeToBranch(long employeeId, String branchCode, long RoleAssignerId);
	
	EmployeeDto  assignRole(long employeeId, String roleName, long RoleAssignerId);
	
	 EmployeeDto getEmployeeById(long employeeId);
	 EmployeeDto getEmployeeByEmail(String email);
	 void deleteEmployee(long employeeId);
	 List<EmployeeDto> getEmployeeByBranch(String branchCode);
	 List<EmployeeDto> getEmployeeByRole(Integer roleId);
	
	 
}
