FROM eclipse-temurin:17-jdk as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV GEMINI_API_KEY=your_api_key
ENV SPRING_DATASOURCE_URL=jdbc:h2:file:/data/geminidb

# Create data directory for persistent H2 database
RUN mkdir -p /data
VOLUME /data

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "-Dgemini.api.key=${GEMINI_API_KEY}", "com.project.wrappergemini.WrapperGeminiApplication"] 