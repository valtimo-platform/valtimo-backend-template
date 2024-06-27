FROM openjdk:17-bullseye

ADD build/libs/valtimo-backend-template.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]