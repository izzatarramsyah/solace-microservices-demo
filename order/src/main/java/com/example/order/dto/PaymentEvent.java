package com.example.order.dto;

import com.example.order.model.OrderCreatedEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private OrderCreatedEvent orderEvent;
    private String paymentStatus;
}
