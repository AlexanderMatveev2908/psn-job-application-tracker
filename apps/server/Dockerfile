# ? build app 🛠️
FROM gradle:8.10.2-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew bootJar --no-daemon

# ? run app 🔥
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY --from=builder /app/app/build/libs/server-1.0.0.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]