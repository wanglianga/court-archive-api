FROM maven:3.8.6-openjdk-8-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=builder /app/target/court-archive-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
