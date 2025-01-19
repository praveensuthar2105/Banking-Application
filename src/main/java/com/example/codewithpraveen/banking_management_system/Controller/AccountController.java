package com.example.codewithpraveen.banking_management_system.Controller;


import com.example.codewithpraveen.banking_management_system.Service.AccountService;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.AccountDto;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.MoneyTrasferReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	// create account
	@PostMapping(value = "/create/{branchCode}/{username}" , produces = "application/json")
	public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto , @PathVariable String username , @PathVariable String branchCode) {
	  AccountDto createdAccount = this.accountService.createAccount(accountDto, username , branchCode);
	  return new ResponseEntity<>(createdAccount , HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/{accountNumber}/{branchCode}" , produces = "application/json")
	public  ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto accountDto , @PathVariable long accountNumber , @PathVariable String branchCode) {
		AccountDto updatedAccount = accountService.updateAccount(accountDto , accountNumber , branchCode);
		return new ResponseEntity<>(updatedAccount , HttpStatus.OK);
	}
	
	@GetMapping(value = "/{accountNumber}" ,  produces = "application/json")
	public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable long accountNumber) {
	
		AccountDto accountDto = this.accountService.getAccountByAccountNumber(accountNumber);
		return new ResponseEntity<>(accountDto , HttpStatus.OK);
		
	}
	@DeleteMapping(value =  "/{accountNumber}" , produces = "application/json")
	public  ResponseEntity<ApiResponse> deleteAccount(@PathVariable long accountNumber) {
		
		this.accountService.deleteAccount(accountNumber);
		return new ResponseEntity<>(new ApiResponse("Account deleted Successfully" , true) , HttpStatus.OK);
	}
	
	@GetMapping(value = "/deposit/{accountNumber}/{amount}")
	public ResponseEntity<AccountDto> deposit(@PathVariable long accountNumber , @PathVariable double amount) {
		AccountDto accountDto = this.accountService.deposit(accountNumber , amount);
		return  new ResponseEntity<>(accountDto , HttpStatus.OK);
	}
	
	@GetMapping(value = "/withdraw/{accountNumber}/{amount}")
	public ResponseEntity<ApiResponse> withdraw(@PathVariable long accountNumber , @PathVariable double amount) {
		this.accountService.withdraw(accountNumber , amount);
		return  new ResponseEntity<>(new ApiResponse("Withdraw Successful" , true) , HttpStatus.OK);
	}
	
	@PostMapping(value = "/transfer" , produces = "application/json")
	public ResponseEntity<ApiResponse> transfer(@RequestBody MoneyTrasferReq req ) {
		this.accountService.transfer(req);
		return new ResponseEntity<>(new ApiResponse("Transfer Success" , true) , HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/{username}" , produces = "application/json")
	public ResponseEntity<List<AccountDto>> getAccountByUser(@PathVariable String username) {
		List<AccountDto> accountDto = this.accountService.getAccountByUser(username);
		return new ResponseEntity<List<AccountDto>>(accountDto , HttpStatus.OK);
	}
	
	@GetMapping(value = "/branch/{branch}" , produces = "application/json")
	public ResponseEntity<List<AccountDto>> getAccountByBranch(@PathVariable String branch) {
		List<AccountDto> accountDto = this.accountService.getAccountByBranch(branch);
		return new ResponseEntity<>(accountDto , HttpStatus.OK);
	}
	@GetMapping(value = "/status/{status}" , produces = "application/json")
	public ResponseEntity<List<AccountDto>> getAccountByStatus(@PathVariable String status) {
		List<AccountDto> accountDto = this.accountService.getAccountByStatus(status);
		return new ResponseEntity<List<AccountDto>>(accountDto , HttpStatus.OK);
	}
	
	@GetMapping(value = "/accountType/{accountType}" , produces = "application/json")
	public ResponseEntity<List<AccountDto>> getAccountByAccountType(@PathVariable String accountType) {
		List<AccountDto> accountDto = this.accountService.getAccountByAccountType(accountType);
		return new ResponseEntity<List<AccountDto>>(accountDto , HttpStatus.OK);
	}
	
}
