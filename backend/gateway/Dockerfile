FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

WORKDIR /app/unicos-core
RUN mvn clean install -DskipTests

WORKDIR /app/gateway
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/gateway/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]