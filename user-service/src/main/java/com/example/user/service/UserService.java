package com.example.user.service;

import com.example.user.dto.ClientDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.Client;
import com.example.user.entity.User;
import com.example.user.repository.ClientRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserDto)
                .toList();
    }

    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::toUserDto);
    }

    public Optional<ClientDto> getClientByUserId(UUID userId) {
        return clientRepository.findByUserId(userId)
                .map(this::toClientDto);
    }

    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getRole().getName(),
                user.getStatus()
        );
    }

    private ClientDto toClientDto(Client client) {
        return new ClientDto(
                client.getId(),
                client.getUser().getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone()
        );
    }
}
