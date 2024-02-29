# Use a base image with Java installed
FROM openjdk:17

RUN mkdir /app

# Copy the server JAR to the container
COPY target/Kaplat_server_excercise-0.0.1-SNAPSHOT.jar /app/Kaplat_server_excercise.jar

# Set the working directory inside the container
WORKDIR /app

# Expose the port on which your server listens
EXPOSE 3769

# Set the entrypoint command to run the server JAR
CMD ["java", "-jar" ,"Kaplat_server_excercise.jar"]
