# 베이스 이미지로 OpenJDK 사용
FROM openjdk:17-jdk-slim

# JAR 파일을 애플리케이션으로 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app.jar"]
