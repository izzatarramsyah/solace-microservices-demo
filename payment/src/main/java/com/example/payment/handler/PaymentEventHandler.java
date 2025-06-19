package com.example.payment.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.payment.dto.InventoryStatus;
import com.example.payment.service.PaymentService;

import java.util.function.Consumer;

@Component
public class PaymentEventHandler {

    private final PaymentService paymentService;

    public PaymentEventHandler (PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Bean
    public Consumer<InventoryStatus> handleInventoryReserved() {
        System.out.println("🔧 Bean handleInventoryReserved() initialized!");
        return event -> {
            if ("AVAILABLE".equalsIgnoreCase(event.getStatus())) {
                System.out.println("💰 Inventory available, proceeding with payment for: " + event.getOrderEvent().getOrderId());
                paymentService.handleInventoryReserved(event.getOrderEvent());
            } else {
                System.out.println("⚠️ Skipped payment: Status not AVAILABLE (" + event.getStatus() + ")");
            }
        };
    }
    
}
