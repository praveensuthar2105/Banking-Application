package com.example.codewithpraveen.banking_management_system.Service;

import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.BranchDto;

import java.util.List;

public interface BranchService {
	
	BranchDto createBranch(BranchDto branchDto);
	Void closebranch(String branchCode);
	List<BranchDto> getAllBranches();
	BranchDto getBranchByCode(String branchCode);
	BranchDto updateBranch(String branchCode, BranchDto branchDto);
	
}
