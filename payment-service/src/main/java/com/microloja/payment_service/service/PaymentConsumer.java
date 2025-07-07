package com.microloja.payment_service.service;

import com.microloja.payment_service.dto.OrderEvent;
import com.microloja.payment_service.model.Payment;
import com.microloja.payment_service.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

    private final PaymentRepository repository;

    public PaymentConsumer(PaymentRepository repository) {
        this.repository = repository;
    }
    @RabbitListener(queues = "order.queue")
    public void consume(OrderEvent event) {
        logger.info("Recebido do RabbitMQ: {}", event);

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getTotal());
        payment.setProduct(event.getProduct());
        payment.setProcessedAt(LocalDateTime.now());

        repository.save(payment);
        logger.info("Pagamento registrado: {}", payment);
    }
}
