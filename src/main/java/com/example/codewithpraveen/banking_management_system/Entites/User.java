package com.example.codewithpraveen.banking_management_system.Entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    private String name;

    private String email;

    private String phoneNumber;

    private String password;

    private String address;
    
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();



}
