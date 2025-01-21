package com.example.codewithpraveen.banking_management_system.Service.ServiceImpl;

import com.example.codewithpraveen.banking_management_system.Entites.Branch;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.BranchRepo;
import com.example.codewithpraveen.banking_management_system.Service.BranchService;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.BranchDto;
import com.example.codewithpraveen.banking_management_system.payLoad.allEnum.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchRepo branchRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public BranchDto createBranch(BranchDto branchDto) {
		Branch branch = this.modelMapper.map(branchDto, Branch.class);
		branch.setBranchCode("SBI"+branch.getBranchCity()+branch.getBranchZipCode());
		branch.setBranchName(branch.getBranchCity()+branch.getBranchZipCode());
		branch.setBranchStatus(Status.ACTIVE);
		Branch savedBranch = this.branchRepo.save(branch);
		return this.modelMapper.map(savedBranch , BranchDto.class);
	}
	
	@Override
	public Void closebranch(String branchCode) {
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		branch.setBranchStatus(Status.INACTIVE);
		this.branchRepo.save(branch);
		return null;
	}
	
	@Override
	public List<BranchDto> getAllBranches() {
		List<Branch> branches = this.branchRepo.findAll();
		return branches.stream().map(branch -> this.modelMapper.map(branch, BranchDto.class)).toList();
	}
	
	@Override
	public BranchDto getBranchByCode(String branchCode) {
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(()-> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		return this.modelMapper.map(branch, BranchDto.class);
	}
	
	@Override
	public BranchDto updateBranch(String branchCode, BranchDto branchDto) {
		Branch branch = this.branchRepo.findByBranchCode(branchCode).orElseThrow(() -> new ResourceNotFoundException("Branch" , "BranchCode" , branchCode));
		branch.setBranchAddress(branchDto.getBranchAddress());
		branch.setBranchEmail(branchDto.getBranchEmail());
		branch.setBranchPhoneNumber(branchDto.getBranchPhoneNumber());
		branch.setBranchType(branchDto.getBranchType());
		branch.setBranchStatus(branchDto.getBranchStatus());
		Branch saveedBranch = branchRepo.save(branch);
		return this.modelMapper.map(saveedBranch , BranchDto.class);
	}
}
