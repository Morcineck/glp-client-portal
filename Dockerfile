FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/client-portal-0.0.1-SNAPSHOT.jar /app/cliente-portal.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/cliente-portal.jar"]