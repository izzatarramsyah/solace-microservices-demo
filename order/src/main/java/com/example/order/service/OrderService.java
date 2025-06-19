package com.example.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.order.model.OrderCreatedEvent;

@Service
public class OrderService {

    @Autowired
    StreamBridge streamBridge;

    public void sendOrder(OrderCreatedEvent event){
        streamBridge.send("sendOrder-out-0", MessageBuilder.withPayload(event).build());
        System.out.println("✅ Event dikirim: " + event.getOrderId());
    }
    
    public void updateOrderStatus(String orderId, String status) {
        System.out.println("📝 [DUMMY] Updating order ID " + orderId + " with status: " + status);
        // Simulasi update, bisa taruh ke Map jika ingin track state
    }


}
