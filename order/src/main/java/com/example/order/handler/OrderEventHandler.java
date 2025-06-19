package com.example.order.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.order.dto.InventoryEvent;
import com.example.order.dto.PaymentEvent;
import com.example.order.service.OrderService;

import java.util.function.Consumer;

@Component
public class OrderEventHandler {
    
    private final OrderService orderService;

    public OrderEventHandler (OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public Consumer<PaymentEvent> paymentResultListener() {
        return event -> {
            String orderId = event.getOrderEvent().getOrderId();
            String status = event.getPaymentStatus();
            
            // Simpan/update status ke DB
            orderService.updateOrderStatus(orderId, status);

            System.out.println("✅ Payment Success for order : " + event.getOrderEvent().getOrderId() + ", Status : " + event.getPaymentStatus());
        };
    }

    @Bean
    public Consumer<InventoryEvent> inventoryOutOfStockListener() {
        return status -> {
            System.out.println("❌ Order failed due to out of stock: " + status.getOrderEvent().getOrderId());
            // Update status order di DB kalau kamu simpan, atau kasih notifikasi ke user
        };
    }

    @Bean
    public Consumer<PaymentEvent> paymentFailedListener() {
        return event -> {

            String orderId = event.getOrderEvent().getOrderId();
            String status = event.getPaymentStatus();
            
            // Simpan/update status ke DB
            orderService.updateOrderStatus(orderId, status);

            System.out.println("❌ Payment FAILED for order: " + event.getOrderEvent().getOrderId() + ", Status : " + event.getPaymentStatus());
        };
    }

}
