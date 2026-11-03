package com.example.user.config;

import com.example.user.entity.Client;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.repository.ClientRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Bean
    CommandLineRunner initializeData() {
        return args -> {
            for (String name : new String[]{"CLIENT", "EMPLOYEE", "ADMIN"}) {
                if (roleRepository.findByName(name).isEmpty()) {
                    Role role = new Role();
                    role.setName(name);
                    roleRepository.save(role);
                }
            }

            if (userRepository.findByLogin("testclient").isEmpty()) {
                User user = new User();
                user.setLogin("testclient");
                user.setEmail("client@example.com");
                user.setPasswordHash("TEST_PASSWORD_HASH");
                user.setStatus("ACTIVE");
                user.setCreatedAt(LocalDateTime.now());
                user.setRole(roleRepository.findByName("CLIENT").orElseThrow());

                user = userRepository.save(user);

                Client client = new Client();
                client.setUser(user);
                client.setFirstName("Иван");
                client.setLastName("Иванов");
                client.setPhone("+375291234567");

                clientRepository.save(client);
            }
        };
    }
}
