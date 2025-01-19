package com.example.codewithpraveen.banking_management_system.Service;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.AccountDto;
import com.example.codewithpraveen.banking_management_system.payLoad.MoneyTrasferReq;

import java.util.List;

public interface AccountService {

  AccountDto createAccount(AccountDto accountDto , String username , String branchCode);
  AccountDto getAccountByAccountNumber(long accountNumber);
  AccountDto updateAccount(AccountDto accountDto , long accountNumber , String branchCode);
  void deleteAccount(long accountNumber);
  AccountDto deposit(long accountNumber , double amount);
  void withdraw(long accountNumber , double amount);
  void transfer(MoneyTrasferReq moneyTrasferReq);
  List<AccountDto> getAccountByUser(String username);
  List<AccountDto> getAccountByBranch(String branchCode);
  List<AccountDto> getAccountByStatus(String status);
  List<AccountDto> getAccountByAccountType(String accountType);
//  AccountDto getAccountByInterestRate(String interestRate);

}
