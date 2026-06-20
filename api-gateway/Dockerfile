# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /build
COPY pom.xml /build/pom.xml
COPY api-gateway/pom.xml /build/api-gateway/pom.xml
COPY user-service/pom.xml /build/user-service/pom.xml
COPY resume-service/pom.xml /build/resume-service/pom.xml
COPY interview-service/pom.xml /build/interview-service/pom.xml
COPY feedback-service/pom.xml /build/feedback-service/pom.xml
COPY api-gateway/pom.xml /build/api-gateway/pom.xml
RUN mvn -q -pl api-gateway -am dependency:go-offline || true
COPY api-gateway/src /build/api-gateway/src
RUN mvn -q -pl api-gateway -am clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:22-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /build/api-gateway/target/*.jar /app/app.jar
RUN chown -R app:app /app
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
