package org.example.kafka.service;

import org.example.kafka.dto.OrderEvent;
import org.example.kafka.dto.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderValidationService {

    public OrderStatus validate(OrderEvent orderEvent) {

        if (orderEvent.getAmount() > 0) {
            return OrderStatus.ACCEPTED;
        }

        return OrderStatus.REJECTED;
    }
}
