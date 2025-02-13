package com.example.codewithpraveen.banking_management_system.Security;

import com.example.codewithpraveen.banking_management_system.Entites.Employee;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.EmployeeRepo;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {
	
	private final UserRepo userRepo;
	private final EmployeeRepo employeeRepo;
	public UserServices(UserRepo userRepo , EmployeeRepo employeeRepo) {
		this.userRepo = userRepo;
		this.employeeRepo = employeeRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(username).orElse(null);
		if(user != null) {
			return new CustomUserDetail(user);
		}
		Employee employee = employeeRepo.findEmployeeByEmployeeEmail(username);
		if(employee != null) {
			return new CustomEmployeeDetail(employee);
		}
		
		
		
		throw new UsernameNotFoundException("User not found" + username);
	}
}
