# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Pre-download Gradle dependencies
################################################################################
FROM eclipse-temurin:21-jdk-jammy AS deps

WORKDIR /build

# Copy Gradle wrapper and configs
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

# Pre-download dependencies (skip tests)
RUN ./gradlew build -x test --refresh-dependencies --no-daemon --stacktrace --info

################################################################################
# Stage 2: Build Spring Boot application
################################################################################
FROM deps AS package

WORKDIR /build

# Copy source code
COPY src/ src/

# Build bootJar
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

################################################################################
# Stage 3: Extract Spring Boot layers
################################################################################
FROM package AS extract

WORKDIR /build

# Extract layered boot jar (executable jar)
RUN java -Djarmode=layertools -jar build/libs/*-boot.jar extract --destination build/extracted

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

# Copy Spring Boot layers
COPY --from=extract /build/build/extracted/dependencies/ ./
COPY --from=extract /build/build/extracted/spring-boot-loader/ ./
COPY --from=extract /build/build/extracted/application/ ./

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application with UAT profile
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "org.springframework.boot.loader.JarLauncher"]
