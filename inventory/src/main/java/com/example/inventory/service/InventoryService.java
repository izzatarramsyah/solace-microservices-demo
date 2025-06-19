package com.example.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.InventoryStatus;
import com.example.inventory.dto.OrderEvent;
import com.example.inventory.model.ProductStock;
import com.example.inventory.repository.StockRepository;

@Service
public class InventoryService {
    
    @Autowired
    private StreamBridge streamBridge;

    private final StockRepository stockRepository;

    public InventoryService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void handleOrder(OrderEvent order) {
        ProductStock stock = stockRepository.findByProductId(order.getItemId());
        InventoryStatus inventoryStatus = new InventoryStatus();
        inventoryStatus.setOrderEvent(order);

        if (stock != null) {
            if (stock.getStock() >= order.getQuantity()) {
                stock.setStock(stock.getStock() - order.getQuantity());
                stockRepository.save(stock);
                inventoryStatus.setStatus("AVAILABLE");
                streamBridge.send("inventoryReserved-out-0", MessageBuilder.withPayload(inventoryStatus).build());
                System.out.println("✅ Inventory reserved: " + order.getOrderId());
            } else {
                inventoryStatus.setStatus("OUT OF STOCK");
                streamBridge.send("inventoryOutOfStock-out-0", MessageBuilder.withPayload(inventoryStatus).build());
                System.out.println("⚠️ Not enough stock: " + order.getOrderId() + ", stock: " + stock.getStock() + ", requested: " + order.getQuantity());
            }
        } else {
            inventoryStatus.setStatus("OUT OF STOCK");
            streamBridge.send("inventoryOutOfStock-out-0", MessageBuilder.withPayload(inventoryStatus).build());
            System.out.println("❌ Product not found: " + order.getItemId());
        }

        if (stock != null) {
            System.out.println("✅ Order processed. Remaining stock: " + stock.getStock());
        }
    }

    public void rollbackStock(OrderEvent order) {
        ProductStock stock = stockRepository.findByProductId(order.getItemId());
        if (stock != null) {
            stock.setStock(stock.getStock() + order.getQuantity()); // rollback
            stockRepository.save(stock);
        } else {
            System.out.println("⚠️ No stock to rollback for: " + order.getItemId());
        }
    }

}
