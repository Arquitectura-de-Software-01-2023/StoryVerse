FROM eclipse-temurin:11.0.18_10-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:11



VOLUME C:/logs/tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV SERVER_URI "http://localhost:8282"
ENV SPRING_PROFILE "local"
ENV USERNAME_DB "postgres"
ENV PASSWORD_DB "password"
ENV URL_DB "jdbc:postgresql://localhost:5432/storyverse"
ENV URL_ZIPKIN "http://localhost:9411/"
ENV URL_EUREKA "http://localhost:8761/eureka/"
ENV URL_KEYCLOAK "http://localhost:8080"
ENV REALM_KEYCLOAK "software"
ENV CREDENTIALS_SECRET_KEYCLOAK "sJfdWcUkRXjnGHvkkRZ6q5ejibUEcPY7"
ENV CLIENT_KEYCLOAK "backend"


ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.msuser.MsUserApplicationKt"]


