# Stage 1: Build the application
FROM gradle:8.5-jdk19 AS builder

# Set working directory
WORKDIR /app

# Copy project files
COPY --chown=gradle:gradle . .

# Grant execution permission to Gradle wrapper
RUN chmod +x ./gradlew

# Build the project (you can change `bootJar` to `build` if not using Spring Boot plugin)
RUN ./gradlew bootJar

# Stage 2: Run the application
FROM openjdk:19-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app-JamiiX-0.0.1-SNAPSHOT.jar

# Expose application port
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app-JamiiX-0.0.1-SNAPSHOT.jar"]