package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import com.example.codewithpraveen.banking_management_system.Service.JwtService;
import com.example.codewithpraveen.banking_management_system.Service.UserService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.AuthRequest;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;
import com.example.codewithpraveen.banking_management_system.payLoad.JwtResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.RefreshTokenRequest;
import com.example.codewithpraveen.banking_management_system.payLoad.LogoutRequest;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;
    
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
    @PreAuthorize("hasRole('User')")
    @PutMapping(value = "/{id}" , produces = "application/json")
    public ResponseEntity<UserDto> updateUser( @Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        UserDto updatedUser = userService.updateUser(userDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
//    delete users by id
//    @PreAuthorize("hasRole('User')")
@PreAuthorize("hasRole('User')")
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
    @PreAuthorize("hasRole('User')")
 @GetMapping(value = "/{email}" , produces = "application/json")
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(this.userService.getUserByEmail(email));
    }
    
    @PostMapping(value = "/login" , produces = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            User user = userRepo.findByEmail(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Generate both access and refresh tokens
            String accessToken = jwtService.generateAccessToken(authRequest.getUsername(), user.getRoles().toString());
            String refreshToken = jwtService.generateRefreshToken(authRequest.getUsername());
            
            UserDto userDto = modelMapper.map(user, UserDto.class);
            
            // Calculate expiration time in seconds
            long expiresIn = jwtService.getExpirationTime(accessToken) - System.currentTimeMillis();
            expiresIn = expiresIn / 1000; // Convert to seconds
            
            JwtResponse jwtResponse = new JwtResponse(userDto, accessToken, refreshToken, expiresIn);
            return ResponseEntity.ok(jwtResponse);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Invalid credentials", false));
        }
    }
    
    @PostMapping(value = "/refresh-token", produces = "application/json")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = jwtService.getUsernameFromToken(refreshToken);
            
            if (jwtService.validateRefreshToken(refreshToken, username)) {
                User user = userRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                // Generate new access token
                String newAccessToken = jwtService.generateAccessToken(username, user.getRoles().toString());
                
                UserDto userDto = modelMapper.map(user, UserDto.class);
                long expiresIn = jwtService.getExpirationTime(newAccessToken) - System.currentTimeMillis();
                expiresIn = expiresIn / 1000;
                
                JwtResponse jwtResponse = new JwtResponse(userDto, newAccessToken, refreshToken, expiresIn);
                return ResponseEntity.ok(jwtResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid refresh token", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Token refresh failed", false));
        }
    }
    
    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            // Revoke both access and refresh tokens
            jwtService.revokeToken(logoutRequest.getAccessToken());
            if (logoutRequest.getRefreshToken() != null) {
                jwtService.revokeToken(logoutRequest.getRefreshToken());
            }
            
            return ResponseEntity.ok(new ApiResponse("Logged out successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Logout failed", false));
        }
    }
}
