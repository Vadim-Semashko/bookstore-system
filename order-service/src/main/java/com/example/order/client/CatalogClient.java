package com.example.order.client;

import java.math.BigDecimal;
import java.util.UUID;

public interface CatalogClient {

    BigDecimal reserveBook(
            UUID orderId,
            UUID bookId,
            int quantity
    );

    void releaseReservation(UUID orderId);
}
