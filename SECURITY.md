# Guía de Seguridad para API Token Management

## Buenas Prácticas Implementadas

1. **Variables de Entorno**: 
   - Utilizamos variables de entorno para almacenar tokens de API sensibles
   - El token se carga desde un archivo `.env` que no se incluye en el control de versiones

2. **Validación de Tokens**:
   - El servicio valida que el token API esté presente antes de realizar solicitudes
   - Se manejan correctamente los errores cuando el token no está configurado

3. **Seguridad de H2 Console**:
   - Acceso a la consola H2 limitado solo a conexiones locales
   - La consola H2 requiere credenciales para acceder a la base de datos

4. **Seguridad de Cookies**:
   - Las cookies de sesión están configuradas con `HttpOnly` para prevenir acceso por JavaScript
   - Las cookies están marcadas como `Secure` para uso solo con HTTPS

## Recomendaciones Adicionales para Entornos de Producción

1. **Rotación de Tokens**:
   - Cambiar el token API periódicamente (cada 30-90 días)
   - Implementar un proceso para actualizar tokens sin tiempo de inactividad

2. **Almacenamiento Seguro de Secretos**:
   - En producción, utilizar servicios como AWS Secrets Manager, HashiCorp Vault o Google Secret Manager
   - Evitar almacenar tokens en archivos de configuración o código fuente

3. **Monitoreo y Logging**:
   - Implementar registro de auditoría para todas las solicitudes API
   - Monitorear activamente el uso de tokens para detectar actividad sospechosa

4. **Control de Acceso**:
   - Implementar principio de privilegio mínimo para acceso a tokens
   - Utilizar tokens con alcance limitado cuando sea posible

5. **Consideraciones para Contenedores**:
   - No incluir tokens API en imágenes Docker
   - Utilizar secretos de Kubernetes o variables de entorno para inyectar tokens en tiempo de ejecución

## Procedimiento para Actualizar el Token API

1. Actualiza el archivo `.env` con el nuevo token:
   ```
   GEMINI_API_KEY=tu_nuevo_token_aquí
   GEMINI_MODEL=gemini-2.0-flash
   ```

2. Reinicia la aplicación para que cargue el nuevo token:
   ```
   mvnw spring-boot:run
   ```

3. Si estás utilizando Docker:
   ```
   docker run -p 8080:8080 -e GEMINI_API_KEY=tu_nuevo_token_aquí -e GEMINI_MODEL=gemini-2.0-flash wrapper-gemini
   ``` 