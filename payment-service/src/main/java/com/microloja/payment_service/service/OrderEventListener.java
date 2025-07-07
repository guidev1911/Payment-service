package com.microloja.payment_service.service;

import com.microloja.payment_service.dto.OrderEvent;
import com.microloja.payment_service.model.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.microloja.payment_service.model.Payment;
import com.microloja.payment_service.repository.PaymentRepository;

import java.time.LocalDateTime;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    private final PaymentRepository repository;

    public OrderEventListener(PaymentRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "order.queue")
    public void listen(OrderEvent event) {
        logger.info("📦 Pedido recebido: {}", event);

        try {
            Thread.sleep(2000); // Simulando processamento

            PaymentStatus status = Math.random() > 0.2 ? PaymentStatus.APPROVED : PaymentStatus.REJECTED;

            Payment payment = new Payment();
            payment.setOrderId(event.getOrderId());
            payment.setProduct(event.getProduct());
            payment.setQuantity(event.getQuantity());
            payment.setTotal(event.getTotal());
            payment.setProcessedAt(LocalDateTime.now());
            payment.setStatus(status);

            repository.save(payment);

            logger.info("✅ Pagamento {} para pedido {}!", status, event.getOrderId());

        } catch (InterruptedException e) {
            logger.error("❌ Erro ao processar pedido {}", event.getOrderId(), e);
            Thread.currentThread().interrupt();
        }
    }
}
