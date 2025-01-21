package com.example.codewithpraveen.banking_management_system.Entites;


import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Account {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long accountNumber;
	
	private AccountType accountType;
	
	private double balance;
	
	@ManyToOne
	private User user;
	
	private String status;
	
	private double interestRate;
	
//	private String branch;
	
	@ManyToOne
	private  Branch branch;
}
