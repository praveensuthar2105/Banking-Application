package com.example.codewithpraveen.banking_management_system.payLoad.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Data
//@JsonIgnoreProperties({"accounts"})
public class UserDto {
   
    private  int id;
   
    @NotEmpty
    @Size(min = 4 , message = "Name should have atleast 4 characters")
    private String name;
    
    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    @Size(min = 4 , max = 8 , message = "Password should be between 4 to 8 characters")
    private String password;

    @NotEmpty
    private String address;
    
    private Set<RoleDto> roles = new HashSet<>();
    
//    private List<AccountDto> accounts;



}
