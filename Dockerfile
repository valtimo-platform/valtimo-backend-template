FROM openjdk:17-bullseye

ADD /libs/*.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]