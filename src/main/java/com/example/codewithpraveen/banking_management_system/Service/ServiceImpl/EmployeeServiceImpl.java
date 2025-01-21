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
import org.springframework.stereotype.Service;

import java.util.List;

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
	
	@Override
	public EmployeeDto addEmployee(EmployeeDto employeeDto) {
		
		Employee employee = this.modelMapper.map(employeeDto , Employee.class);
		employee.setEmployeeId(GenerateNumber.generateUniqueNumber(AppConstant.EMPLOYEE_Id_Digit));
		employee.setRole(null);
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
		if(roleAssigner.getRole().getRoleName().equals("HR") || roleAssigner.getRole().getRoleName().equals("RegionManager")) {
			Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
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
		if(roleAssigner.getRole().getRoleName().equals("HR") || roleAssigner.getRole().getRoleName().equals("RegionManager") || roleAssigner.getRole().getRoleName().equals("BranchManager")) {
			employee.setRole(role);
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
		List<Employee> employees = this.employeeRepo.findByRole(role);
		return employees.stream().map(employee -> this.modelMapper.map(employee , EmployeeDto.class)).toList();
	}
	
	
}
