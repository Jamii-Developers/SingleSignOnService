# Step 1: Use Gradle image to build the app
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copy everything and build the JAR
COPY . .
RUN gradle build -x test

# Step 2: Use a lightweight runtime image to run the JAR
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port defined in application.properties
EXPOSE 8080

# Environment variables (optional)
ENV JAVA_OPTS=""

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]