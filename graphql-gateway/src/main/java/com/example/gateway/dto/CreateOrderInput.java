package com.example.gateway.dto;

import java.util.List;

public record CreateOrderInput(
        List<CreateOrderItemInput> items
) {}
