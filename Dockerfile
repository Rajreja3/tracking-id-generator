# -------- 1. Build stage --------
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- 2. Runtime stage --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built JAR into runtime image
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]