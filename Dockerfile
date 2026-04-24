# ---------- BUILD STAGE ----------
FROM maven:3.9.14-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn -B -q -e -DskipTests dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:25-jdk-noble

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Run
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "app.jar"]