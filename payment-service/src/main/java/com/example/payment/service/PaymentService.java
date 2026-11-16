package com.example.payment.service;

import com.example.payment.client.OrderClient;
import com.example.payment.dto.PaymentDto;
import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<PaymentDto> getPaymentById(UUID id) {
        return paymentRepository.findById(id)
                .map(this::toDto);
    }

    public List<PaymentDto> getPaymentsByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public PaymentDto payOrder(UUID orderId) {

        if (orderId == null) {
            throw new IllegalArgumentException("Идентификатор заказа обязателен");
        }

        OrderClient.OrderInfo order = orderClient.getOrder(orderId);

        if (!"CREATED".equals(order.status())) {
            throw new IllegalStateException("Оплатить можно только заказ со статусом CREATED");
        }

        if (paymentRepository.existsByOrderIdAndStatus(
                orderId, "COMPLETED")) {
            throw new IllegalStateException("Заказ уже оплачен");
        }

        if (order.totalAmount() == null ||
                order.totalAmount().signum() <= 0) {
            throw new IllegalStateException("Некорректная сумма заказа");
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.totalAmount());
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        orderClient.markOrderPaid(orderId);

        return toDto(payment);
    }

    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentDate()
        );
    }
}
