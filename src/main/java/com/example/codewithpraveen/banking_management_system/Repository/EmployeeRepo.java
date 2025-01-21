package com.example.codewithpraveen.banking_management_system.Repository;

import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import com.example.codewithpraveen.banking_management_system.Entites.Employee;
import com.example.codewithpraveen.banking_management_system.Entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
	Employee findEmployeeByEmployeeEmail(String email);
	List<Employee> findByBranch(Branch branch);
	
	List<Employee> findByRole(Role role);

}
