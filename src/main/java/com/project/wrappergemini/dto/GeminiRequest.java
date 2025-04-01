package com.project.wrappergemini.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO para las solicitudes a la API de Gemini
 * 
 * Esta clase define la estructura exacta para construir peticiones compatibles con
 * la API de Gemini. La estructura sigue el formato oficial de la API:
 * https://ai.google.dev/api/rest/v1beta/models/generateContent
 */
@Data
public class GeminiRequest {
    /**
     * Lista de contenidos que forman la petición
     * Incluye el texto del prompt y otros detalles
     */
    private List<Content> contents;
    
    /**
     * Configuración para controlar la generación del modelo
     * Permite ajustar parámetros como temperatura, topP, etc.
     */
    private GenerationConfig generationConfig;

    /**
     * Clase interna que representa un contenido en la petición
     * Cada contenido contiene una lista de partes que forman el mensaje
     */
    @Data
    public static class Content {
        /**
         * Lista de partes que componen el contenido
         * Típicamente incluye texto, pero podría incluir otros tipos
         */
        private List<Part> parts;
    }

    /**
     * Clase interna que representa una parte de un contenido
     * Generalmente contiene texto, aunque la API soporta también otros formatos
     */
    @Data
    public static class Part {
        /**
         * Texto del prompt o parte del mensaje
         */
        private String text;
    }

    /**
     * Clase interna para configurar los parámetros de generación
     * Afecta cómo el modelo genera el texto de respuesta
     */
    @Data
    public static class GenerationConfig {
        /**
         * Controla la aleatoriedad de las respuestas (0.0 a 1.0)
         * Valores más altos producen respuestas más creativas y diversas
         */
        private double temperature = 0.7;
        
        /**
         * Limita tokens considerados a los de mayor probabilidad (0.0 a 1.0)
         * Valores más altos consideran más opciones
         */
        private double topP = 0.95;
        
        /**
         * Limita los tokens considerados a los k más probables
         * Valores más altos consideran más opciones
         */
        private int topK = 40;
    }
} 