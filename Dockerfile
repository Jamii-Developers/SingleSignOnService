# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Download Gradle dependencies (optimized for CI/CD)
################################################################################
FROM eclipse-temurin:21-jdk-jammy AS deps

WORKDIR /build

# Copy Gradle wrapper and configuration files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Ensure wrapper is executable
RUN chmod +x gradlew

# Download dependencies only to take advantage of Docker caching
# --no-daemon prevents Gradle daemon issues in CI/CD environments
# --stacktrace gives better error logs
RUN ./gradlew build -x test --refresh-dependencies --no-daemon --stacktrace

################################################################################
# Stage 2: Build the Spring Boot application
################################################################################
FROM deps AS package

WORKDIR /build

# Copy application source code
COPY src/ src/

# Build the executable Spring Boot jar, skip tests, disable daemon
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

# Rename the jar to app.jar for consistency
RUN cp build/libs/*.jar build/app.jar

################################################################################
# Stage 3: Extract Spring Boot layers for caching
################################################################################
FROM package AS extract

WORKDIR /build

# Extract layered jar
RUN java -Djarmode=layertools -jar build/app.jar extract --destination build/extracted

################################################################################
# Stage 4: Final runtime image (optimized slim)
################################################################################
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

WORKDIR /app

# Copy only the necessary layers
COPY --from=extract /build/build/extracted/dependencies/ ./
COPY --from=extract /build/build/extracted/spring-boot-loader/ ./
COPY --from=extract /build/build/extracted/application/ ./
# snapshot-dependencies layer omitted to reduce image size

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot application with UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "org.springframework.boot.loader.JarLauncher"]
