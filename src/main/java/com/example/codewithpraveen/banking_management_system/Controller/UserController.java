package com.example.codewithpraveen.banking_management_system.Controller;

import com.example.codewithpraveen.banking_management_system.Service.UserService;
import com.example.codewithpraveen.banking_management_system.payLoad.ApiResponse;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //    create user
    @PostMapping(value = "/create" , produces = "application/json")
    public ResponseEntity<UserDto>  createUser( @Valid @RequestBody UserDto userDto) {

    UserDto createdUser = userService.createUser(userDto);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    //    update user
    @PostMapping(value = "/{id}" , produces = "application/json")
    public ResponseEntity<UserDto> updateUser( @Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        UserDto updatedUser = userService.updateUser(userDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
//    delete users by id
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





//    get user by id

}
