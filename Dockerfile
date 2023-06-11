# Use a base image with Java installed
FROM openjdk:17

RUN mkdir /app

# Copy the server JAR to the container
COPY out/artifacts/Kaplat_server_excercise_jar/Kaplat_server_excercise.jar /app/Kaplat_server_excercise.jar

# Set the working directory inside the container
WORKDIR /app

# Expose the port on which your server listens
EXPOSE 3769

# Set the entrypoint command to run the server JAR
CMD ["java", "-jar" ,"Kaplat_server_excercise.jar"]
