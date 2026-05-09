# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew \
    && ./gradlew bootJar -x test -x check --no-daemon

RUN JAR="$(find build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' -print -quit)" \
    && test -n "$JAR" \
    && cp "$JAR" /application.jar


FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /application.jar ./application.jar

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT exec java "-Dserver.port=${PORT:-8080}" ${JAVA_OPTS} -jar ./application.jar
