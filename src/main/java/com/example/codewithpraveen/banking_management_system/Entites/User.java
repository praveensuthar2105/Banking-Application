package com.example.codewithpraveen.banking_management_system.Entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    private String name;

//    username for authentication
    @Column(unique = true)
    private String email;

    private String phoneNumber;

//    password for authentication
    private String password;

    private String address;
    
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch  = FetchType.LAZY)
    private  List<Loan> loans = new ArrayList<>();
    
//    @ManyToOne
//    private Branch branch;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"))
    private Set<Role> roles = new HashSet<>();
    
    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }
//
//    @Override
//    public String getUsername() {
//        return getEmail();
//    }
//
//    @Override
//    public String getPassword() {
//        return getPassword();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
