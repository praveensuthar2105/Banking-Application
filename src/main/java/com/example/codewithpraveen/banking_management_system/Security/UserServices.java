package com.example.codewithpraveen.banking_management_system.Security;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServices implements UserDetailsService {
	
	private final UserRepo userRepo;
	
	public UserServices(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
		UserDetails customUserDetail = new CustomUserDetail(user);
		
		return customUserDetail;
	}
}
