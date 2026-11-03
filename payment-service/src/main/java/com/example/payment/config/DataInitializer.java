package com.example.payment.config;

import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializePayments(PaymentRepository paymentRepository) {
        return args -> {
            if (paymentRepository.count() > 0) {
                return;
            }

            Payment payment = new Payment();

            payment.setOrderId(
                    UUID.fromString("5dc6ba30-74bb-4e31-82bb-2620862804ff")
            );

            payment.setAmount(new BigDecimal("99.00"));
            payment.setStatus("COMPLETED");
            payment.setPaymentDate(LocalDateTime.now());

            paymentRepository.save(payment);
        };
    }
}
