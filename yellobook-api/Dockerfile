FROM openjdk:17

WORKDIR /app

EXPOSE 8080

ARG JAR_FILE=build/libs/*.jar
ARG PROFILE=dev

COPY ${JAR_FILE} yellobook-server.jar

ENV PROFILE=${PROFILE}

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "yellobook-server.jar"]