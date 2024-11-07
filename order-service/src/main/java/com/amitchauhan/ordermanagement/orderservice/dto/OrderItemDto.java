package com.amitchauhan.ordermanagement.orderservice.dto;

// import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
// @AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private String skuCode;

    public OrderItemDto(Long id,String skuCode,String productName,int quantity,BigDecimal price) {
        this.id = id;
        this.skuCode = skuCode;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    private String productName;
    private int quantity;
    private BigDecimal price;

}

