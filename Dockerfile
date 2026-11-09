# =========================================================
# Etapa 1: compilación con Maven
# =========================================================
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Descarga dependencias primero para aprovechar la caché de capas
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests \
    && cp target/*.jar app.jar

# =========================================================
# Etapa 2: imagen de ejecución (solo JRE, usuario sin privilegios)
# =========================================================
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
COPY --from=build --chown=app:app /app/app.jar app.jar
USER app

EXPOSE 8081

# Configurable en tiempo de ejecución:
#   SPRING_DATASOURCE_URL, RESERVAS_DB_USERNAME, RESERVAS_DB_PASSWORD
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
