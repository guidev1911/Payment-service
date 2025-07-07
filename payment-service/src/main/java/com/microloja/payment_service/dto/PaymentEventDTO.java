package com.microloja.payment_service.dto;

import java.math.BigDecimal;

public class PaymentEventDTO {
    private String orderId;
    private String product;
    private BigDecimal total;
}
