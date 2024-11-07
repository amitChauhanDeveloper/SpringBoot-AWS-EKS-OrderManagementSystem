package com.amitchauhan.ordermanagement.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.amitchauhan.ordermanagement.orderservice.entity.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
}


