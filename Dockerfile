# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Cache Gradle dependencies
################################################################################
FROM eclipse-temurin:25-jdk AS deps

WORKDIR /build

# Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew

# Download dependencies and populate Gradle cache
RUN ./gradlew dependencies --no-daemon

################################################################################
# Stage 2: Build Spring Boot application
################################################################################
FROM deps AS build

WORKDIR /build

# Application source
COPY src/ src/

# Build executable Spring Boot jar
RUN ./gradlew bootJar -x test --no-daemon

################################################################################
# Stage 3: Runtime image
################################################################################
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy generated jar
COPY --from=build /build/build/libs/*.jar app.jar

EXPOSE 8080

# UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "-jar", "app.jar"]