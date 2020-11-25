FROM openjdk:8-jre-alpine3.9
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
EXPOSE 8070
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} app.jar
CMD ["/bin/sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]