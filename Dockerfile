# Stage 1: Build the application
FROM gradle:8.5-jdk19 AS builder

# Set working directory
WORKDIR /app

# Copy everything and give permissions
COPY . .
RUN chmod +x ./gradlew

# Build the Spring Boot app (change `build` to `bootJar` if you use Spring Boot)
RUN ./gradlew build

# Stage 2: Create the final image
FROM openjdk:19-jdk-slim

WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]