package com.example.codewithpraveen.banking_management_system.Repository;

import com.example.codewithpraveen.banking_management_system.Entites.Loan;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.LoanDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loan, Long> {
	List<Loan> findByUser(User user);
}
