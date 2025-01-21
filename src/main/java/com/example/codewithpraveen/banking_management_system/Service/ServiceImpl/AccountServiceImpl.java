package com.example.codewithpraveen.banking_management_system.Service.ServiceImpl;

import com.example.codewithpraveen.banking_management_system.Entites.Account;
import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.AccountRepo;
import com.example.codewithpraveen.banking_management_system.Repository.BranchRepo;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import com.example.codewithpraveen.banking_management_system.Service.AccountService;
import com.example.codewithpraveen.banking_management_system.config.AppConstant;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.AccountDto;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.AccountType;
import com.example.codewithpraveen.banking_management_system.payLoad.GenerateNumber;
import com.example.codewithpraveen.banking_management_system.payLoad.MoneyTrasferReq;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl  implements AccountService  {
	
	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private BranchRepo branchRepo;
	
	
	
	@Override
	public AccountDto createAccount(AccountDto accountDto, String username , String branchCode) {
		
		User user = this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User" , "Email" , username));
		Account account = this.modelMapper.map(accountDto, Account.class);
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		
		
		account.setUser(user);
		account.setAccountNumber(GenerateNumber.generateUniqueNumber(AppConstant.ACCOUNT_NUMBER_DIGIT));
		account.setBranch(branch);
		account.setStatus("Active");
		account.setBalance(0.0);
		if (Objects.requireNonNull(account.getAccountType()) == AccountType.CURRENT) {
			account.setInterestRate(AppConstant.CURRENT_ACCOUNT_INTEREST_RATE);
		} else {
			account.setInterestRate(AppConstant.SAVING_ACCOUNT_INTEREST_RATE);
		}
		Account savedAccount = this.accountRepo.save(account);
		
		return this.modelMapper.map(savedAccount , AccountDto.class);
		
	}
	
	@Override
	public AccountDto getAccountByAccountNumber(long accountNumber) {
		Account account = this.accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , accountNumber));
		return  this.modelMapper.map(account , AccountDto.class);
	}
	
	@Override
	public AccountDto updateAccount(AccountDto accountDto, long accountNumber , String branchCode) {
		Account account = this.accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , accountNumber));
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		account.setBranch(branch);
		account.setAccountType(accountDto.getAccountType());
		
		account.setStatus(accountDto.getStatus());
     	account.setBalance(accountDto.getBalance());
//		account.setInterestRate(accountDto.getInterestRate());
		Account savedAccount = this.accountRepo.save(account);
		return this.modelMapper.map(savedAccount , AccountDto.class);
	}
	
	@Override
	public void deleteAccount(long accountNumber) {
		Account account = this.accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , accountNumber));
		this.accountRepo.delete(account);
	}
	
	@Override
	public AccountDto deposit(long accountNumber, double amount) {
		Account account = this.accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , accountNumber));
		account.setBalance(account.getBalance() + amount);
		Account savedAccount = this.accountRepo.save(account);
		return this.modelMapper.map(savedAccount , AccountDto.class);
	}
	
	@Override
	public void withdraw(long accountNumber, double amount) {
		Account account = this.accountRepo.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , accountNumber));
			account.setBalance(account.getBalance() - amount);
			 this.accountRepo.save(account);
		
	
	
	}
	
	@Override
	public void transfer(MoneyTrasferReq moneyTrasferReq) {
		Account account1 = this.accountRepo.findByAccountNumber(moneyTrasferReq.getSenderAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , moneyTrasferReq.getSenderAccountNumber()));
		Account account2 = this.accountRepo.findByAccountNumber(moneyTrasferReq.getReceiverAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("Account" , "Account Number" , moneyTrasferReq.getReceiverAccountNumber()));
		account1.setBalance(account1.getBalance() - moneyTrasferReq.getAmount());
		account2.setBalance(account2.getBalance() + moneyTrasferReq.getAmount());
		this.accountRepo.save(account1);
		this.accountRepo.save(account2);
//		return List.of(this.modelMapper.map(savedAccount1 , AccountDto.class) , this.modelMapper.map(savedAccount2 , AccountDto.class));
		
	}
	
	@Override
	public List<AccountDto> getAccountByUser(String username) {
		User user = this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User" , "Email" , username));
		List<Account> accounts = this.accountRepo.findByUser(user);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("Account", "User", username);
		}
		return  accounts.stream()
				.map(account -> this.modelMapper.map(account, AccountDto.class))
				.toList();
	}
	
	@Override
	public List<AccountDto> getAccountByBranch(String branchCode) {
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		List<Account>  accounts = this.accountRepo.findByBranch(branch);
		if(accounts.isEmpty()) {
			throw  new ResourceNotFoundException("Account" , "Branch" , branchCode);
		}
		
		return accounts.stream().map(account -> this.modelMapper.map(accounts , AccountDto.class))
				.toList();
	}
	
	@Override
	public List<AccountDto> getAccountByStatus(String status) {
		List<Account> accounts = this.accountRepo.findByStatus(status);
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("Account", "Status", status);
		}
		return accounts.stream()
				.map(account -> this.modelMapper.map(account, AccountDto.class))
				.toList();
	}
	
	@Override
	public List<AccountDto> getAccountByAccountType(String accountType) {
		List<Account> accounts = this.accountRepo.findByAccountType(AccountType.valueOf(accountType));
		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("Account", "Account Type", accountType);
		}
		return accounts.stream()
				.map(account -> this.modelMapper.map(account, AccountDto.class))
				.toList();
	}
}
