# Stage 1: Build Order Service
FROM openjdk:21-jdk as order-service-build

WORKDIR /app

# Copy order service JAR from the correct path
COPY order-service/target/ordermanagement-order-service.jar /app/ordermanagement-order-service.jar

# Stage 2: Build Product Service
FROM openjdk:21-jdk as product-service-build

WORKDIR /app

# Copy product service JAR from the correct path
COPY product-service/target/ordermanagement-product-service.jar /app/ordermanagement-product-service.jar
