package com.example.codewithpraveen.banking_management_system.Service.ServiceImpl;

import com.example.codewithpraveen.banking_management_system.Entites.Loan;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.LoanRepo;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import com.example.codewithpraveen.banking_management_system.Service.LoanService;
import com.example.codewithpraveen.banking_management_system.config.AppConstant;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.LoanDto;
import com.example.codewithpraveen.banking_management_system.payLoad.GenerateNumber;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepo loanRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	

	@Override
	public LoanDto applyLoan(LoanDto loanDto, String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User " , "email" , email));
		Loan loan = modelMapper.map(loanDto, Loan.class);
	
		loan.setLoanId(GenerateNumber.generateUniqueNumber(10));
		
		Map<String, Double> interestRates = Map.of(
				"HOME_LOAN", AppConstant.HOME_LOAN,
				"CAR_LOAN", AppConstant.CAR_LOAN,
				"PERSONAL_LOAN", AppConstant.PERSONAL_LOAN,
				"EDUCATION_LOAN", AppConstant.EDUCATION_LOAN,
				"GOLD_LOAN", AppConstant.GOLD_LOAN,
				"OTHERS", AppConstant.OTHERS
		);
		loan.setInterestRate(interestRates.getOrDefault(loan.getLoanType(), AppConstant.OTHERS));
		loan.setUser(user);
		loan.setLoanStatus("ACTIVE");
		double interestAmount = loan.getLoanAmount() * loan.getInterestRate() / 100;
		loan.setEmiAmount( loan.getLoanAmount() / loan.getMonth() + interestAmount);
		loan.setInterestAmount(interestAmount);
		loan.setRemainingAmount(loan.getLoanAmount());
		loan.setNoOfEmi(loan.getMonth());
		Loan savedLoan = loanRepo.save(loan);
		
		return modelMapper.map(savedLoan, LoanDto.class);
		
	}
	
	@Override
	public LoanDto getLoanById(long loanId) {
		Loan loan = this.loanRepo.findById(loanId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "loanId", loanId));
		return modelMapper.map(loan, LoanDto.class);
	}
	
	@Override
	public LoanDto payEmi(long loanId) {
	Loan loan = this.loanRepo.findById(loanId)
			.orElseThrow(() -> new ResourceNotFoundException("Loan", "loanId", loanId));
	loan.setRemainingAmount(loan.getRemainingAmount() - loan.getEmiAmount());
	loan.setNoOfEmi(loan.getNoOfEmi() - 1);
	if (loan.getNoOfEmi() == 0) {
		loan.setLoanStatus("CLOSED");
	}
	Loan savedLoan = this.loanRepo.save(loan);
	return modelMapper.map(savedLoan, LoanDto.class);
	}
	
	@Override
	public List<LoanDto> getLoanByUserId(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		List<Loan> loans = this.loanRepo.findByUser(user);
			if (loans.isEmpty()) {
				throw new ResourceNotFoundException("Loan", "userId", userId);
			}
			
			return  loans.stream()
					.map(loan -> modelMapper.map(loan , LoanDto.class)).toList();
			
	}
}
