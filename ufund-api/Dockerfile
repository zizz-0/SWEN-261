# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the local Maven project into the container
COPY . /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Run Maven to build the application (this creates the JAR file)
RUN mvn clean package

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Command to run the application using the generated JAR file
CMD ["java", "-jar", "target/needs-api-0.0.1-SNAPSHOT.jar"]