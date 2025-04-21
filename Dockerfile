# Step 1: Use a base image that has OpenJDK 19
FROM openjdk:19-jdk-slim as builder

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the entire project to the container
COPY . .

# Step 4: Give gradlew executable permissions
RUN chmod +x gradlew

# Step 5: Run the Gradle build process to create the JAR
RUN ./gradlew build

# Debug step: List the directory contents to ensure the JAR file is being generated
RUN ls -alh /app/build/libs

# Step 6: Copy the built JAR file into the app directory
# Copy the generated JAR file (adjust the path if necessary)
COPY --from=builder /app/build/libs/app-JamiiX-0.0.1-SNAPSHOT.jar /app/app-JamiiX-0.0.1-SNAPSHOT.jar

# Step 7: Expose the port the app will run on
EXPOSE 8080

# Step 8: Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app-JamiiX-0.0.1-SNAPSHOT.jar"]
