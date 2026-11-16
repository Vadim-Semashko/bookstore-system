package com.example.gateway.resolver;

import com.example.gateway.client.MockPaymentClient;
import com.example.gateway.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PaymentMutationResolver {

    private final MockPaymentClient paymentClient;

    @MutationMapping
    public PaymentDto payOrder(@Argument String orderId) {
        return paymentClient.payOrder(orderId);
    }
}
