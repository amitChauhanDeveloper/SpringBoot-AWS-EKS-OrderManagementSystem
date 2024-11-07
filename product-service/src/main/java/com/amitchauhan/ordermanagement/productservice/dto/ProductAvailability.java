package com.amitchauhan.ordermanagement.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAvailability {
    private String skuCode;
    private boolean isAvailable;
}


