FROM eclipse-temurin:11.0.18_10-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:11

#RUN mkdir -p /opt/arquitectura/logs
#VOLUME /opt/arquitectura/logs

VOLUME C:/logs/tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV SERVER_URI "http://localhost:8888"
ENV SPRING_PROFILE "test"

ENTRYPOINT ["java","-cp","app:app/lib/*","ucb.arqsoft.msgateway.MsGatewayApplicationKt"]