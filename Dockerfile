FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/tracking-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
