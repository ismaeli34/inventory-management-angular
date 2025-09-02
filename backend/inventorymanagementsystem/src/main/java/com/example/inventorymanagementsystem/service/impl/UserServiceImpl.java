package com.example.inventorymanagementsystem.service.impl;
import com.example.inventorymanagementsystem.dto.UserDto;
import com.example.inventorymanagementsystem.entity.User;
import com.example.inventorymanagementsystem.enums.UserRole;
import com.example.inventorymanagementsystem.exception.InvalidCredentialsException;
import com.example.inventorymanagementsystem.exception.NotFoundException;
import com.example.inventorymanagementsystem.payload.LoginRequest;
import com.example.inventorymanagementsystem.payload.RegisterRequest;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.repository.UserRepository;
import com.example.inventorymanagementsystem.security.JwtUtils;
import com.example.inventorymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        UserRole userRole = UserRole.MANAGER;
        if (registerRequest.getRole() != null){
            userRole=registerRequest.getRole();
        }
        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(userRole)
                .build();

        userRepository.save(userToSave);
        return Response.builder().status(200).message("User created successfully").build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest
                        .getEmail())
                .orElseThrow(()-> new NotFoundException("Email not found "));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("password does not match");
        }
        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in Successfully")
                .role(user.getRole())
                .token(token)
                .expirationDate("6 month")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        users.forEach(user -> user.setTransactions(null));
        List<UserDto> userDtos = modelMapper.map(users, new TypeToken<List<UserDto>>() {}.getType());
        userDtos.forEach(userDto -> userDto.setTransactions(null));
        return Response.builder().status(200).message("success").users(userDtos).build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Email Not Found"));
        user.setTransactions(null);
        return user;
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        if (userDto.getEmail()!=null) existingUser.setEmail(userDto.getEmail());
        if (userDto.getName()!=null) existingUser.setName(userDto.getName());
        if (userDto.getPhoneNumber()!=null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
        if (userDto.getRole()!=null) existingUser.setRole(userDto.getRole());

        if (userDto.getPassword() !=null && !userDto.getPassword().isEmpty()){
            existingUser.setPhoneNumber(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {

        userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        userRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("User successfully deleted")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .user(userDto)
                .message(" success")
                .build();
    }





}
