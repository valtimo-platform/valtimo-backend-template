FROM openjdk:17.0.2-bullseye

ADD /libs/*.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]