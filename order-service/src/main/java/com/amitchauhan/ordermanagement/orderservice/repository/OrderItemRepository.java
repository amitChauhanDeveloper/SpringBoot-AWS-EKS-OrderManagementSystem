package com.amitchauhan.ordermanagement.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amitchauhan.ordermanagement.orderservice.entity.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}


