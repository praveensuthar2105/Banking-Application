package com.example.codewithpraveen.banking_management_system.Repository;

import com.example.codewithpraveen.banking_management_system.Entites.Account;
import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.payLoad.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Integer> {
	
	
	   Optional<Account> findByAccountNumber(long accountNumber);
	
//	Optional<Object> findByUserEmail(String username);
       List<Account> findByUser(User user);
	
	   List<Account> findByBranch(Branch branch);
	
    	List<Account> findByStatus(String status);
	
	List<Account> findByAccountType(AccountType accountType);
}
