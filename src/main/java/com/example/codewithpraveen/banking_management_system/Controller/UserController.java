package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import com.example.codewithpraveen.banking_management_system.Service.JwtService;
import com.example.codewithpraveen.banking_management_system.Service.UserService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.AuthRequest;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;

import com.example.codewithpraveen.banking_management_system.payLoad.JwtResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    private JwtService jwtService;
    
    @Autowired
    private UserRepo userRepo;
    
    
    public UserController(AuthenticationManager authenticationManager, JwtService jwtService ,  UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    
    //    create user
    @PostMapping(value = "/create" , produces = "application/json")
    public ResponseEntity<UserDto>  createUser( @Valid @RequestBody UserDto userDto) {

    UserDto createdUser = userService.createUser(userDto);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    //    update user
    @PutMapping(value = "/{id}" , produces = "application/json")
    public ResponseEntity<UserDto> updateUser( @Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        UserDto updatedUser = userService.updateUser(userDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
//    delete users by id
//    @PreAuthorize("hasRole('User')")
    @DeleteMapping(value = "/{id}" , produces = "application/json")
    public  ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse( "User deleted successfully" , true ), HttpStatus.OK);
    }
   
   
    //    get all users
    @GetMapping(value = "/" , produces = "application/json")
    public ResponseEntity<List<UserDto>> getAllUsers() {
      return ResponseEntity.ok(this.userService.getAllUsers());
    }
    
    



//    get user email
 @GetMapping(value = "/{email}" , produces = "application/json")
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(this.userService.getUserByEmail(email));
    }
    
    @PostMapping(value = "/login" , produces = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
     
     String token = jwtService.generateToken(authRequest.getUsername());
     User user = userRepo.findByEmail(authRequest.getUsername()).get();
     JwtResponse jwtResponse = new JwtResponse( user, token);
     return ResponseEntity.ok(jwtResponse);
     
    }
    
    
}





