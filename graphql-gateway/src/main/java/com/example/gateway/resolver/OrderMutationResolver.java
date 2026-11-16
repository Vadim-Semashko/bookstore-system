package com.example.gateway.resolver;

import com.example.gateway.client.MockOrderClient;
import com.example.gateway.dto.CreateOrderInput;
import com.example.gateway.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderMutationResolver {

    private final MockOrderClient orderClient;

    @MutationMapping
    public OrderDto createOrder(@Argument("input") CreateOrderInput input) {
        return orderClient.createOrder(input);
    }

    @MutationMapping
    public OrderDto cancelOrder(@Argument String orderId) {
        return orderClient.cancelOrder(orderId);
    }
}
