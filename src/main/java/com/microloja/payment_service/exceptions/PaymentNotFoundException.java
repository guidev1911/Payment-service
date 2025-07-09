package com.microloja.payment_service.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Pagamento não encontrado com id: " + id);
    }
}
