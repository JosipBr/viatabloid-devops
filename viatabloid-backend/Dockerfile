# Use a lightweight OpenJDK 21 base image for Java applications
FROM openjdk:21-jdk-slim

WORKDIR /app

ARG JAR_FILE=target/viatabloid-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Explicitly define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]