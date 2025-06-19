# 🧩 Solace Event-Driven Microservices Demo

This project demonstrates an **event-driven microservices architecture** using **Spring Boot**, **Spring Cloud Stream**, and **Solace PubSub+**. It simulates an order flow system involving inventory checks and payment processing.

---

## 📦 Services Overview

### 🛒 Order Service
- Publishes new order events to Solace
- Listens for payment results (success or failure)
- Updates order status accordingly

### 🏪 Inventory Service
- Listens to order events
- Checks and updates stock
- Sends stock status (available or out of stock)
- Handles payment failures by restoring stock (rollback)

### 💳 Payment Service
- Listens to inventory stock availability
- Simulates payment success or failure
- Sends payment result back to order service

---

## How to Run

```bash
docker-compose up

cd order-service && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
cd payment-service && mvn spring-boot:run

---

## 🧬 Event Flow Diagram

```text
[Order Service]
    |
    v
order.created  🔁 ---------------> [Inventory Service]
                                      |
                        inventory.reserved / inventory.outofstock
                                      |
                                      v
                            [Payment Service]
                                |
              payment.completed / payment.failed
                                |
                                v
                          [Order Service]
