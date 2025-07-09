package com.example.codewithpraveen.banking_management_system.Service.ServiceImpl;

import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import com.example.codewithpraveen.banking_management_system.Entites.Employee;
import com.example.codewithpraveen.banking_management_system.Entites.Role;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.BranchRepo;
import com.example.codewithpraveen.banking_management_system.Repository.EmployeeRepo;
import com.example.codewithpraveen.banking_management_system.Repository.RoleRepo;
import com.example.codewithpraveen.banking_management_system.Service.EmployeeService;
import com.example.codewithpraveen.banking_management_system.config.AppConstant;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.EmployeeDto;
import com.example.codewithpraveen.banking_management_system.payLoad.GenerateNumber;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BranchRepo branchRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public EmployeeDto addEmployee(EmployeeDto employeeDto) {
		
		Employee employee = this.modelMapper.map(employeeDto , Employee.class);
		employee.setEmployeeId(GenerateNumber.generateUniqueNumber(AppConstant.EMPLOYEE_Id_Digit));
		
		// Fix: Check if password is not null before encoding
		if (employeeDto.getPassword() != null && !employeeDto.getPassword().isEmpty()) {
			employee.setPassword(this.passwordEncoder.encode(employeeDto.getPassword()));
		} else {
			throw new IllegalArgumentException("Employee password cannot be null or empty");
		}
		
		// Fix: Don't add null role, initialize roles set properly
		if (employee.getRoles() == null) {
			employee.setRoles(new HashSet<>());
		}
		
		// You can add a default role here if needed
		// Role defaultRole = this.roleRepo.findByRoleName("EMPLOYEE");
		// if (defaultRole != null) {
		//     employee.getRoles().add(defaultRole);
		// }
		
		Employee savedEmployee = this.employeeRepo.save(employee);
		
		return this.modelMapper.map(savedEmployee , EmployeeDto.class);
	}
	
	
	@Override
	public EmployeeDto updateEmployee(EmployeeDto employeeDto, long employeeId) {
	 Employee employee = this.employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , employeeId));
	 employee.setEmployeeName(employeeDto.getEmployeeName());
	 employee.setEmployeeEmail(employeeDto.getEmployeeEmail());
	 employee.setEmployeePhoneNumber(employeeDto.getEmployeePhoneNumber());
	 employee.setEmployeeZipCode(employeeDto.getEmployeeZipCode());
	 Employee savedEmployee = this.employeeRepo.save(employee);
	 return this.modelMapper.map(savedEmployee , EmployeeDto.class);
	 
	}
	
	
	
	@Override
	public EmployeeDto assignEmployeeToBranch(long employeeId, String branchCode, long RoleAssignerId) {
		Employee roleAssigner = this.employeeRepo.findById(RoleAssignerId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , RoleAssignerId));
		Employee employee = this.employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , employeeId));
//		if(roleAssigner.getRoles().equals("HR") || roleAssigner.getRoles().equals("RegionManager")) {
//			Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
//			employee.setBranch(branch);
//		}
		if (roleAssigner.getRoles().stream().anyMatch(r -> r.getRoleName().equals("HR") || r.getRoleName().equals("RegionManager"))) {
			Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch", "BranchCode", branchCode));
			employee.setBranch(branch);
		}
		Employee savedEmployee = this.employeeRepo.save(employee);
		return this.modelMapper.map(savedEmployee , EmployeeDto.class);
		
	}
	
	@Override
	public EmployeeDto assignRole(long employeeId, String roleName, long RoleAssignerId) {
		Employee roleAssigner = this.employeeRepo.findById(RoleAssignerId).orElseThrow(()-> new ResourceNotFoundException("Employee" , "EmployeeId" , RoleAssignerId));
		Employee employee = this.employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , employeeId));
		Role role =  this.roleRepo.findByRoleName(roleName);
		if(roleAssigner.getRoles().stream().anyMatch(r -> r.getRoleName().equals("HR") || r.getRoleName().equals("RegionManager"))) {
			employee.getRoles().add(role);
		}
//		Role savedRole = this.roleRepo.save(role);
		Employee savedEmployee = this.employeeRepo.save(employee);
		return this.modelMapper.map(savedEmployee , EmployeeDto.class);

	}
	
	
	@Override
	public EmployeeDto getEmployeeById(long employeeId) {
		Employee employee = this.employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , employeeId));
		return this.modelMapper.map(employee , EmployeeDto.class);
	}
	
	@Override
	public EmployeeDto getEmployeeByEmail(String email) {
		Employee employee = this.employeeRepo.findEmployeeByEmployeeEmail(email);
		return this.modelMapper.map(employee , EmployeeDto.class);
	}
	
	@Override
	public void deleteEmployee(long employeeId) {
	  		Employee employee = this.employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee" , "EmployeeId" , employeeId));
		this.employeeRepo.delete(employee);
	}
	
	@Override
	public List<EmployeeDto> getEmployeeByBranch(String branchCode) {
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		List<Employee> employees = this.employeeRepo.findByBranch(branch);
		return employees.stream().map(employee -> this.modelMapper.map(employee , EmployeeDto.class)).toList();
	}
	
	@Override
public List<EmployeeDto> getEmployeeByRole(Integer roleId) {
	Role role = this.roleRepo.findByRoleId(roleId);
	Set<Role> roles = Set.of(role);
	List<Employee> employees = this.employeeRepo.findByRoles(roles);
	return employees.stream().map(employee -> this.modelMapper.map(employee , EmployeeDto.class)).toList();
}
	
	
}
