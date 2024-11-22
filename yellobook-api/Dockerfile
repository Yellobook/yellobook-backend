FROM openjdk:17

WORKDIR /app

EXPOSE 8080

ARG JAR_FILE="yellobook-api/build/libs/*.jar"
ARG PROFILE=dev

COPY ${JAR_FILE} yellobook-server.jar

ENV PROFILE=${PROFILE}

ENTRYPOINT ["java", \
            "-javaagent:/pinpoint/pinpoint-bootstrap-2.5.1.jar", \
            "-Dpinpoint.agentId=yellobook-agent", \
            "-Dpinpoint.applicationName=yellobook-backend", \
            "-jar", \
            "-Dspring.profiles.active=${PROFILE}", \
            "-Duser.timezone=Asia/Seoul", \
            "yellobook-server.jar"]