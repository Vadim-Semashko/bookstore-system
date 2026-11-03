package com.example.user.dto;

import java.util.UUID;

public record ClientDto(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String phone
) {}
