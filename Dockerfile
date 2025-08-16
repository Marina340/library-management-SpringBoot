# Use a lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built jar into the container
COPY ../target/Mendix-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java","-jar","app.jar"]
