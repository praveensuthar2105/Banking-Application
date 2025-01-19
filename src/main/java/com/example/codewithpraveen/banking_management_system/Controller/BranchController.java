package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Service.BranchService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.BranchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch")
public class BranchController {
	
	@Autowired
	private BranchService branchService;
	
	@PostMapping("/create")
	public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branchDto){
		BranchDto branch = this.branchService.createBranch(branchDto);
		return new ResponseEntity<>(branch, HttpStatus.CREATED);
	}
	
	@GetMapping("/close/{branchCode}")
	public  ResponseEntity<ApiResponse> closeBranch(@PathVariable String branchCode) {
		this.branchService.closebranch(branchCode);
		return new ResponseEntity<>(new ApiResponse("Branch Closed Successfully" , true), HttpStatus.OK);
		
	}
	
	@GetMapping("/")
	public ResponseEntity<List<BranchDto>> getAllBranches() {
		List<BranchDto> branches = this.branchService.getAllBranches();
		return new ResponseEntity<>(branches, HttpStatus.OK);
	}
	
	@GetMapping("/{branchCode}")
	public ResponseEntity<BranchDto> getBranchByCode(@PathVariable String branchCode) {
		BranchDto branch = this.branchService.getBranchByCode(branchCode);
		return new ResponseEntity<>(branch, HttpStatus.OK);
	}
	
	@PutMapping("/{branchCode}")
	public ResponseEntity<BranchDto> updateBranch(@PathVariable String branchCode, @RequestBody BranchDto branchDto) {
		BranchDto branch = this.branchService.updateBranch(branchCode, branchDto);
		return new ResponseEntity<>(branch, HttpStatus.OK);
	}
	
}
