# Use a Gradle image with JDK 19 to build the project
FROM gradle:8.5-jdk19 AS builder

WORKDIR /app

# Copy everything and build the jar
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Use a smaller runtime image for running the app
FROM openjdk:19-jdk-slim

WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port that Render will use (default is 8080)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]