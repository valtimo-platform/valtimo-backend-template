FROM eclipse-temurin:17-jre-noble

ADD build/libs/valtimo-backend-template.war /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]