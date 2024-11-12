# Use an official JDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the application's JAR file to the container
COPY target/ordermanagement-order-service.jar ordermanagement-order-service.jar

# Expose the port your application runs on
EXPOSE 8082

# Run the application with the specified profile
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "ordermanagement-order-service.jar"]