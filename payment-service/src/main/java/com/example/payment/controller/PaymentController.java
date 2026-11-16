package com.example.payment.controller;

import com.example.payment.dto.PayOrderRequest;
import com.example.payment.dto.PaymentDto;
import com.example.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentDto> getPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(
            @PathVariable UUID id
    ) {
        return ResponseEntity.of(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentDto> getOrderPayments(
            @PathVariable UUID orderId
    ) {
        return paymentService.getPaymentsByOrderId(orderId);
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentDto> payOrder(@Valid @RequestBody PayOrderRequest request) {
        return ResponseEntity.status(201)
                .body(paymentService.payOrder(request.orderId()));
    }

}
