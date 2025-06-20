# 경량 JDK 17
FROM amazoncorretto:17-alpine-jdk

# curl 추가 (단독 4-5 MB)
RUN apk add --no-cache curl

# CI에서 빌드된 JAR 복사
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app.jar

# ENTRYPOINT — PROFILES 없으면 prod
ENTRYPOINT ["sh", "-c", "exec java -Dspring.profiles.active=${PROFILES:-prod} -Dserver.env=${ENV:-prod} -jar /app.jar"]