FROM eclipse-temurin:11.0.18_10-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM eclipse-temurin:11

RUN mkdir -p /opt/arquitectura/logs/ms-story
VOLUME /opt/arquitectura/logs/ms-story

VOLUME C:/logs/tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV SERVER_URI "http://localhost:8888"
ENV SPRING_PROFILE "test"
ENV USERNAME_DB "postgres"
ENV PASSWORD_DB "mysecretpassword"
ENV URL_DB "jdbc:postgresql://localhost:5432/storyverse"
ENV URL_ZIPKIN "http://localhost:9411/"
ENV URL_EUREKA "http://localhost:8761/eureka/"
ENV URL_KEYCLOAK "http://localhost:8080"
ENV REALM_KEYCLOAK "software"
ENV CREDENTIALS_SECRET_KEYCLOAK "sJfdWcUkRXjnGHvkkRZ6q5ejibUEcPY7"


ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.msstory.MsStoryApplicationKt"]