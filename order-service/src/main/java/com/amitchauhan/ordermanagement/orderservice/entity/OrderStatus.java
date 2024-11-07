package com.amitchauhan.ordermanagement.orderservice.entity;

public enum OrderStatus {
    ORDER_PLACED,
    ORDER_CANCELLED;

    @Override
    public String toString() {
        return "OrderStatus{}";
    }
}
