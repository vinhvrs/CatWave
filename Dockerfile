# Dockerfile

FROM openjdk:21
WORKDIR /app
COPY demo/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 1212
ENTRYPOINT ["java", "-jar", "app.jar"]
