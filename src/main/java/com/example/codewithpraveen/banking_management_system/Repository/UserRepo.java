package com.example.codewithpraveen.banking_management_system.Repository;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

 Optional<User> findByEmail(String email);
}