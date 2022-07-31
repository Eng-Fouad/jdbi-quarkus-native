## Stage 1: build with gradle builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel:22.2-java17 AS stage1
USER root
RUN microdnf install findutils
COPY --chown=quarkus:quarkus gradle /code/gradle
COPY --chown=quarkus:quarkus gradlew /code/gradlew
COPY --chown=quarkus:quarkus build.gradle.kts /code/
COPY --chown=quarkus:quarkus settings.gradle.kts /code/
COPY --chown=quarkus:quarkus gradle.properties /code/
RUN chown quarkus:quarkus /code/
USER quarkus
WORKDIR /code/
# to cache dependencies layer
RUN ./gradlew build --no-daemon
COPY src /code/src
ARG GRADLE_BUILD_ARGS
RUN ./gradlew build --no-daemon ${GRADLE_BUILD_ARGS}

## Stage 2: create the docker final image
FROM quay.io/quarkus/quarkus-micro-image:1.0 AS stage2
WORKDIR /work/
COPY --from=stage1 /code/build/*-runner /work/application
RUN chmod 775 /work/
VOLUME /work/config/
EXPOSE 2030
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]