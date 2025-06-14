# 1️⃣ 경량 JDK 17 이미지
FROM amazoncorretto:17-alpine-jdk

# 2️⃣ CI에서 빌드된 JAR 복사
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app.jar

# 3️⃣ ENTRYPOINT — 빈 값이면 기본 prod 로
ENTRYPOINT sh -c 'exec java \
  -Dspring.profiles.active=${PROFILES:-prod} \
  -Dserver.env=${ENV:-prod} \
  -jar /app.jar'
