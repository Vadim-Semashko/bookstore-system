package com.example.order.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MockCatalogClient implements CatalogClient {

    private static final UUID BOOK_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID BOOK_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private final Map<UUID, BigDecimal> prices = Map.of(
            BOOK_1, new BigDecimal("35.50"),
            BOOK_2, new BigDecimal("28.00")
    );

    private final Map<UUID, Integer> stock = new HashMap<>();

    private final Map<UUID, Map<UUID, Integer>> reservations = new HashMap<>();

    public MockCatalogClient() {
        stock.put(BOOK_1, 15);
        stock.put(BOOK_2, 10);
    }

    @Override
    public synchronized BigDecimal reserveBook(UUID orderId, UUID bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        BigDecimal price = prices.get(bookId);

        if (price == null) {
            throw new IllegalArgumentException("Книга не найдена");
        }

        int available = stock.getOrDefault(bookId, 0);

        if (available < quantity) {
            throw new IllegalStateException("Недостаточно экземпляров книги");
        }

        stock.put(bookId, available - quantity);

        reservations
                .computeIfAbsent(orderId, id -> new HashMap<>())
                .merge(bookId, quantity, Integer::sum);

        return price;
    }

    @Override
    public synchronized void releaseReservation(UUID orderId) {
        Map<UUID, Integer> reserved = reservations.remove(orderId);

        if (reserved == null) {
            return;
        }

        reserved.forEach((bookId, quantity) -> stock.merge(bookId, quantity, Integer::sum));
    }

    public synchronized int getAvailableStock(UUID bookId) {
        if (!prices.containsKey(bookId)) {
            throw new IllegalArgumentException("Книга не найдена");
        }

        return stock.getOrDefault(bookId, 0);
    }


}
