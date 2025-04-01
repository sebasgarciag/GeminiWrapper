package com.project.wrappergemini.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO para las respuestas de la API de Gemini
 * 
 * Esta clase define la estructura exacta para deserializar respuestas de la API de Gemini.
 * Sigue el formato oficial de respuesta definido en la documentación:
 * https://ai.google.dev/api/rest/v1beta/models/generateContent#response-body
 */
@Data
public class GeminiResponse {
    /**
     * Lista de respuestas candidatas generadas por el modelo
     * Normalmente solo incluye una respuesta, pero podría tener varias
     */
    private List<Candidate> candidates;
    
    /**
     * Feedback sobre el prompt enviado
     * Incluye calificaciones de seguridad y otros metadatos
     */
    private PromptFeedback promptFeedback;

    /**
     * Clase interna que representa una respuesta candidata
     * Contiene el contenido generado y metadatos relacionados
     */
    @Data
    public static class Candidate {
        /**
         * Contenido generado por el modelo
         */
        private Content content;
        
        /**
         * Razón por la que la generación terminó
         * Ej: "STOP", "MAX_TOKENS", "SAFETY", etc.
         */
        private String finishReason;
        
        /**
         * Índice del candidato en la lista de respuestas
         */
        private int index;
        
        /**
         * Calificaciones de seguridad para la respuesta generada
         */
        private List<SafetyRating> safetyRatings;
    }

    /**
     * Clase interna que representa el contenido de una respuesta
     */
    @Data
    public static class Content {
        /**
         * Lista de partes que componen la respuesta
         * Generalmente contiene texto, aunque podría incluir otros formatos
         */
        private List<Part> parts;
        
        /**
         * Calificaciones de seguridad específicas para este contenido
         */
        private List<SafetyRating> safetyRatings;
        
        /**
         * Rol asignado al contenido (ej: "user", "model")
         * Utilizado en conversaciones multiturno
         */
        private String role;
    }

    /**
     * Clase interna que representa una parte del contenido
     */
    @Data
    public static class Part {
        /**
         * Texto generado por el modelo
         */
        private String text;
    }

    /**
     * Clase interna que representa una calificación de seguridad
     * Utilizada para evaluar tanto el prompt como la respuesta
     */
    @Data
    public static class SafetyRating {
        /**
         * Categoría de la calificación de seguridad
         * Ej: "HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_DANGEROUS_CONTENT", etc.
         */
        private String category;
        
        /**
         * Nivel de probabilidad para la categoría de seguridad
         * Ej: "NEGLIGIBLE", "LOW", "MEDIUM", "HIGH"
         */
        private String probability;
    }

    /**
     * Clase interna que contiene feedback sobre el prompt enviado
     */
    @Data
    public static class PromptFeedback {
        /**
         * Calificaciones de seguridad para el prompt enviado
         */
        private List<SafetyRating> safetyRatings;
    }
} 