package com.example.codewithpraveen.banking_management_system.Service;

import com.example.codewithpraveen.banking_management_system.payLoad.AccountDto;
import com.example.codewithpraveen.banking_management_system.payLoad.MoneyTrasferReq;

import java.util.List;

public interface AccountService {

  AccountDto createAccount(AccountDto accountDto , String username);
  AccountDto getAccountByAccountNumber(long accountNumber);
  AccountDto updateAccount(AccountDto accountDto , long accountNumber);
  void deleteAccount(long accountNumber);
  AccountDto deposit(long accountNumber , double amount);
  void withdraw(long accountNumber , double amount);
  void transfer(MoneyTrasferReq moneyTrasferReq);
  List<AccountDto> getAccountByUser(String username);
  List<AccountDto> getAccountByBranch(String branch);
  List<AccountDto> getAccountByStatus(String status);
  List<AccountDto> getAccountByAccountType(String accountType);
//  AccountDto getAccountByInterestRate(String interestRate);

}
