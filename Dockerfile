# 베이스 이미지로 OpenJDK 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 필요한 파일 복사
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 캐시를 위해 빌드 실행 (선택 사항)
RUN chmod +x gradlew && ./gradlew build --exclude-task bootJar

# 소스 코드 복사
COPY src ./src

# 포트 설정
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["./gradlew", "bootRun"]
