# Spring Boot Gemini AI Wrapper byyyy sebas 

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
src/main/java/com/project/wrappergemini/
├── controller/            # Controladores REST
├── model/                 # Clases de entidad
├── repository/            # Interfaces de acceso a datos
├── service/               # Lógica de negocio
└── WrapperGeminiApplication.java  # Clase principal
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

- **Bootstrap 5**: Framework CSS utilizado para obtener una interfaz responsiva y moderna con mínimo esfuerzo, proporcionando componentes prediseñados y un sistema de rejilla flexible.

- **Font Awesome**: Biblioteca de iconos vectoriales que mejora la experiencia visual y la usabilidad de la interfaz con símbolos reconocibles e intuitivos.

- **API de Google Gemini**: Motor de IA seleccionado por su potente capacidad para generar texto coherente y contextualmente relevante, permitiendo una interacción natural con los usuarios. 