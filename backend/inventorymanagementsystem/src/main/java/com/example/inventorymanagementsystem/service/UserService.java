package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.dto.UserDto;
import com.example.inventorymanagementsystem.entity.User;
import com.example.inventorymanagementsystem.payload.LoginRequest;
import com.example.inventorymanagementsystem.payload.RegisterRequest;
import com.example.inventorymanagementsystem.response.Response;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response updateUser(Long id, UserDto userDto);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);

}
