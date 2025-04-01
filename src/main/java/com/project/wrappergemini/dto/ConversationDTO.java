package com.project.wrappergemini.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la transferencia de datos de conversación
 * 
 * Esta clase se utiliza para transferir información de conversaciones entre las capas
 * del sistema sin exponer directamente la entidad de persistencia. Proporciona una estructura
 * clara para la comunicación entre el controlador y la vista.
 */
@Data
public class ConversationDTO {
    /**
     * Identificador único de la conversación
     */
    private Long id;
    
    /**
     * Pregunta o prompt enviado por el usuario al modelo Gemini
     */
    private String question;
    
    /**
     * Respuesta generada por el modelo Gemini
     */
    private String answer;
    
    /**
     * Fecha y hora en que se realizó la conversación
     */
    private LocalDateTime timestamp;
} 