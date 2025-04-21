# Stage 1: Build the application
FROM gradle:7.4.2-jdk19 as builder

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and the build.gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Give the gradlew script executable permissions
RUN chmod +x gradlew

# Run the Gradle build process to generate the JAR file
RUN ./gradlew build

# Stage 2: Run the application
FROM openjdk:19-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
