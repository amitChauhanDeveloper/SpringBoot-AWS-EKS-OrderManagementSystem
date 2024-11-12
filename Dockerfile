# Start from an OpenJDK 21 image
FROM openjdk:21-jdk-slim AS build

# Install Maven 3.9.2 manually
RUN apt-get update && apt-get install -y curl && \
    curl -sSL https://archive.apache.org/dist/maven/maven-3/3.9.2/binaries/apache-maven-3.9.2-bin.tar.gz | tar xz -C /opt && \
    ln -s /opt/apache-maven-3.9.2/bin/mvn /usr/local/bin/mvn

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY . /app

# Run Maven to build the JAR files (both order-service and product-service)
RUN mvn clean install -DskipTests

# Now use a smaller OpenJDK base image for running the application
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the generated JAR files from the build stage
COPY --from=build /app/order-service/target/ordermanagement-order-service.jar /app/ordermanagement-order-service.jar
COPY --from=build /app/product-service/target/ordermanagement-product-service.jar /app/ordermanagement-product-service.jar

# Expose the required ports for both services
EXPOSE 8082 8083

# Run both services in the background using `sh -c` to start both JAR files
CMD ["sh", "-c", "java -jar /app/ordermanagement-order-service.jar & java -jar /app/ordermanagement-product-service.jar"]
