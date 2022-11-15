FROM openjdk:13.0.2

ADD build/libs/valtimo-backend.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]