# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Pre-download Gradle dependencies
################################################################################
FROM eclipse-temurin:21-jdk-jammy AS deps

# Cache-busting ARG: increment to force rebuild
ARG CACHEBUST=1

WORKDIR /build

# Copy Gradle wrapper and configuration
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Ensure gradlew is executable
RUN chmod +x gradlew

# Dummy cache-busting step
RUN echo "Cache bust value: $CACHEBUST"

# Pre-download dependencies (skip tests)
RUN ./gradlew build -x test --refresh-dependencies --no-daemon --stacktrace --info

################################################################################
# Stage 2: Build Spring Boot application
################################################################################
FROM deps AS package

WORKDIR /build

# Copy source code
COPY src/ src/

# Build Spring Boot jar, skip tests
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

################################################################################
# Stage 3: Extract Spring Boot layers
################################################################################
FROM package AS extract

WORKDIR /build

# Extract layered jar directly from bootJar output
RUN java -Djarmode=layertools -jar build/libs/*.jar extract --destination build/extracted

################################################################################
# Stage 4: Final runtime image
################################################################################
FROM eclipse-temurin:21-jre-jammy AS final

# Create non-root user
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

# Copy only necessary Spring Boot layers
COPY --from=extract /build/build/extracted/dependencies/ ./
COPY --from=extract /build/build/extracted/spring-boot-loader/ ./
COPY --from=extract /build/build/extracted/application/ ./

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application with UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "org.springframework.boot.loader.JarLauncher"]
