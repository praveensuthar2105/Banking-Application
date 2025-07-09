package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Entites.Employee;
import com.example.codewithpraveen.banking_management_system.Repository.EmployeeRepo;
import com.example.codewithpraveen.banking_management_system.Service.EmployeeService;
import com.example.codewithpraveen.banking_management_system.Service.JwtService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.AuthRequest;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.EmployeeDto;
import com.example.codewithpraveen.banking_management_system.payLoad.EmployeeResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.RefreshTokenRequest;
import com.example.codewithpraveen.banking_management_system.payLoad.LogoutRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ModelMapper modelMapper;
	
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
	
	@PostMapping(value = "/login" , produces = "application/json")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
			);
			
			Employee employee = employeeRepo.findEmployeeByEmployeeEmail(authRequest.getUsername());
			if (employee == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse("Employee not found", false));
			}
			
			// Generate both access and refresh tokens
			String accessToken = jwtService.generateAccessToken(authRequest.getUsername(), employee.getRoles().toString());
			String refreshToken = jwtService.generateRefreshToken(authRequest.getUsername());
			
			EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
			
			// Calculate expiration time in seconds
			long expiresIn = jwtService.getExpirationTime(accessToken) - System.currentTimeMillis();
			expiresIn = expiresIn / 1000; // Convert to seconds
			
			// Create enhanced response with both tokens
			EmployeeResponse employeeResponse = new EmployeeResponse(employeeDto, accessToken, refreshToken, expiresIn);
			return ResponseEntity.ok(employeeResponse);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponse("Invalid credentials", false));
		}
	}
	
	@PostMapping(value = "/refresh-token", produces = "application/json")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		try {
			String refreshToken = refreshTokenRequest.getRefreshToken();
			String username = jwtService.getUsernameFromToken(refreshToken);
			
			if (jwtService.validateRefreshToken(refreshToken, username)) {
				Employee employee = employeeRepo.findEmployeeByEmployeeEmail(username);
				if (employee == null) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ApiResponse("Employee not found", false));
				}
				
				// Generate new access token
				String newAccessToken = jwtService.generateAccessToken(username, employee.getRoles().toString());
				
				EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
				long expiresIn = jwtService.getExpirationTime(newAccessToken) - System.currentTimeMillis();
				expiresIn = expiresIn / 1000;
				
				EmployeeResponse employeeResponse = new EmployeeResponse(employeeDto, newAccessToken, refreshToken, expiresIn);
				return ResponseEntity.ok(employeeResponse);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse("Invalid refresh token", false));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponse("Token refresh failed", false));
		}
	}
	
	@PostMapping(value = "/logout", produces = "application/json")
	public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
		try {
			// Revoke both access and refresh tokens
			jwtService.revokeToken(logoutRequest.getAccessToken());
			if (logoutRequest.getRefreshToken() != null) {
				jwtService.revokeToken(logoutRequest.getRefreshToken());
			}
			
			return ResponseEntity.ok(new ApiResponse("Employee logged out successfully", true));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse("Logout failed", false));
		}
	}
	
	// Temporary test endpoint - completely bypass security for testing
	@PostMapping(value = "/test-add" , produces = "application/json")
	public ResponseEntity<String> testAddEmployee(@RequestBody EmployeeDto employeeDto) {
		try {
			EmployeeDto savedEmployeeDto = this.employeeService.addEmployee(employeeDto);
			return ResponseEntity.ok("Employee created successfully: " + savedEmployeeDto.getEmployeeName());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}
}
