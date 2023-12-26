FROM maven:3.8.2-openjdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11-ea-11-jdk-slim
COPY --from=build /target/employee-dir-0.0.1-SNAPSHOT.jar employee-dir-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/employee-dir-0.0.1-SNAPSHOT.jar"]