
# 1️⃣ 경량 JDK 17 기반 이미지
FROM amazoncorretto:17-alpine-jdk

# 2️⃣ CI에서 빌드된 JAR 복사
#     (예: target/demo-0.0.1-SNAPSHOT.jar)
ARG JAR_FILE=target/*.jar
ARG SPRING_PROFILES_ACTIVE
ARG ENV
COPY ${JAR_FILE} /app.jar

# 3️⃣ 런타임 옵션
#    ─ SPRING_PROFILES_ACTIVE : blue / green / local
#    ─ SERVER_ENV             : blue / green (헬스체크 용도)
#    ─ DATASOURCE_URL         : 컨테이너 내부 DB 주소
#
#    값은 docker-compose-blue.yml / -green.yml 쪽에
#    고정으로 적어두면 자동으로 치환됨.
#
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILES}","-Dserver.env=${ENV}","-jar","app.jar"]


