# Step 1: Use a base image that has OpenJDK 19
FROM openjdk:19-jdk-slim as builder

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the built JAR file from the build directory
COPY build/libs/*.jar app.jar

# Step 4: Expose the port the app will run on
EXPOSE 8080

# Step 5: Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]