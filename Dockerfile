FROM maven:3.9.9-eclipse-temurin-21-alpine as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/api-0.0.1-SNAPSHOT.jar api-cloudnrg.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-cloudnrg.jar"]