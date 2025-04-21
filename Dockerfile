# Stage 1: Build the application
FROM openjdk:19-jdk-slim as builder

# Install Gradle manually in the image
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-7.4.2-bin.zip -P /tmp
RUN unzip /tmp/gradle-7.4.2-bin.zip -d /opt
RUN ln -s /opt/gradle-7.4.2/bin/gradle /usr/bin/gradle

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

#Use OpenJDK 19 for the runtime environment

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
