FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

WORKDIR /app/unicos-core
RUN mvn clean install -DskipTests

WORKDIR /app/ms-pessoas
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/ms-pessoas/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]