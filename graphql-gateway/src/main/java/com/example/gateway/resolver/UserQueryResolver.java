package com.example.gateway.resolver;

import com.example.gateway.client.MockUserClient;
import com.example.gateway.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserQueryResolver {

    private final MockUserClient userClient;

    @QueryMapping
    public UserDto me() {
        return userClient.getCurrentUser().orElse(null);
    }
}
