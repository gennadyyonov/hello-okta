FROM eclipse-temurin:20-jdk-alpine AS build
WORKDIR /workspace/app
COPY . /workspace/app
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/app.jar)

FROM eclipse-temurin:20-jre-alpine
VOLUME /tmp
RUN addgroup -S hellookta && adduser -S hellookta -G hellookta
USER hellookta:hellookta
EXPOSE 8060
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","lv.gennadyyonov.hellookta.bff.ClientApplication"]