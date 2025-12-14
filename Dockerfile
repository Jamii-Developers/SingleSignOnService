# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Cache dependencies
################################################################################
FROM eclipse-temurin:21-jdk-jammy AS deps

WORKDIR /build

# Copy Gradle wrapper and config files first (so dependencies are cached)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

# Pre-download dependencies without building
RUN ./gradlew build -x test --refresh-dependencies --no-daemon --stacktrace

################################################################################
# Stage 2: Build Spring Boot application
################################################################################
FROM deps AS build

WORKDIR /build

# Copy source code
COPY src/ src/

# Build executable bootJar (skip tests for speed)
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

################################################################################
# Stage 3: Runtime image
################################################################################
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Copy the bootJar from build stage
COPY --from=build /build/build/libs/app-0.0.1.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application with UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "-jar", "app.jar"]
