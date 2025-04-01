# Etapa de construcción
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno
ENV GEMINI_API_KEY=your_api_key
ENV SPRING_DATASOURCE_URL=jdbc:h2:file:/data/geminidb
ENV DB_USERNAME=sa
ENV DB_PASSWORD=password

# Crear directorio para la base de datos
RUN mkdir -p /data
VOLUME /data

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"] 