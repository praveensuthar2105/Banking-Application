package com.example.codewithpraveen.banking_management_system.Entites;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	
	private String roleName;
	
//	@OneToMany(mappedBy = "role" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
//	private List<Employee> employees = new ArrayList<>();
//
//	@ManyToMany(cascade = CascadeType.ALL , fetch  = FetchType.LAZY )
//	private  List<User> users = new ArrayList<>();
//
//	@ManyToMany(mappedBy = "roles")
//	private Collection<User> users;


}
