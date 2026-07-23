package org.example.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.dto.OrderEvent;
import org.example.kafka.dto.OrderResult;
import org.example.kafka.dto.OrderStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.example.kafka.service.OrderValidationService;

@Slf4j
@Service
public class OrderConsumer {


    private static final String OUTPUT_TOPIC = "orders.processed";


    private final KafkaTemplate<String, OrderResult> kafkaTemplate;
    private final OrderValidationService validationService;



    public OrderConsumer(
            @Qualifier("orderResultKafkaTemplate")
            KafkaTemplate<String, OrderResult> kafkaTemplate,
            OrderValidationService validationService) {

        this.kafkaTemplate = kafkaTemplate;
        this.validationService = validationService;
    }



    @KafkaListener(
            topics = "orders.created",
            groupId = "orders-group"
    )
    public void processOrder(@Payload OrderEvent orderEvent) {


        log.info(
                "Received order: {}",
                orderEvent
        );


        OrderStatus status =
                validationService.validate(orderEvent);


        log.info(
                "Validation result: {}",
                status
        );


        OrderResult result =
                new OrderResult(
                        orderEvent.getOrderId(),
                        status
                );


        kafkaTemplate.send(
                OUTPUT_TOPIC,
                result
        );


        log.info(
                "Sent result: {}",
                result
        );

    }

}
