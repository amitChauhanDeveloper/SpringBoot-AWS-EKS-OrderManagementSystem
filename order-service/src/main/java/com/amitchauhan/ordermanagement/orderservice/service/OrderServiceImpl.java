package com.amitchauhan.ordermanagement.orderservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.amitchauhan.ordermanagement.orderservice.client.ProductServiceClient;
import com.amitchauhan.ordermanagement.orderservice.dto.OrderDto;
import com.amitchauhan.ordermanagement.orderservice.dto.OrderItemDto;
import com.amitchauhan.ordermanagement.orderservice.dto.OrderResponse;
import com.amitchauhan.ordermanagement.orderservice.dto.ProductAvailability;
import com.amitchauhan.ordermanagement.orderservice.entity.Order;
import com.amitchauhan.ordermanagement.orderservice.entity.OrderItem;
import com.amitchauhan.ordermanagement.orderservice.exception.BadRequestException;
import com.amitchauhan.ordermanagement.orderservice.exception.ResourceNotFoundException;
import com.amitchauhan.ordermanagement.orderservice.kafka.OrderProducer;
import com.amitchauhan.ordermanagement.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderProducer orderProducer;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> 
        new ResourceNotFoundException("Order","order id",id));
        return convertToDto(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDto orderDto) {

        // Filter available items by checking with ProductService
        List<ProductAvailability> availabilityList = productServiceClient.checkProductAvailability(orderDto.getOrderItems());

        // Filter available items based on the availability response
        List<OrderItemDto> availableItems = orderDto.getOrderItems().stream()
                .filter(item -> isProductAvailable(item.getSkuCode(), availabilityList))
                .collect(Collectors.toList());

        // Handle case where no items are available
        if (availableItems.isEmpty()) {
            log.warn("No items available in the order request.");
            throw new BadRequestException("No items available for the order.");
        }
        // Update the order with available items only
        orderDto.setOrderItems(availableItems);

        Order order = convertToEntity(orderDto);

        Order finalOrder = order;
        List<OrderItem> orderItemList = orderDto.getOrderItems().stream()
                .map(itemDto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemDto.getSkuCode());
                    orderItem.setProductName(itemDto.getProductName());
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setOrder(finalOrder);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItemList);
        order.setOrderStatus("Order Placed");
        

        try {
        log.info("Order placed successfully, sending 'Order Placed' event to Kafka...");
        order = orderRepository.save(order);
        orderDto.setId(order.getId());
        orderProducer.sendOrderEvent(orderDto, "placed");  // Send event to Kafka
            
        } catch (Exception e_) {}

        return OrderResponse.builder()
                .orderId(order.getId())
                .status("Order Placed")
                .build();

        
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> 
        new ResourceNotFoundException("Order","order id",id));
        // order.setId(orderDto.getId());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setOrderDate(orderDto.getOrderDate());
    
        // Handle order items - only update existing items or add new items if necessary
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            boolean exists = false;
            for (OrderItem orderItem : order.getOrderItems()) {
                // Assuming orderItem has an id, check if it already exists in the order
                if (orderItem.getId().equals(orderItemDto.getId())) {
                    // Update the existing item
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setPrice(orderItemDto.getPrice());
                    exists = true;
                    break;
                }
            }
            // If the item doesn't exist, add it as a new item
            if (!exists) {
                order.getOrderItems().add(convertToEntity(orderItemDto));
            }
         }

        order = orderRepository.save(order);

        return convertToDto(order);
    }

    @Override
    public void deleteOrder(Long id) {

        orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order","order id",id));

        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order","order id", + orderId));

        if(!order.getOrderStatus().equals("ORDER_CANCELLED")){
            order.setOrderStatus("Order Cancelled");
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderItems(order.getOrderItems().stream()
                .map(item -> new OrderItemDto(item.getId(),item.getProductId(),item.getProductName(),item.getQuantity(),item.getPrice()))  // Assuming constructor or converter for OrderItemDto
                .collect(Collectors.toList()));

        try {
            log.info("Order cancelled successfully, sending 'Order cancelled' event to Kafka...");
            // order = orderRepository.save(order);
            orderRepository.save(order);
            // orderDto.setId(order.getId());
            orderProducer.sendOrderEvent(orderDto, "cancelled"); // Send cancellation event with full order data
                
            } catch (Exception e_) {}

        // orderProducer.sendOrderEvent(orderDto, "cancelled");  // Send cancellation event with full order data
        return new OrderResponse(order);  // Return updated order response
    }
    private OrderDto convertToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .orderItems(order.getOrderItems().stream().map(this::convertToDto).collect(Collectors.toList()))
                .build();
    }

    private OrderItemDto convertToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .skuCode(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    private Order convertToEntity(OrderDto orderDto) {
        return Order.builder()
                .totalPrice(orderDto.getTotalPrice())
                .orderDate(orderDto.getOrderDate())
                .orderItems(orderDto.getOrderItems().stream().map(this::convertToEntity).collect(Collectors.toList()))
                .build();
    }

    private OrderItem convertToEntity(OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .productId(orderItemDto.getSkuCode())
                .productName(orderItemDto.getProductName())
                .quantity(orderItemDto.getQuantity())
                .price(orderItemDto.getPrice())
                .build();
    }


    /**
     * Checks if the product is available based on the SKU code and availability list.
     *
     * @param skuCode          The SKU code of the product.
     * @param availabilityList The list of product availability.
     * @return true if the product is available; otherwise false.
     */
    @Override
    public boolean isProductAvailable(String skuCode, List<ProductAvailability> availabilityList) {
        return availabilityList.stream()
                .anyMatch(availability -> availability.getSkuCode().equals(skuCode) && availability.isAvailable());
    }
}
