# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Download Gradle dependencies
################################################################################
FROM eclipse-temurin:19-jdk-jammy AS deps

WORKDIR /build

# Copy Gradle wrapper and config
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Make wrapper executable
RUN chmod +x gradlew

# Download dependencies only to take advantage of Docker layer caching
# RUN ./gradlew build -x test --refresh-dependencies

################################################################################
# Stage 2: Build the application
################################################################################
FROM deps AS package

WORKDIR /build

# Copy source code
COPY src/ src/

# Build Spring Boot executable jar
RUN ./gradlew bootJar -x test

# Rename jar to app.jar for consistency
RUN cp build/libs/*.jar build/app.jar

################################################################################
# Stage 3: Extract Spring Boot layers
################################################################################
FROM package AS extract

WORKDIR /build

# Extract layered jar for caching
RUN java -Djarmode=layertools -jar build/app.jar extract --destination build/extracted

################################################################################
# Stage 4: Final runtime image
################################################################################
FROM eclipse-temurin:19-jre-jammy AS final

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

# Copy extracted layers
COPY --from=extract /build/build/extracted/dependencies/ ./
COPY --from=extract /build/build/extracted/spring-boot-loader/ ./
COPY --from=extract /build/build/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/build/extracted/application/ ./

# Expose default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-Dspring.profiles.active=UAT", "org.springframework.boot.loader.JarLauncher"]
