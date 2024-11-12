# Stage 1: Build Order Service
FROM openjdk:21-jdk

WORKDIR /app

# Copy the application's JAR file to the container
COPY target/ordermanagement-order-service.jar ordermanagement-order-service.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "ordermanagement-order-service.jar"]

# Stage 2: Build Product Service
FROM openjdk:21-jdk

WORKDIR /app

# Copy product service JAR from the correct path
COPY target/ordermanagement-product-service.jar ordermanagement-product-service.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "ordermanagement-product-service.jar"]