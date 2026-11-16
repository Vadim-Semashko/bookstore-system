package com.example.gateway.resolver;

import com.example.gateway.client.MockUserClient;
import com.example.gateway.dto.RegisterUserInput;
import com.example.gateway.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserMutationResolver {

    private final MockUserClient userClient;

    @MutationMapping
    public UserDto registerUser(@Argument("input") RegisterUserInput input) {
        return userClient.registerUser(input);
    }
}
