FROM openjdk:21-jdk-slim

WORKDIR /app

COPY out/artifacts/effective_mobile_jar/effective_mobile.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]