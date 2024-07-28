FROM openjdk:21-jdk-slim AS build

WORKDIR /foreign_exchange_service

COPY build.gradle settings.gradle ./
COPY src ./src
COPY gradlew ./
COPY gradle ./gradle

RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim

LABEL maintainer="Berkay Gungor"
LABEL version="1.0.0"
LABEL description="Foreign Exchange Service"

WORKDIR /foreign_exchange_service

COPY --from=build /foreign_exchange_service/build/libs/*.jar foreign_exchange_service.jar

EXPOSE 8080

VOLUME /data

HEALTHCHECK --interval=30s --timeout=30s --start-period=30s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "foreign_exchange_service.jar"]
