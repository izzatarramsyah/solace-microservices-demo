package com.example.inventory.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.inventory.model.ProductStock;

@Repository
public class StockRepository {
    
    private final Map<String, ProductStock> stockMap = new HashMap<>();

    public StockRepository() {
        // Simulasi data awal
        stockMap.put("P001", new ProductStock("P001", 100));
        stockMap.put("P002", new ProductStock("P002", 50));
    }

    public ProductStock findByProductId(String productId) {
        return stockMap.get(productId);
    }

    public void save(ProductStock stock) {
        stockMap.put(stock.getProductId(), stock);
    }

}
