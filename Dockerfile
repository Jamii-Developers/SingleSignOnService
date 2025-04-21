# Stage 1: Build the app
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /home/app

# Copy everything and build the jar
COPY --chown=gradle:gradle . .
RUN gradle build -x test

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /home/app/build/libs/*.jar app.jar

# Expose port (change if needed)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]