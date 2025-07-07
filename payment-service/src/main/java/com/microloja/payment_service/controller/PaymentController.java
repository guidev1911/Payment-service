package com.microloja.payment_service.controller;

import com.microloja.payment_service.dto.PaymentResponseDTO;
import com.microloja.payment_service.model.Payment;
import com.microloja.payment_service.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository repository;

    public PaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        var payments = repository.findAll().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(payments);
    }

    private PaymentResponseDTO toDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setProduct(payment.getProduct());
        dto.setProcessedAt(payment.getProcessedAt());
        return dto;
    }
}
