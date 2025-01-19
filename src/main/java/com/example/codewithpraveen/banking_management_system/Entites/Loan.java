package com.example.codewithpraveen.banking_management_system.Entites;

import com.example.codewithpraveen.banking_management_system.payLoad.LoanType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Month;

@Entity
@Table(name = "loan")
@Getter
@Setter
@Data
public class Loan {
	
	@Id
	private long loanId;
	
	private double loanAmount;
	
    private int month;
	
	private LoanType loanType;
	
	private String loanStatus;
	
	@ManyToOne
	private  User user;
	
	private  double remainingAmount;

	private  int NoOfEmi;
	
	private double emiAmount;
	
	private double interestAmount;
	
	private double interestRate;
}
