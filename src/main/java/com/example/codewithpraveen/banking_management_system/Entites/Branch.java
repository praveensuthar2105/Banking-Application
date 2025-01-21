package com.example.codewithpraveen.banking_management_system.Entites;

import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.BranchType;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Data
public class Branch {
	@Id
	private String branchCode;
	
	
	private String branchName;
	
	private BranchType branchType;
	
	private String branchAddress;
	
	private String branchCity;
	
	private String branchState;
	
	private String branchZipCode;
	
	private String branchPhoneNumber;
	
	private String branchEmail;
	
	private Status branchStatus;
	

	@OneToMany(mappedBy = "branch" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
	private List<Account> accounts = new ArrayList<>();
	
	@OneToMany(mappedBy = "branch" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
	private  List<Employee> employees = new ArrayList<>();
	
}
