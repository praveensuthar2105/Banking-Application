package com.example.codewithpraveen.banking_management_system.Entites;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.RoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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
	
	@ManyToOne
	private Role role;
}
