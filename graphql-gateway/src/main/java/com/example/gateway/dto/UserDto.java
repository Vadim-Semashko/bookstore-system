package com.example.gateway.dto;

public record UserDto(
        String id,
        String login,
        String email,
        String role,
        String status
) {}
