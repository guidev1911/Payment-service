package com.microloja.payment_service.service;

import com.microloja.payment_service.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @RabbitListener(queues = "order.queue")
    public void listen(OrderEvent event) {
        logger.info("📦 Pedido recebido para pagamento: {}", event);

        try {
            // Simulando tempo de processamento (ex: integração com gateway de pagamento)
            Thread.sleep(2000);

            // Simulação de aprovação (poderia ser aleatório ou por regra)
            logger.info("✅ Pagamento aprovado para pedido ID: {}", event.getOrderId());

        } catch (InterruptedException e) {
            logger.error("❌ Falha ao processar pagamento do pedido ID: {}", event.getOrderId(), e);
            Thread.currentThread().interrupt();
        }
    }
}

