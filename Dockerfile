FROM maven:3.9-amazoncorretto-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.6
WORKDIR /app
COPY --from=builder /app/target/keycloakxp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "app.jar"]