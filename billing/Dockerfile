# Use an official Tomcat runtime as a parent image
FROM openjdk:17

EXPOSE 8080

# Copy the webapp JAR file to the webapps directory
ADD /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
