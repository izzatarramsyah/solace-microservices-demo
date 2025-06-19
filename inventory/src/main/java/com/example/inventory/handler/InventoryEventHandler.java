package com.example.inventory.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.inventory.dto.OrderEvent;
import com.example.inventory.dto.PaymentEvent;
import com.example.inventory.model.ProductStock;
import com.example.inventory.service.InventoryService;

import java.util.function.Consumer;

@Component
public class InventoryEventHandler {
    
    private final InventoryService inventoryService;

    public InventoryEventHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Bean
    public Consumer<OrderEvent> processOrder() {
        System.out.println("🔧 Bean processOrder() initialized!");
        return order -> {
            System.out.println("✅ Received order event: " + order.getOrderId());
            inventoryService.handleOrder(order);
        };
    }

    @Bean
    public Consumer<PaymentEvent> handlePaymentFailed() {
        return event -> {
            inventoryService.rollbackStock(event.getOrderEvent());
            System.out.println("🔁 Rolled back stock for failed payment: " + event.getOrderEvent().getOrderId());  
        };
    }

}
