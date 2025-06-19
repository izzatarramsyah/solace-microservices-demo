package com.example.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.payment.dto.OrderEvent;
import com.example.payment.dto.PaymentEvent;

@Service
public class PaymentService {
    
    @Autowired
    private StreamBridge streamBridge;

    public void handleInventoryReserved(OrderEvent event) {
        // Simulasi pembayaran berhasil
        boolean paymentSuccess = false; // atau logic real
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderEvent(event);
        if (paymentSuccess) { 
            paymentEvent.setPaymentStatus("Paid");
            streamBridge.send("paymentCompleted-out-0", MessageBuilder.withPayload(paymentEvent).build());
            System.out.println("✅ Payment success: " + paymentEvent);
        } else {
            paymentEvent.setPaymentStatus("Payment Declined");
            streamBridge.send("paymentFailed-out-0", MessageBuilder.withPayload(paymentEvent).build());
            System.out.println("❌ Payment failed: " + paymentEvent);
        }
    }
}
