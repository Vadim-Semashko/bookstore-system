package com.example.user.controller;

import com.example.user.dto.ClientDto;
import com.example.user.dto.RegisterUserRequest;
import com.example.user.dto.UserDto;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @GetMapping("/{userId}/client")
    public ResponseEntity<ClientDto> getClient(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.of(
                userService.getClientByUserId(userId)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserRequest request) {
        UserDto user = userService.registerUser(request);
        return ResponseEntity
                .status(201)
                .body(user);
    }
}
