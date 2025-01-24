package com.example.codewithpraveen.banking_management_system.Service.ServiceImpl;

import com.example.codewithpraveen.banking_management_system.Entites.Role;
import com.example.codewithpraveen.banking_management_system.Entites.User;
import com.example.codewithpraveen.banking_management_system.Exceptions.ResourceNotFoundException;
import com.example.codewithpraveen.banking_management_system.Repository.RoleRepo;
import com.example.codewithpraveen.banking_management_system.Repository.UserRepo;
import com.example.codewithpraveen.banking_management_system.Service.UserService;
import com.example.codewithpraveen.banking_management_system.payLoad.Dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = this.roleRepo.findByRoleId(1);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        if (role == null) {
            throw new ResourceNotFoundException("Role", "roleId", 1);
           
        }
        user.getRoles().add(role);
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }
	
    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
       User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User ", "id", id));
      user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setAddress(userDto.getAddress());
       User savedUser = userRepo.save(user);
         return modelMapper.map(savedUser, UserDto.class);
        
    }

    @Override
    public void deleteUser(Integer id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User ", "id", id));
        this.userRepo.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
       List<User> users = userRepo.findAll();
         return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
       User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User ", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User ", "id", id));
        return modelMapper.map(user, UserDto.class);
    }
}
