# Etapa 1: compilación
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -ntp dependency:go-offline
COPY src src
# Las pruebas ya corren en el job "build" del workflow.
RUN ./mvnw -B -ntp package -DskipTests
# Separa el JAR en capas (dependencias / loader / snapshots / código) para que un cambio
# de código solo invalide la última capa de la imagen.
RUN cp target/*.jar app.jar \
    && java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# Etapa 2: ejecución (solo JRE, usuario sin privilegios)
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN groupadd --system --gid 10001 app && useradd --system --uid 10001 --gid app app
# De la capa que menos cambia a la que más.
COPY --from=build /app/extracted/dependencies/ ./
COPY --from=build /app/extracted/spring-boot-loader/ ./
COPY --from=build /app/extracted/snapshot-dependencies/ ./
COPY --from=build /app/extracted/application/ ./
USER 10001
# La app usa 8081 por defecto (application.properties); en el contenedor se fija 8080.
ENV SERVER_PORT=8080
# Heap acotado al límite de memoria del contenedor (por defecto la JVM usa solo el 25 %).
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
