package com.example.codewithpraveen.banking_management_system.Service;


import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;

import java.util.List;


public interface UserService {

//    create user

    UserDto createUser(UserDto userDto);
//    update user
    UserDto updateUser(UserDto userDto , Integer id);
//    delete user
    void deleteUser(Integer id);
//    get all user
    List<UserDto> getAllUsers();
//    get user by email
    UserDto getUserByEmail(String email);
//    get user by id
    UserDto getUserById(Integer id);
}
