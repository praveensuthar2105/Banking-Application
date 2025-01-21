package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Service.EmployeeService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping(value = "/addEmployee" , produces = "application/json")
	public ResponseEntity<EmployeeDto>  addEmployee(@RequestBody EmployeeDto employeeDto) {
		EmployeeDto savedEmployeeDto = this.employeeService.addEmployee(employeeDto);
		return  new ResponseEntity<>(savedEmployeeDto , HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/updateEmployee/{employeeId}" , produces = "application/json")
	public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable long employeeId) {
		EmployeeDto updatedEmployeeDto = this.employeeService.updateEmployee(employeeDto, employeeId);
		return new ResponseEntity<>(updatedEmployeeDto , HttpStatus.OK);
	}
	
	@PutMapping(value = "/assignEmployeeToBranch/{employeeId}/{branchCode}/{RoleAssignerId}" , produces = "application/json")
	public  ResponseEntity<EmployeeDto> assignEmployeeToBranch(@PathVariable long employeeId, @PathVariable String branchCode , @PathVariable long RoleAssignerId) {
		EmployeeDto employeeDto = this.employeeService.assignEmployeeToBranch(employeeId, branchCode , RoleAssignerId);
		return new ResponseEntity<>(employeeDto , HttpStatus.OK);
	}
	
	@PutMapping(value = "/assignRole/{employeeId}/{roleName}/{RoleAssignerId}" , produces = "application/json")
	public ResponseEntity<EmployeeDto> assignRole(@PathVariable long employeeId, @PathVariable String roleName, @PathVariable long RoleAssignerId) {
		EmployeeDto employeeDto = this.employeeService.assignRole(employeeId, roleName, RoleAssignerId);
		return new ResponseEntity<>(employeeDto , HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEmployeeById/{employeeId}" , produces = "application/json")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable long employeeId) {
		EmployeeDto employeeDto = this.employeeService.getEmployeeById(employeeId);
		return new ResponseEntity<>(employeeDto , HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEmployeeByEmail/{email}" , produces = "application/json")
	public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable String email) {
		EmployeeDto employeeDto = this.employeeService.getEmployeeByEmail(email);
		return new ResponseEntity<>(employeeDto , HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteEmployee/{employeeId}" , produces = "application/json")
	public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable long employeeId) {
		this.employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<>(new ApiResponse("Employee Deleted Successfully" , true) , HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEmployeeByBranch/{branchCode}" , produces = "application/json")
	public ResponseEntity<List<EmployeeDto>> getEmployeeByBranch(@PathVariable String branchCode) {
		List<EmployeeDto> employeeDtos = this.employeeService.getEmployeeByBranch(branchCode);
		return new ResponseEntity<>(employeeDtos , HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEmployeeByRole/{roleId}" , produces = "application/json")
	public ResponseEntity<List<EmployeeDto>> getEmployeeByRole(@PathVariable Integer roleId) {
		List<EmployeeDto> employeeDtos = this.employeeService.getEmployeeByRole(roleId);
		return new ResponseEntity<>(employeeDtos , HttpStatus.OK);
	}
	
	
}
