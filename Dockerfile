FROM maven:3.9.9-amazoncorretto-8-alpine AS builder
WORKDIR /builder
COPY . /builder
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY --from=builder /builder/target/*.jar /app/server.jar
CMD ["java", "-jar", "server.jar"]