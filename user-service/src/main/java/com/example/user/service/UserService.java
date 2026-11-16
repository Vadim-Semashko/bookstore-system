package com.example.user.service;

import com.example.user.dto.ClientDto;
import com.example.user.dto.RegisterUserRequest;
import com.example.user.dto.UserDto;
import com.example.user.entity.Client;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.repository.ClientRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public UserDto registerUser(RegisterUserRequest request) {

        if (userRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new IllegalStateException("Роль CLIENT не найдена"));

        User user = new User();
        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(clientRole);
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        Client client = new Client();
        client.setUser(user);
        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());
        client.setPhone(request.phone());

        clientRepository.save(client);

        return toUserDto(user);
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
