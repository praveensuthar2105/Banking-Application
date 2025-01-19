package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Service.LoanService;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.LoanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {
	
	@Autowired
	private LoanService loanService;
	
	// @PostMapping("/apply")
	@PostMapping(value = "/apply/{email}" , produces = "application/json")
	public ResponseEntity<LoanDto> applyLoan(@RequestBody LoanDto loanDto , @PathVariable String email) {
		LoanDto loan = this.loanService.applyLoan(loanDto, email);
		return  new ResponseEntity<>(loan , HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{loanId}")
	public ResponseEntity<LoanDto> getLoanById(@PathVariable long loanId) {
		LoanDto loan = this.loanService.getLoanById(loanId);
		return new ResponseEntity<>(loan , HttpStatus.OK);
	}
	
	@GetMapping("/payEmi/{loanId}")
	public ResponseEntity<LoanDto> payEmi(@PathVariable long loanId) {
		LoanDto loan = this.loanService.payEmi(loanId);
		return new ResponseEntity<>(loan , HttpStatus.OK);
	}
	
	@GetMapping("/getByUserId/{userId}")
	public ResponseEntity<List<LoanDto>> getLoanByUserId(@PathVariable Integer userId) {
		List<LoanDto> loan = this.loanService.getLoanByUserId(userId);
		return new ResponseEntity<>(loan , HttpStatus.OK);
	}
}
