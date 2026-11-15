package com.example.gateway.resolver;

import com.example.gateway.client.MockOrderClient;
import com.example.gateway.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderQueryResolver {

    private final MockOrderClient orderClient;

    @QueryMapping
    public List<OrderDto> myOrders() {
        return orderClient.getOrdersByClientId(
                orderClient.getTestClientId()
        );
    }

    @QueryMapping
    public OrderDto order(@Argument String id) {
        return orderClient.getOrderById(id).orElse(null);
    }
}
