package com.project.wrappergemini.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa una conversación con el modelo Gemini
 * Almacena la pregunta del usuario, la respuesta del modelo y la marca de tiempo
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    
    /**
     * Identificador único de la conversación
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Pregunta realizada por el usuario
     */
    @Column(length = 2000)
    private String question;
    
    /**
     * Respuesta generada por el modelo Gemini
     */
    @Column(length = 10000)
    private String answer;
    
    /**
     * Fecha y hora en que se realizó la conversación
     */
    private LocalDateTime timestamp;
    
    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad
     * Establece la marca de tiempo actual
     */
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
} 