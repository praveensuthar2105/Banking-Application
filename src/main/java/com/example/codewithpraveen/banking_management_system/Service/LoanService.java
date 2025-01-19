package com.example.codewithpraveen.banking_management_system.Service;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.LoanDto;

import java.util.List;

public interface LoanService {

	LoanDto applyLoan(LoanDto loanDto , String email);
	LoanDto  getLoanById(long loanId);
	LoanDto  payEmi(long loanId);
	List<LoanDto>  getLoanByUserId(Integer userId);
	
	
//	LoanDto  getLoanByLoanId(long loanId);


	

}
