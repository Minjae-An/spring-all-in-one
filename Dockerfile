FROM openjdk:17-jdk-alpine AS builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17-jdk-alpine
COPY --from=builder build/libs/*.jar /home/server.jar
ENTRYPOINT ["java", "-jar", "/home/server.jar", "--spring.profiles.active=dev"]