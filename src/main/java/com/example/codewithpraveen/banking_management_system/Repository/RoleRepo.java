package com.example.codewithpraveen.banking_management_system.Repository;


import com.example.codewithpraveen.banking_management_system.Entites.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
	
	Role findByRoleId(Integer roleId);
	
	Role findByRoleName(String roleName);
}
