package com.example.gateway.dto;

public record RegisterUserInput(
        String login,
        String email,
        String password,
        String firstName,
        String lastName,
        String phone
) {}
