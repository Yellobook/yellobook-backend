FROM openjdk:17

WORKDIR /app

EXPOSE 8080

# workflow 에서 빌드 컨텍스트를 프로젝트 루트로 설정해두었으므로 COPY 는 루트 기준으로 작성
ARG JAR_FILE="yellobook-api/build/libs/*.jar"
ARG PROFILE=dev

COPY ${JAR_FILE} yellobook-server.jar

ENV PROFILE=${PROFILE}

ENTRYPOINT ["java",
            "-javaagent:/pinpoint/pinpoint-bootstrap-2.5.1.jar",
            "-Dpinpoint.agentId=yellobook-agent",
            "-Dpinpoint.applicationName=yellobook-backend",
            "-jar",
            "-Dspring.profiles.active=${PROFILE}",
            "-Duser.timezone=Asia/Seoul",
            "yellobook-server.jar"]