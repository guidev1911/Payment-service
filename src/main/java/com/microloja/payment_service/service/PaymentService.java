package com.microloja.payment_service.service;

import com.microloja.payment_service.dto.PaymentResponseDTO;
import com.microloja.payment_service.exceptions.PaymentNotFoundException;
import com.microloja.payment_service.model.Payment;
import com.microloja.payment_service.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<PaymentResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public PaymentResponseDTO findById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pagamento não encontrado com id: {}", id);
                    return new PaymentNotFoundException(id);
                });
        return toDTO(payment);
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
