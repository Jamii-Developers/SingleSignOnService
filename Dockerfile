# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Build the application
################################################################################
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /build

# Copy Gradle wrapper and project files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./
COPY src/ src/

RUN chmod +x gradlew

# Build Spring Boot executable jar (skip tests for faster builds)
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

################################################################################
# Stage 2: Runtime image
################################################################################
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Copy the bootJar from build stage
COPY --from=build /build/build/libs/app-0.0.1.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application with UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "-jar", "app.jar"]
