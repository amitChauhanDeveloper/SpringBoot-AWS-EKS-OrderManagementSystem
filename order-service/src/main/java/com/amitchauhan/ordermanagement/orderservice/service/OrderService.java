package com.amitchauhan.ordermanagement.orderservice.service;

import java.util.List;
import com.amitchauhan.ordermanagement.orderservice.dto.OrderDto;
import com.amitchauhan.ordermanagement.orderservice.dto.OrderResponse;
import com.amitchauhan.ordermanagement.orderservice.dto.ProductAvailability;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderResponse createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);

    OrderResponse cancelOrder(Long id);

    boolean isProductAvailable(String skuCode, List<ProductAvailability> availabilityList);
}
