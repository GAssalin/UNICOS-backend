# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS builder
ARG SERVICE_DIR
WORKDIR /build

COPY unicos-core /build/unicos-core
COPY ${SERVICE_DIR} /build/${SERVICE_DIR}

RUN mvn -f /build/unicos-core/pom.xml -DskipTests clean install \
 && mvn -f /build/${SERVICE_DIR}/pom.xml -DskipTests clean package

FROM eclipse-temurin:21-jre
ARG SERVICE_DIR
WORKDIR /app

RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# cria usuário não-root
RUN useradd -r -u 10001 -g root appuser

COPY --from=builder /build/${SERVICE_DIR}/target/*.jar /app/app.jar

USER 10001
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]