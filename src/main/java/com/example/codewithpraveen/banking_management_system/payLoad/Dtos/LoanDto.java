package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.payLoad.LoanType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;



@Data
@Getter
@Setter
public class LoanDto {
	
	@NotEmpty
	private long loanId;
	
	private double loanAmount;
	
	private int month;
	
	private LoanType loanType;
	
	private String loanStatus;
	

	private UserDto user;
	
	private  double remainingAmount;
	
	private  int NoOfEmi;
	
	private double emiAmount;
	
	private double interestAmount;
	
	private double interestRate;
}
