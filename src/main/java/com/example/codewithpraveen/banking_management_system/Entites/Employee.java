package com.example.codewithpraveen.banking_management_system.Entites;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Employee {
	
	@Id
	private long employeeId;
	
	private String employeeName;
	
	private String employeeEmail;
	
	private String employeePhoneNumber;
	
	private String employeeZipCode;
	
	
	@ManyToOne
	private Branch branch;
	
	@ManyToMany(cascade = CascadeType.ALL , fetch  = FetchType.EAGER )
	@JoinTable(name = "employee_role" , joinColumns = @JoinColumn(name = "employee_id" , referencedColumnName = "employeeId") , inverseJoinColumns = @JoinColumn(name = "role_id" , referencedColumnName = "roleId"))
	private Set<Role> roles = new HashSet<>();
}
