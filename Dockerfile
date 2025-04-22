# Stage 1: Build using Gradle with JDK 17
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Stage 2: Run using a slim JDK 17 image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]