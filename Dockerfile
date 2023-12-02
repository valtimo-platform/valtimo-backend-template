FROM openjdk:21-bullseye

ADD /libs/*.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]