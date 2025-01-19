package com.example.codewithpraveen.banking_management_system.Repository;

import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepo extends JpaRepository<Branch, String> {
	Optional<Branch> findByBranchCode(String branchCode);
}
