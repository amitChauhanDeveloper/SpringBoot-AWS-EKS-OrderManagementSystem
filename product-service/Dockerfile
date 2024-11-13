# Use an official JDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY . /app

# Specify the default profile in the Dockerfile
ENV SPRING_PROFILES_ACTIVE=prod

# Run Maven to build the JAR files (both order-service and product-service)
RUN mvn clean install -DskipTests

# Copy the application's JAR file to the container
COPY target/ordermanagement-product-service.jar ordermanagement-product-service.jar

# Expose the port your application runs on
EXPOSE 8081

# Run the application with the specified profile
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "ordermanagement-product-service.jar"]
