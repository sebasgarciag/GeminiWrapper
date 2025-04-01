# Spring Boot Gemini AI Wrapper by Sebastian Garcia

Una aplicación Spring Boot que sirve como wrapper para Google Gemini AI, proporcionando una interfaz de usuario para interactuar con Gemini AI, almacenar el historial de conversaciones y gestionar las respuestas de la API.

## Capacidad de la app

- Interfaz de usuario moderna profesional y una estilo cyberpunk
- Conexión a la API de Google Gemini AI
- Almacenamiento del historial de conversaciones en base de datos H2
- Gestión segura de tokens API mediante variables de entorno
- Modo oscuro/claro y visualizador de base de datos incorporado

## Requisitos Previos

### Software Necesario
- **Java Development Kit (JDK):**
  - Versión: 17 o superior
  - [Descargar JDK](https://adoptium.net/)
  - Verificar instalación: `java -version`

- **Maven:**
  - Gestor de dependencias y construcción
  - [Descargar Maven](https://maven.apache.org/download.cgi)
  - Verificar instalación: `mvn -version`
  - Alternativa: Usar el Maven wrapper incluido (`./mvnw`)

- **Docker (Opcional):**
  - Necesario solo para ejecución contenerizada
  - [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop)
  - Verificar instalación: `docker --version`
  - Componentes requeridos:
    - Docker Engine
    - Docker Compose
    - Verificar: `docker-compose --version`

### Credenciales y Configuración
- **Cuenta de Google AI Studio:**
  - Registrarse en [Google AI Studio](https://makersuite.google.com/app/apikey)
  - Obtener una clave API de Gemini
  - La clave es necesaria para interactuar con la API

### Requisitos de Sistema
- **Espacio en Disco:**
  - Mínimo: 500MB para la aplicación
  - Recomendado: 1GB para la aplicación y datos

- **Memoria RAM:**
  - Mínimo: 2GB disponibles
  - Recomendado: 4GB o más

- **Puertos:**
  - Puerto 8080 disponible (configurable)
  - Asegurarse de que no esté en uso por otras aplicaciones

## Configuración Inicial

1. Clonar este repositorio:
   ```bash
   git clone https://github.com/sebasgarciag/GeminiWrapper.git
   cd wrapperGemini
   ```

2. Configurar variables de entorno:
   - Copia el archivo de ejemplo `.env.example` a `.env`:
     ```bash
     cp .env.example .env
     ```
   - Edita el archivo `.env` y configura las variables:
     ```properties
     # API de Gemini (Requerido)
     GEMINI_API_KEY=tu_clave_api_aquí
     GEMINI_MODEL=gemini-2.0-flash
     
     # Base de Datos H2 (Opcional)
     DB_USERNAME=sa          # Usuario por defecto si no se especifica
     DB_PASSWORD=password    # Contraseña por defecto si no se especifica
     DB_PATH=./data/geminidb  # Ruta por defecto para la base de datos
     ```

3. **Importante:** 
   - El archivo `.env` está incluido en `.gitignore` para proteger tus credenciales
   - Nunca subas este archivo al control de versiones
   - Cambia las credenciales por defecto en entornos de producción

## Formas de Ejecutar la Aplicación

### 1. Ejecución Local (Sin Docker)

Esta es la forma más directa de ejecutar la aplicación durante el desarrollo:

1. **Compilar la aplicación:**
   ```bash
   ./mvnw clean package
   ```

2. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acceder a la aplicación:**
   - Interfaz principal: `http://localhost:8080`
   - Consola H2: `http://localhost:8080/h2-console`
     - URL JDBC: `jdbc:h2:file:./data/geminidb`
     - Usuario: `sa`
     - Contraseña: `password`

### 2. Ejecución con Docker

#### Opción A: Usando Docker Compose (Recomendado)
Esta es la forma más sencilla de ejecutar la aplicación contenerizada:

1. **Iniciar la aplicación:**
   ```bash
   docker-compose up -d
   ```

2. **Detener la aplicación:**
   ```bash
   docker-compose down
   ```

#### Opción B: Usando Docker directamente
Si prefieres más control sobre la configuración:

1. **Construir la imagen:**
   ```bash
   docker build -t wrapper-gemini .
   ```

2. **Ejecutar el contenedor:**
   ```bash
   docker run -p 8080:8080 --env-file .env wrapper-gemini
   ```

### Acceso a la Aplicación (Ambos Métodos)

Una vez iniciada la aplicación (por cualquier método):

1. **Interfaz Principal:**
   - URL: `http://localhost:8080`
   - Disponible inmediatamente después del inicio

2. **Consola de Base de Datos:**
   - URL: `http://localhost:8080/h2-console`
   - Credenciales:
     ```
     URL JDBC: jdbc:h2:file:/data/geminidb
     Usuario: sa
     Contraseña: password
     ```

### Consideraciones Importantes

- **Ejecución Local:**
  - Los datos se almacenan en `./data/geminidb`
  - La consola H2 solo acepta conexiones locales
  - Ideal para desarrollo y pruebas

- **Ejecución con Docker:**
  - Los datos persisten en el volumen `./data:/data`
  - La consola H2 está configurada para aceptar conexiones desde el host
  - Recomendado para entornos de prueba y preproducción

## Containerización con Docker

1. Construir la imagen Docker:
   ```
   docker build -t wrapper-gemini .
   ```

2. Ejecutar el contenedor Docker:
   ```
   docker run -p 8080:8080 -e GEMINI_API_KEY=tu_clave_api_aquí -e GEMINI_MODEL=gemini-2.0-flash wrapper-gemini
   ```

## Docker Compose y Persistencia de Datos

Para una gestión más robusta de la aplicación en Docker, el proyecto incluye configuración con Docker Compose. Este método ofrece varias ventajas:
- Gestión más sencilla de la configuración
- Persistencia automática de datos
- Manejo seguro de variables de entorno
- Facilidad para escalar y mantener

### Uso de Docker Compose

El proyecto ya incluye un archivo `docker-compose.yml` configurado y listo para usar. Para ejecutar la aplicación:

1. **Iniciar la aplicación:**
   ```bash
   docker-compose up -d    # El flag -d ejecuta en modo detached (background)
   ```

2. **Detener la aplicación:**
   ```bash
   docker-compose down
   ```

El archivo `docker-compose.yml` incluido está configurado con:
- Construcción automática de la imagen
- Mapeo del puerto 8080
- Carga de variables desde el archivo `.env`
- Persistencia de datos mediante volúmenes

### Persistencia de Datos

La configuración de volúmenes (`volumes`) en el docker-compose.yml permite que los datos persistan entre reinicios del contenedor:

- **¿Qué significa persistir datos?**
  - Los datos sobreviven aunque el contenedor se detenga o elimine
  - La información se almacena en tu máquina local
  - No hay pérdida de datos al actualizar la aplicación
  - Facilita la realización de backups

- **¿Cómo funciona?**
  - `./data:/data` crea un "puente" entre:
    - `./data`: Directorio en tu máquina local
    - `/data`: Directorio dentro del contenedor
  - Todos los datos de la base H2 se guardan en tu máquina
  - Al reiniciar el contenedor, los datos están disponibles automáticamente

- **Configuración de la ruta de la base de datos**:
  - La aplicación permite configurar la ubicación de la base de datos H2 mediante la variable `DB_PATH`
  - Esta variable puede establecerse en el archivo `.env`
  - Los valores por defecto son:
    - Ejecución local: `./data/geminidb`
    - Docker: `/data/geminidb`
  - Esta flexibilidad permite:
    - Cambiar la ubicación de la base de datos sin modificar el código
    - Usar diferentes bases de datos para diferentes entornos
    - Facilitar copias de seguridad apuntando a directorios específicos
    - Compartir la base de datos entre múltiples instancias

- **Beneficios:**
  - Seguridad: los datos están separados del contenedor
  - Facilidad de backup: solo hay que copiar el directorio local
  - Portabilidad: puedes mover los datos entre diferentes instalaciones
  - Desarrollo consistente: mismo conjunto de datos entre reinicios

### Acceso a la Consola H2 en Docker

Cuando la aplicación se ejecuta en Docker, hay algunas consideraciones especiales para acceder a la consola H2:

- Las conexiones desde tu navegador al contenedor Docker son consideradas "remotas" por H2, incluso cuando accedes a través de localhost.
- Por esta razón, la aplicación tiene configurado `spring.h2.console.settings.web-allow-others=true` en el archivo `application.properties`.
- Esta configuración es necesaria específicamente para el escenario de Docker y permite que puedas acceder a la consola H2 desde tu navegador al contenedor.

Para acceder a la consola H2 cuando la aplicación está en Docker:
1. Navega a `http://localhost:8080/h2-console`
2. Usa los siguientes datos de conexión:
   - URL JDBC: `jdbc:h2:file:/data/geminidb`
   - Usuario: `sa`
   - Contraseña: `password`

**Nota de Seguridad**: 
- Si estás ejecutando la aplicación localmente sin Docker (usando `./mvnw spring-boot:run`), debes cambiar en `application.properties`:
  ```properties
  spring.h2.console.settings.web-allow-others=false
  ```
  Esto es más seguro ya que solo permitirá conexiones locales a la consola H2.

- La configuración `web-allow-others=true` solo debe usarse cuando:
  1. Se ejecuta la aplicación en Docker
  2. Se necesita acceso remoto a la consola H2 (no recomendado en producción)

- Para entornos de producción, se recomienda:
  1. Deshabilitar completamente la consola H2 (`spring.h2.console.enabled=false`)
  2. Utilizar una base de datos más robusta como MySQL o PostgreSQL
  3. Implementar medidas de seguridad adicionales como VPN o túneles SSH para acceso a la base de datos

## Consideraciones de Seguridad

- La clave API se carga desde un archivo `.env` que no debe incluirse en el control de versiones
- El archivo `.gitignore` ya está configurado para excluir el archivo `.env`
- La base de datos H2 está configurada con un usuario y contraseña predeterminados

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/project/wrappergemini/
│   │   ├── controller/       # Controladores de la aplicación
│   │   │   └── GeminiController.java
│   │   ├── model/           # Entidades y DTOs
│   │   │   ├── Conversation.java
│   │   │   ├── ConversationDTO.java
│   │   │   ├── GeminiRequest.java
│   │   │   └── GeminiResponse.java
│   │   ├── repository/      # Repositorios de datos
│   │   │   └── ConversationRepository.java
│   │   ├── service/         # Lógica de negocio
│   │   │   └── GeminiService.java
│   │   └── WrapperGeminiApplication.java
│   └── resources/
│       ├── static/          # Recursos estáticos (CSS, JS, imágenes)
│       │   ├── css/
│       │   │   └── styles.css
│       │   └── js/
│       │       └── main.js
│       ├── templates/       # Plantillas Thymeleaf
│       │   ├── index.html
│       │   └── error.html
│       └── application.properties
```

## Tecnologías Utilizadas

- **Spring Boot 3.x**: Framework elegido por su facilidad de configuración, amplia comunidad y excelente integración con otras tecnologías Java. Permite un desarrollo rápido y productivo con configuración mínima.

- **Spring Data JPA**: Facilita el acceso a datos mediante un nivel de abstracción que simplifica las operaciones CRUD y reduce el código repetitivo, mejorando la mantenibilidad.

- **H2 Database**: Base de datos relacional embebida, seleccionada por su capacidad para funcionar en memoria o en archivo sin instalación adicional. Ideal para este proyecto porque:
  - No requiere un servidor de base de datos separado
  - Se inicia rápidamente con la aplicación
  - Proporciona una consola web integrada para administración
  - Permite persistencia en archivo para mantener datos entre reinicios
  - Perfecta para aplicaciones de demostración, desarrollo y pruebas
  - Menor complejidad de configuración que MySQL, PostgreSQL u otras bases de datos relacionales tradicionales

- **Thymeleaf**: Motor de plantillas seleccionado por su integración nativa con Spring y capacidad para crear prototipos HTML que pueden verse sin servidor, facilitando el desarrollo frontend.

- **Spring Web**: Proporciona las dependencias necesarias para crear aplicaciones web con Spring MVC, incluyendo servlets, servidores web y configuración básica.

- **Spring Boot DevTools**: Herramientas de desarrollo que mejoran la experiencia de desarrollo con:
  - Recarga automática de cambios
  - Reinicio automático de la aplicación
  - Configuración de propiedades por defecto optimizadas para desarrollo

- **Spring Dotenv**: Permite cargar variables de entorno desde archivos `.env`, facilitando la gestión de configuraciones sensibles y específicas del entorno.

- **Lombok**: Reduce el código boilerplate mediante anotaciones que generan automáticamente getters, setters, constructores y otros métodos comunes.

- **JUnit y Mockito**: Frameworks de testing que permiten:
  - Escritura de pruebas unitarias
  - Mocking de dependencias
  - Verificación de comportamiento
  - Cobertura de código

- **JaCoCo**: Herramienta de análisis de cobertura de código para Java que:
  - Genera informes detallados de cobertura
  - Permite identificar código no probado
  - Integra métricas de calidad en el proceso de build
  - Ofrece visualizaciones intuitivas mediante informes HTML

- **Bootstrap 5**: Framework CSS utilizado para obtener una interfaz responsiva y moderna con mínimo esfuerzo, proporcionando componentes prediseñados y un sistema de rejilla flexible.

- **Font Awesome**: Biblioteca de iconos vectoriales que mejora la experiencia visual y la usabilidad de la interfaz con símbolos reconocibles e intuitivos.

- **API de Google Gemini**: Motor de IA seleccionado por su potente capacidad para generar texto coherente y contextualmente relevante, permitiendo una interacción natural con los usuarios.

## Mejoras Recientes

### 1. Pruebas Unitarias
- Se han agregado pruebas unitarias para los componentes principales:
  - `GeminiServiceTest`: Prueba la lógica de negocio y la integración con la API
  - `GeminiControllerTest`: Prueba los endpoints y el manejo de solicitudes
  - `WrapperGeminiApplicationTests`: Prueba la carga correcta del contexto de Spring
- Las pruebas cubren:
  - Validación de entrada
  - Manejo de errores
  - Integración con la base de datos
  - Respuestas de la API
- Se ha implementado JaCoCo para análisis de cobertura de código
- Todos los tests están documentados con Javadoc

### 2. Implementación de DTOs
- Creación de tres DTOs principales para mejorar la arquitectura:
  - `ConversationDTO`: Para transferir datos de conversaciones
  - `GeminiRequest`: Para estructurar peticiones a la API
  - `GeminiResponse`: Para deserializar respuestas de la API
- Mejora en la organización del código con separación clara de responsabilidades
- Configuración de ObjectMapper para manejar propiedades desconocidas
- Documentación completa de todos los DTOs

### 3. Validación de Tamaño de Solicitud
- Se ha implementado un límite máximo de caracteres para las preguntas
- El límite actual es de 30,000 caracteres
- Se proporcionan mensajes de error claros cuando se excede el límite
- Mejora la estabilidad y previene errores en la API

### 4. Optimización del Dockerfile
- Implementación de multi-stage build para reducir el tamaño final de la imagen
- Uso de la imagen oficial de Maven para la construcción
- Imagen base más ligera (eclipse-temurin:17-jre-alpine)
- Mejor manejo de dependencias y caché de Maven
- Configuración más limpia y mantenible

### 5. Gestión Mejorada de Configuración
- Todas las configuraciones sensibles ahora usan variables de entorno
- Valores por defecto seguros para todas las configuraciones
- Mejor documentación de las variables de entorno
- Introducción de la variable `DB_PATH` para configurar la ubicación de la base de datos
- Mayor flexibilidad para diferentes entornos de despliegue

### 6. Manejo de Errores
- Mejora en la captura y gestión de excepciones
- Mensajes de error más descriptivos y útiles
- Validación robusta de entrada de usuario
- Manejo graceful de errores de la API de Gemini
- Configuración del ObjectMapper para ignorar propiedades desconocidas en respuestas API

### 7. Documentación Mejorada
- Documentación exhaustiva de código con Javadoc
- Ampliación del README con detalles de implementación
- Sección de pruebas detallada
- Documentación de la arquitectura en capas y patrón DTO
- Traducción de documentación a español
- Documentación mejorada para desarrolladores

## Testing y Control de Calidad

### Ejecución de Tests

Los tests se pueden ejecutar de varias formas:

1. **Usando Maven desde la terminal:**
   ```bash
   # Ejecutar todos los tests
   ./mvnw test

   # Ejecutar tests con cobertura
   ./mvnw test jacoco:report

   # Ejecutar un test específico
   ./mvnw test -Dtest=GeminiControllerTest

   # Ejecutar un método de test específico
   ./mvnw test -Dtest=GeminiControllerTest#testAskGemini
   ```

2. **Desde el IDE (IntelliJ IDEA):**
   - Click derecho en la carpeta `src/test/java`
   - Seleccionar "Run Tests in 'wrapperGemini'"
   - O para un test específico:
     - Click derecho en la clase de test
     - Seleccionar "Run '[TestClassName]'"
     - O para un método específico:
       - Click en el ícono verde junto al método
       - Seleccionar "Run '[testMethodName]()'"

3. **Durante el build:**
   ```bash
   # Los tests se ejecutan automáticamente al hacer build
   ./mvnw clean package
   ```

### Estructura de los Tests

El proyecto incluye tests unitarios para los componentes principales:

1. **GeminiControllerTest:**
   - `testHomePage()`: Verifica la carga correcta de la página principal
   - `testAskGemini()`: Verifica el procesamiento de preguntas

2. **GeminiServiceTest:**
   - `testGenerateResponse_ValidInput()`: Verifica respuestas válidas
   - `testGenerateResponse_EmptyInput()`: Verifica manejo de entradas vacías
   - `testGenerateResponse_NullInput()`: Verifica manejo de entradas nulas
   - `testGetConversationHistory()`: Verifica recuperación del historial

### Patrón de Testing

Los tests siguen el patrón AAA (Arrange-Act-Assert):

1. **Arrange:** Prepara los datos y mocks necesarios
2. **Act:** Ejecuta la acción a probar
3. **Assert:** Verifica que el resultado es el esperado

### Cobertura de Tests

Para ver la cobertura de tests:
1. Ejecuta `./mvnw test jacoco:report`
2. Abre `target/site/jacoco/index.html` en tu navegador 

### Resultados Detallados de los Tests

Maven almacena los resultados detallados de la ejecución de tests en el directorio `target/surefire-reports`. Estos archivos contienen información detallada sobre cada test ejecutado, incluyendo:

- **Archivos XML**: Contienen los resultados en formato XML para integración con herramientas CI/CD
  - Ubicación: `target/surefire-reports/*.xml`
  
- **Archivos de Texto**: Proporcionan un resumen legible de cada test ejecutado
  - Ubicación: `target/surefire-reports/*.txt`
  
- **Información de Fallos**: Si un test falla, estos archivos incluyen:
  - Mensaje de error detallado
  - Traza de la pila de llamadas
  - Valores esperados vs. valores obtenidos
  
Para explorar estos resultados:
```bash
# Ver archivos de reportes de tests
dir target\surefire-reports

# Ver el contenido de un reporte específico (ejemplo)
type target\surefire-reports\com.project.wrappergemini.service.GeminiServiceTest.txt
```

## Patrón DTO y Arquitectura en Capas

### ¿Qué son los DTOs?

Los DTOs (Data Transfer Objects) son objetos utilizados para transferir datos entre subsistemas de una aplicación. En este proyecto, se utilizan para:

1. **Separar las capas de la aplicación:**
   - Desacoplar la capa de persistencia (entidades) de la capa de presentación (vistas)
   - Facilitar la evolución independiente de cada capa

2. **Controlar la transferencia de datos:**
   - Exponer solo los datos necesarios para cada operación
   - Prevenir la exposición accidental de datos sensibles
   - Optimizar la cantidad de datos transferidos

3. **Mejorar la seguridad:**
   - Evitar vulnerabilidades como mass assignment
   - Validar datos de entrada con anotaciones específicas
   - Implementar transformaciones seguras de datos

### DTOs en este proyecto

La aplicación utiliza tres DTOs principales:

1. **ConversationDTO:**
   - Traslada información de conversaciones entre el controlador y la vista
   - Separa la entidad de persistencia de los datos mostrados al usuario
   - Facilita la presentación y manipulación de conversaciones en la interfaz

2. **GeminiRequest:**
   - Estructura exacta para construir peticiones a la API de Gemini
   - Configuración de parámetros de generación (temperatura, topP, etc.)
   - Facilita la serialización correcta al formato JSON esperado por la API

3. **GeminiResponse:**
   - Deserializa correctamente las respuestas de la API
   - Maneja variantes y campos opcionales de la respuesta
   - Permite extraer fácilmente el texto generado

### Beneficios obtenidos

La implementación de DTOs ha proporcionado:

- **Mejor mantenibilidad:** Los cambios en entidades no afectan a la interfaz y viceversa
- **Mayor flexibilidad:** Cada DTO puede adaptarse a requisitos específicos
- **Mejor rendimiento:** Solo se transfieren los datos necesarios
- **Seguridad mejorada:** Control preciso sobre los datos expuestos
- **Validación específica:** Cada DTO implementa sus propias reglas de validación 