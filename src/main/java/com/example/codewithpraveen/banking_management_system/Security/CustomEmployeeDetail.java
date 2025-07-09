package com.example.codewithpraveen.banking_management_system.Security;

import com.example.codewithpraveen.banking_management_system.Entites.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomEmployeeDetail implements UserDetails {
	
	
	private Employee employee;
	
	public CustomEmployeeDetail(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = employee.getRoles().stream().map(role -> new SimpleGrantedAuthority( role.getRoleName())).toList();
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return  employee.getPassword();
	}
	
	
	@Override
	public String getUsername() {
		return employee.getEmployeeEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
