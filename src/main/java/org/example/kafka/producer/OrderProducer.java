package org.example.kafka.producer;

import org.example.kafka.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(
            @Qualifier("orderKafkaTemplate")
            KafkaTemplate<String, OrderEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(OrderEvent orderEvent) {
        kafkaTemplate.send("orders.created", orderEvent);
    }
}
